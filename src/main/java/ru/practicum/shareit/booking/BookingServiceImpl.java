package ru.practicum.shareit.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingValidator bookingValidator,
                              UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingValidator = bookingValidator;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Booking createBooking(long bookerId, Booking booking) {
        LOG.info("Создание бронирования");
        if (!bookingValidator.validate(booking)) {
            LOG.warn("Валидация бронирования не пройдена");
            throw new ValidationException();
        }
        if (bookerId == booking.getItem().getOwner()) {
            LOG.warn("Арендатор не может быть владельцем вещи");
            throw new NotFoundException();
        }
        booking.setBooker(userRepository.findById(bookerId).get());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(long ownerId, long bookingId, Boolean approved) {
        LOG.info("Обновление бронирования");
        Optional<Booking> optBooking = bookingRepository.findById(bookingId);
        if (optBooking.isEmpty()) {
            throw new NotFoundException();
        }
        Booking booking = optBooking.get();
        if (booking.getItem().getOwner() != ownerId) {
            LOG.warn("У пользовтеля нет этого предмета");
            throw new NotFoundException();
        }
        if (booking.getStatus() != Status.WAITING) {
            LOG.warn("Бронирование уже обработано владельцем");
            throw new ValidationException();
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(long userId, long id) {
        Optional<Booking> optBooking = bookingRepository.findById(id);
        if (optBooking.isEmpty()) {
            LOG.warn("Бронирования не найдено");
            throw new NotFoundException();
        }
        LOG.warn("Бронирования найдено");
        Booking booking = optBooking.get();
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner()) {
            LOG.warn("У пользователя нет такого бронирования");
            throw new NotFoundException();
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingList(long bookerId, String state) {
        switch (state) {
            case "ALL":
                return bookingRepository.findByBooker_Id(bookerId, Sort.by("start").descending());
            case "CURRENT" :
                return bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter
                        (bookerId, LocalDateTime.now(), LocalDateTime.now(), Sort.by("start").descending());
            case "PAST":
                return bookingRepository.findByBooker_IdAndEndIsBefore(bookerId, LocalDateTime.now(),
                        Sort.by("start").descending());
            case "FUTURE":
                return bookingRepository.findByBooker_IdAndStartIsAfter(bookerId, LocalDateTime.now(),
                        Sort.by("start").descending());
            case "WAITING":
                return bookingRepository.findByBooker_IdAndStatusEquals(bookerId, Status.WAITING,
                        Sort.by("start").descending());
            case "REJECTED":
                return bookingRepository.findByBooker_IdAndStatusEquals(bookerId, Status.REJECTED,
                        Sort.by("start").descending());
            default:
                LOG.warn("Некорректное значение state");
                throw new RuntimeException(String.format("Unknown state: %s", state));
        }
    }

    @Override
    public List<Booking> getBookingByOwner(long ownerId, String state) {
        List<Item> itemList = itemRepository.findItemsByUser(ownerId);
        List<Long> itemIdList = new ArrayList<>();
        for (Item item : itemList) {
            itemIdList.add(item.getId());
        }
        switch (state) {
            case "ALL":
                return bookingRepository.findByItem_IdIn(itemIdList, Sort.by("start").descending());
            case "CURRENT" :
                return bookingRepository.findByItem_IdInAndStartIsBeforeAndEndIsAfter(itemIdList,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.by("start").descending());
            case "PAST":
                return bookingRepository.findByItem_IdInAndEndIsBefore(itemIdList, LocalDateTime.now(),
                        Sort.by("start").descending());
            case "FUTURE":
                return bookingRepository.findByItem_IdInAndStartIsAfter(itemIdList, LocalDateTime.now(),
                        Sort.by("start").descending());
            case "WAITING":
                return bookingRepository.findByItem_IdInAndStatusEquals(itemIdList, Status.WAITING,
                        Sort.by("start").descending());
            case "REJECTED":
                return bookingRepository.findByItem_IdInAndStatusEquals(itemIdList, Status.REJECTED,
                        Sort.by("start").descending());
            default:
                LOG.warn("Некорректное значение state");
                throw new ValidationException(String.format("Unknown state: %s", state));
        }
    }
}

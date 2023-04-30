package ru.practicum.shareit.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public BookingController(ItemService itemService, UserService userService, BookingService bookingService) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                             @RequestBody BookingDto bookingDto) {
        LOG.info("Получен запрос добавления нового бронирования");
        Booking booking = MappingBooking.mapToBooking(bookingDto,
                itemService.findItemById(bookingDto.getItemId()),
                userService.findUserById(bookerId));
        itemService.checkItem(booking.getItem().getId());
        userService.checkUser(bookerId);
        return bookingService.createBooking(bookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking change(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @PathVariable int bookingId,
                          @RequestParam Boolean approved) {
        LOG.info("Получен запрос обновления бронирования");
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable long bookingId) {
        LOG.info("Получен запрос просмотра бронирования");
        userService.checkUser(userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingList(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                        @RequestParam (defaultValue = "ALL") String state,
                                        @RequestParam(required = false) Integer from,
                                        @RequestParam(required = false) Integer size) {
        LOG.info("Получен запрос просмотра бронирования");
        userService.checkUser(bookerId);
        return bookingService.getBookingList(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @RequestParam(defaultValue = "ALL") String state,
                                           @RequestParam(required = false) Integer from,
                                           @RequestParam(required = false) Integer size) {
        LOG.info("Получен запрос просмотра бронирования");
        userService.checkUser(ownerId);
        return bookingService.getBookingByOwner(ownerId, state, from, size);
    }
}

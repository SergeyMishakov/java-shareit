package ru.practicum.shareit.booking;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingIntegrationTest {

    private final EntityManager em;
    private final BookingServiceImpl bookingService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveBooking() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        userService.createUser(user);
        User booker = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        bookingService.createBooking(booker.getId(), booking);
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking itogBooking = query.setParameter("id", booking.getId()).getSingleResult();
        assertThat(itogBooking.getId(), notNullValue());
        assertThat(itogBooking.getStart(), equalTo(booking.getStart()));
        assertThat(itogBooking.getEnd(), equalTo(booking.getEnd()));
        assertThat(itogBooking.getItem(), equalTo(booking.getItem()));
        assertThat(itogBooking.getBooker(), equalTo(booking.getBooker()));
        assertThat(itogBooking.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void getBookingListAll() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "ALL", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListFuture() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "FUTURE", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListPast() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(4));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "PAST", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListCurrent() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "CURRENT", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListWaiting() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "WAITING", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListRejected() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.REJECTED);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "REJECTED", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListAllPage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "ALL", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListFuturePage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "FUTURE", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListPastPage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(4));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "PAST", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListCurrentPage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "CURRENT", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingListWaitingPage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = new ArrayList<>();
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "WAITING", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
    }

    @Test
    void getBookingListRejectedPage() {
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(1, item);
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Booking booking = new Booking();
        booking.setStatus(Status.REJECTED);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = new ArrayList<>();
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingList(booker2.getId(), "REJECTED", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
    }

    @Test
    void getBookingByOwnerAll() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "ALL", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerFuture() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "FUTURE", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerPast() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(4));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "PAST", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerCurrent() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "CURRENT", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerWaiting() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "WAITING", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerRejected() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.REJECTED);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "REJECTED", null, null);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerAllPage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "ALL", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerFuturePage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        bookingService.createBooking(booker2.getId(), booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "FUTURE", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerPastPage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(4));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "PAST", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerCurrentPage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        List<Booking> sourceBookingList = List.of(booking);
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "CURRENT", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
        assertThat(sourceBookingList.get(0).getItem().getId(), equalTo(targetBookingList.get(0).getItem().getId()));
    }

    @Test
    void getBookingByOwnerWaitingPage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = new ArrayList<>();
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "WAITING", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
    }

    @Test
    void getBookingByOwnerRejectedPage() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.REJECTED);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = new ArrayList<>();
        booking.setBooker(userRepository.findById(booker2.getId()).get());
        bookingRepository.save(booking);
        List<Booking> targetBookingList = bookingService.getBookingByOwner(booker1.getId(), "REJECTED", 0, 1);
        assertThat(sourceBookingList.size(), equalTo(targetBookingList.size()));
    }

    @Test
    void getBookingById() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        User booker1 = userService.createUser(user);
        User booker2 = userService.createUser(user2);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(booker1.getId(), item);
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> sourceBookingList = List.of(booking);
        Booking resultBooking = bookingService.createBooking(booker2.getId(), booking);
        Booking targetBooking = bookingService.getBookingById(booker2.getId(), resultBooking.getId());
        assertThat(resultBooking.getId(), equalTo(targetBooking.getId()));
    }
}
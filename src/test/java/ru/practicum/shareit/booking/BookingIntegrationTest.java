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
import ru.practicum.shareit.user.UserServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
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
    void getBookingList() {
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
}
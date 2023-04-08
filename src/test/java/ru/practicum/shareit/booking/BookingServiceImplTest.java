package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    @Test
    void createBooking() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setOwner(1L);
        item.setAvailable(true);
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        Booking savedBooking = booking;
        savedBooking.setId(1L);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        BookingValidator bookingValidator = new BookingValidator();
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository,
                                                               bookingValidator,
                                                               mockUserRepository,
                                                               mockItemRepository);
        Mockito
                .when(mockUserRepository.findById(2L))
                .thenReturn(Optional.of(user2));
        Mockito
                .when(mockBookingRepository.save(booking))
                .thenReturn(savedBooking);
        Booking b = bookingService.createBooking(2L, booking);
        Assertions.assertEquals(savedBooking, b);
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookingList() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setOwner(1L);
        item.setAvailable(true);
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        BookingValidator bookingValidator = new BookingValidator();
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository,
                bookingValidator,
                mockUserRepository,
                mockItemRepository);
        Mockito
                .when(mockBookingRepository.findByBooker_Id(2L, Sort.by("start").descending()))
                .thenReturn(bookingList);
        List<Booking> bl = bookingService.getBookingList(2L, "ALL", null, null);
        Assertions.assertEquals(bookingList, bl);
    }

    @Test
    void getBookingByOwner() {
    }
}
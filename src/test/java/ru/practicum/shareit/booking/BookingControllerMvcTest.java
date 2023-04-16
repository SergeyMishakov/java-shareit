package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerMvcTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        BookingDto startBookingDto = new BookingDto();
        startBookingDto.setItemId(1L);
        startBookingDto.setStart(LocalDateTime.now().plusDays(1));
        startBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking itogBooking = new Booking();
        itogBooking.setStart(LocalDateTime.now().plusDays(1));
        itogBooking.setEnd(LocalDateTime.now().plusDays(2));
        itogBooking.setId(1L);
        itogBooking.setBooker(user);
        itogBooking.setStatus(Status.WAITING);
        itogBooking.setItem(item);
        when(bookingService.createBooking(1L, MappingBooking.mapToBooking(startBookingDto, item, user)))
                .thenReturn(itogBooking);
        when(itemService.findItemById(1L))
                .thenReturn(item);
        when(userService.findUserById(1L))
                .thenReturn(user);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(startBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }

    @Test
    void change() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        BookingDto startBookingDto = new BookingDto();
        startBookingDto.setItemId(1L);
        startBookingDto.setStart(LocalDateTime.now().plusDays(1));
        startBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking itogBooking = new Booking();
        itogBooking.setStart(LocalDateTime.now().plusDays(1));
        itogBooking.setEnd(LocalDateTime.now().plusDays(2));
        itogBooking.setId(1L);
        itogBooking.setBooker(user);
        itogBooking.setStatus(Status.WAITING);
        itogBooking.setItem(item);
        when(bookingService.updateBooking(1L, 1L, true))
                .thenReturn(itogBooking);
        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }

    @Test
    void getBookingById() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        BookingDto startBookingDto = new BookingDto();
        startBookingDto.setItemId(1L);
        startBookingDto.setStart(LocalDateTime.now().plusDays(1));
        startBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking itogBooking = new Booking();
        itogBooking.setStart(LocalDateTime.now().plusDays(1));
        itogBooking.setEnd(LocalDateTime.now().plusDays(2));
        itogBooking.setId(1L);
        itogBooking.setBooker(user);
        itogBooking.setStatus(Status.WAITING);
        itogBooking.setItem(item);
        when(bookingService.getBookingById(1L, 1))
                .thenReturn(itogBooking);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }

    @Test
    void getBookingList() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        BookingDto startBookingDto = new BookingDto();
        startBookingDto.setItemId(1L);
        startBookingDto.setStart(LocalDateTime.now().plusDays(1));
        startBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking itogBooking = new Booking();
        itogBooking.setStart(LocalDateTime.now().plusDays(1));
        itogBooking.setEnd(LocalDateTime.now().plusDays(2));
        itogBooking.setId(1L);
        itogBooking.setBooker(user);
        itogBooking.setStatus(Status.WAITING);
        itogBooking.setItem(item);
        List<Booking> bookingList = List.of(itogBooking);
        when(bookingService.getBookingList(1L, "ALL", 0, 1))
                .thenReturn(bookingList);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$.[0].booker.name", is(user.getName())));
    }

    @Test
    void getBookingByOwner() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        BookingDto startBookingDto = new BookingDto();
        startBookingDto.setItemId(1L);
        startBookingDto.setStart(LocalDateTime.now().plusDays(1));
        startBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        Booking itogBooking = new Booking();
        itogBooking.setStart(LocalDateTime.now().plusDays(1));
        itogBooking.setEnd(LocalDateTime.now().plusDays(2));
        itogBooking.setId(1L);
        itogBooking.setBooker(user);
        itogBooking.setStatus(Status.WAITING);
        itogBooking.setItem(item);
        List<Booking> bookingList = List.of(itogBooking);
        when(bookingService.getBookingByOwner(1L, "ALL", 0, 1))
                .thenReturn(bookingList);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$.[0].booker.name", is(user.getName())));
    }
}
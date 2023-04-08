package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {

    private final EntityManager em;
    private final BookingServiceImpl bookingService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    private final ItemRequestServiceImpl itemRequestService;

    @Test
    void createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        userService.createUser(user);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        userService.createUser(user2);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User targetUser = query.setParameter("id", user.getId()).getSingleResult();
        assertThat(targetUser.getId(), notNullValue());
        assertThat(user.getName(), equalTo(targetUser.getName()));
        assertThat(user.getEmail(), equalTo(targetUser.getEmail()));
    }
}
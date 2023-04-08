package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {

    private final EntityManager em;
    private final UserServiceImpl userService;
    private final ItemRequestServiceImpl itemRequestService;

    @Test
    void saveRequest() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        userService.createUser(user);
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        userService.createUser(user2);
        ItemRequest request = new ItemRequest();
        request.setDescription("Нужна вещь");
        request.setCreated(LocalDateTime.now());
        itemRequestService.create(user.getId(), request);
        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.id = :id", ItemRequest.class);
        ItemRequest targetItemRequest = query.setParameter("id", request.getId()).getSingleResult();
        assertThat(targetItemRequest.getId(), notNullValue());
        assertThat(request.getDescription(), equalTo(targetItemRequest.getDescription()));
        assertThat(request.getRequestorId(), equalTo(targetItemRequest.getRequestorId()));
    }

    @Test
    void getAllRequests() {
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
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("Нужна вещь1");
        request1.setCreated(LocalDateTime.now());
        request1.setRequestorId(1L);
        itemRequestService.create(1L, request1);
        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setDescription("Нужна вещь2");
        request2.setCreated(LocalDateTime.now());
        request2.setRequestorId(1L);
        itemRequestService.create(1L, request2);
        List<ItemRequestDto> sourceItemRequestDtoList = new ArrayList<>();
        sourceItemRequestDtoList.add(MappingRequest.mapToRequestDto(request1));
        sourceItemRequestDtoList.add(MappingRequest.mapToRequestDto(request2));
        List<ItemRequestDto> targetItemRequestDtoList = itemRequestService.getAllRequests(2L, 0, 2);
        assertThat(sourceItemRequestDtoList.size(), equalTo(targetItemRequestDtoList.size()));
    }
}
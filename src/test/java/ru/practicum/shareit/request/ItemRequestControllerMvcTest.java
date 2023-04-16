package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerMvcTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private User user = createUser();

    private ItemRequest request = createItemRequest();

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@gmail.com");
        return user;
    }

    private ItemRequest createItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Описание запроса");
        itemRequest.setRequestorId(1L);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    @Test
    void create() throws Exception {
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("Описание запроса");
        when(itemRequestService.create(1L, itemRequest1))
                .thenReturn(MappingRequest.mapToRequestDto(request));
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    @Test
    void getRequestList() throws Exception {
        List<ItemRequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(MappingRequest.mapToRequestDto(createItemRequest()));
        when(itemRequestService.getRequestList(1L))
                .thenReturn(requestDtoList);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestDtoList.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDtoList.get(0).getDescription())));
    }

    @Test
    void getAllRequests() throws Exception {
        List<ItemRequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(MappingRequest.mapToRequestDto(createItemRequest()));
        when(itemRequestService.getAllRequests(2L, 0, 1))
                .thenReturn(requestDtoList);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L)
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestDtoList.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDtoList.get(0).getDescription())));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L))
                .thenReturn(MappingRequest.mapToRequestDto(request));
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }
}
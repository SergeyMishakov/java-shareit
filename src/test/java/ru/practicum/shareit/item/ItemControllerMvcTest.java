package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerMvcTest {

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

    Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        return item;
    }

    @Test
    void create() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание вещи");
        itemDto.setAvailable(true);
        when(itemService.createItem(1L, MappingItem.mapToItem(itemDto)))
                .thenReturn(createItem());
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void change() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Вещь обновленная");
        itemDto.setDescription("Описание обновленной вещи");
        itemDto.setAvailable(true);
        Item changedItem = createItem();
        changedItem.setName("Вещь обновленная");
        changedItem.setDescription("Описание обновленной вещи");
        when(itemService.changeItem(1L, 1L, MappingItem.mapToItem(itemDto)))
                .thenReturn(changedItem);
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void getItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Вещь обновленная");
        itemDto.setDescription("Описание обновленной вещи");
        itemDto.setAvailable(true);
        Item changedItem = createItem();
        changedItem.setName("Вещь обновленная");
        changedItem.setDescription("Описание обновленной вещи");
        when(itemService.findItemDtoById(1L, 1L))
                .thenReturn(itemDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void getItemList() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Вещь обновленная");
        itemDto.setDescription("Описание обновленной вещи");
        itemDto.setAvailable(true);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        when(itemService.findItemsByUser(1, 0, 1))
                .thenReturn(itemDtoList);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoList.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDtoList.get(0).getName())));
    }

    @Test
    void searchItem() {
    }

    @Test
    void addComment() {
    }
}
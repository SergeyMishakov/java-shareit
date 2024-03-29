package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Test
    void createItem() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        userService.createUser(user);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        itemService.createItem(user.getId(), item);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item targetItem = query.setParameter("id", item.getId()).getSingleResult();
        assertThat(targetItem.getId(), notNullValue());
        assertThat(targetItem.getName(), equalTo(item.getName()));
        assertThat(targetItem.getOwner(), equalTo(item.getOwner()));
        assertThat(targetItem.getDescription(), equalTo(item.getDescription()));
        assertThat(targetItem.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void changeItem() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User savedUser = userService.createUser(user);
        Item item = new Item();
        item.setName("Вещь2");
        item.setDescription("Полезная вещь");
        item.setAvailable(true);
        Item savedItem = itemService.createItem(user.getId(), item);
        Item updatedItem = new Item();
        updatedItem.setId(savedItem.getId());
        updatedItem.setName("Вещь2");
        updatedItem.setDescription("Обновленная полезная вещь");
        item.setAvailable(true);
        Item sourceItem = itemService.changeItem(savedUser.getId(), savedItem.getId(), updatedItem);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item targetItem = query.setParameter("id", updatedItem.getId()).getSingleResult();
        assertThat(targetItem.getId(), notNullValue());
        assertThat(targetItem.getName(), equalTo(sourceItem.getName()));
        assertThat(targetItem.getOwner(), equalTo(sourceItem.getOwner()));
        assertThat(targetItem.getDescription(), equalTo(sourceItem.getDescription()));
        assertThat(targetItem.getAvailable(), equalTo(sourceItem.getAvailable()));
    }

    @Test
    void findItemsByUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        userService.createUser(user);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Вещь1");
        item1.setDescription("Полезная вещь11");
        item1.setOwner(1L);
        item1.setAvailable(true);
        itemService.createItem(1L, item1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Вещь2");
        item2.setDescription("Полезная вещь2");
        item2.setOwner(1L);
        item2.setAvailable(true);
        itemService.createItem(1L, item2);
        List<ItemDto> sourceItemDtoList = new ArrayList<>();
        sourceItemDtoList.add(MappingItem.mapToItemDto(item1));
        sourceItemDtoList.add(MappingItem.mapToItemDto(item2));
        List<ItemDto> targetItemDtoList = itemService.findItemsByUser(1L, 0,2);
        assertThat(sourceItemDtoList.size(), equalTo(targetItemDtoList.size()));
    }

    @Test
    void findItemDtoById() {
        User user = new User();
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User savedUser = userService.createUser(user);
        Item item1 = new Item();
        item1.setName("Вещь1");
        item1.setDescription("Полезная вещь11");
        item1.setOwner(savedUser.getId());
        item1.setAvailable(true);
        Item savedItem = itemService.createItem(savedUser.getId(), item1);
        ItemDto sourceItemDto = MappingItem.mapToItemDto(item1);
        ItemDto targetItemDtoList = itemService.findItemDtoById(savedUser.getId(), savedItem.getId());
        assertThat(sourceItemDto, equalTo(targetItemDtoList));
    }

    @Test
    void searchItem() {
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        userService.createUser(user);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Выхухоливатель");
        item1.setDescription("Очень полезный");
        item1.setOwner(1L);
        item1.setAvailable(true);
        itemService.createItem(1L, item1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Вещь2");
        item2.setDescription("Поддакиватель");
        item2.setOwner(1L);
        item2.setAvailable(true);
        itemService.createItem(1L, item2);
        List<Item> sourceItemList = List.of(item1, item2);
        List<Item> targetItemList = itemService.searchItem("тель", 0, 2);
        assertThat(sourceItemList.size(), equalTo(targetItemList.size()));
    }
}
package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemService {
    Item createItem(long userId, Item item);

    Item changeItem(long userId, long itemId, Item item);

    Item findItemById(long id);

    List<Item> findItemsByUser(long userId);

    List<Item> searchItem(String text);

    void checkOwner(long userId, long itemId);
}

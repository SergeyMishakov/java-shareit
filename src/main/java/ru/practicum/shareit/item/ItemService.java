package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(long userId, Item item);

    Item changeItem(long userId, long itemId, Item item);

    Item findItemById(long id);

    List<Item> findItemsByUser(long userId);

    List<Item> searchItem(String text);

    void checkOwner(long userId, long itemId);
}

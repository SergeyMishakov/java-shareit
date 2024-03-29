package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item createItem(Item item);

    Item changeItem(Item item);

    Optional<Item> findItemById(long id);

    List<Item> findItemsByUser(long userId);

    List<Item> searchItem(String text);
}

package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(long userId, Item item);

    Item changeItem(long userId, long itemId, Item item);

    ItemDto findItemDtoById(long userId, long id);

    Item findItemById(long id);

    List<ItemDto> findItemsByUser(long userId, Integer from, Integer size);

    List<Item> searchItem(String text, Integer from, Integer size);

    void checkOwner(long userId, long itemId);

    void checkItem(long itemId);

    CommentDto addComment(long userId, long itemId, Comment comment);
}

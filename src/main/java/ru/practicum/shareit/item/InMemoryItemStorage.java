package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    Map<Long, Item> itemList = new HashMap<>();
    @Override
    public Item createItem(Item item) {
        itemList.put(item.getId(), item);
        return item;
    }

    @Override
    public Item changeItem(Item item) {
        Item itemToUpdate = itemList.get(item.getId());
        if (item.getName() != null) {
            itemToUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemToUpdate.setAvailable(item.getAvailable());
        }
        itemList.put(itemToUpdate.getId(), itemToUpdate);
        return itemToUpdate;
    }

    @Override
    public Item findItemById(long id) {
        return itemList.get(id);
    }

    @Override
    public List<Item> findItemsByUser(long userId) {
        List<Item> userItemsList = new ArrayList<>();
        for (Item item : itemList.values()) {
            if (item.getOwner() == userId) {
                userItemsList.add(item);
            }
        }
        return userItemsList;
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchList = new ArrayList<>();
        for (Item item : itemList.values()) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(item);
                }
            }
        }
        return searchList;
    }
}

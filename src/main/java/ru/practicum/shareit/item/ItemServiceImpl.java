package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private final ItemStorage itemStorage;
    private final ItemValidator itemValidator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private long idIncrement = 0;

    public ItemServiceImpl(ItemStorage itemStorage, ItemValidator itemValidator) {
        this.itemStorage = itemStorage;
        this.itemValidator = itemValidator;
    }

    @Override
    public Item createItem(long userId, Item item) {
        //проверить предмет
        if (!itemValidator.validate(item)) {
            LOG.warn("Валидация предмета не пройдена");
            throw new ValidationException();
        }
        idIncrement++;
        item.setId(idIncrement);
        item.setOwner(userId);
        return itemStorage.createItem(item);
    }

    @Override
    public Item changeItem(long userId, long itemId, Item item) {
        item.setId(itemId);
        item.setOwner(userId);
        return itemStorage.changeItem(item);
    }

    @Override
    public Item findItemById(long id) {
        return itemStorage.findItemById(id);
    }

    @Override
    public List<Item> findItemsByUser(long userId) {
        return itemStorage.findItemsByUser(userId);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(text);
    }

    @Override
    public void checkOwner(long userId, long itemId) {
        Item item = itemStorage.findItemById(itemId);
        if (item.getOwner() != userId) {
            LOG.warn("У пользоваателя не такого предмета");
            throw new NotFoundException();
        }
    }
}

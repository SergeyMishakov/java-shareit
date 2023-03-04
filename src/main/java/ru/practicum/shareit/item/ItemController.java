package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final MappingItem mappingItem;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public ItemController(ItemService itemService, UserService userService, MappingItem mappingItem) {
        this.itemService = itemService;
        this.userService = userService;
        this.mappingItem = mappingItem;
    }

    //Добавление новой вещи
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        LOG.info("Получен запрос добавления новой вещи");
        userService.checkUser(userId);
        return mappingItem.mapToItemDto(itemService.createItem(userId, mappingItem.mapToItem(itemDto)));
    }

    //Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto change(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable int itemId,
                       @RequestBody ItemDto itemDto) {
        LOG.info("Получен запрос обновления вещи");
        userService.checkUser(userId);
        itemService.checkOwner(userId, itemId);
        return mappingItem.mapToItemDto(itemService.changeItem(userId, itemId, mappingItem.mapToItem(itemDto)));
    }

    //Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id) {
        LOG.info("Получен запрос просмотра вещи");
        return mappingItem.mapToItemDto(itemService.findItemById(id));
    }

    //Просмотр владельцем списка всех его вещей
    @GetMapping
    public List<ItemDto> getItemList(@RequestHeader("X-Sharer-User-Id") Long userId) {
        LOG.info("Получен запрос просмотра всех вещей пользователя");
        return transferToDto(itemService.findItemsByUser(userId));
    }

    //Поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        LOG.info("Получен запрос поиска вещей");
        return transferToDto(itemService.searchItem(text));
    }

    private List<ItemDto> transferToDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(mappingItem.mapToItemDto(item));
        }
        return itemDtoList;
    }
}

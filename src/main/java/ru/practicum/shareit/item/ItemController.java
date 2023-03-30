package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    //Добавление новой вещи
    @PostMapping
    public ItemDto create(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto) {
        LOG.info("Получен запрос добавления новой вещи");
        userService.checkUser(userId);
        return MappingItem.mapToItemDto(itemService.createItem(userId, MappingItem.mapToItem(itemDto)));
    }

    //Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto change(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable int itemId,
                       @RequestBody ItemDto itemDto) {
        LOG.info("Получен запрос обновления вещи");
        userService.checkUser(userId);
        itemService.checkOwner(userId, itemId);
        return MappingItem.mapToItemDto(itemService.changeItem(userId, itemId, MappingItem.mapToItem(itemDto)));
    }

    //Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{id}")
    public ItemDto getItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                           @NotNull @PathVariable int id) {
        LOG.info("Получен запрос просмотра вещи");
        return itemService.findItemDtoById(userId, id);
    }

    //Просмотр владельцем списка всех его вещей
    @GetMapping
    public List<ItemDto> getItemList(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        LOG.info("Получен запрос просмотра всех вещей пользователя");
        return itemService.findItemsByUser(userId);
    }

    //Поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotBlank @RequestParam String text) {
        LOG.info("Получен запрос поиска вещей");
        return MappingItem.transferToDto(itemService.searchItem(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @NotNull @PathVariable long itemId,
                                 @Valid @RequestBody Comment comment) {
        LOG.info("Получен запрос добавления комментария");
        return itemService.addComment(userId, itemId, comment);
    }
}

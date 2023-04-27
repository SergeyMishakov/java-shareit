package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    //Добавление новой вещи
    @PostMapping
    public ResponseEntity<Object> create(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    //Редактирование вещи
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> change(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable int itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Получен запрос обновления вещи");
        return itemClient.changeItem(userId, itemId, itemDto);
    }

    //Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                           @NotNull @PathVariable int id) {
        log.info("Получен запрос просмотра вещи");
        return itemClient.findItemDtoById(userId, id);
    }

    //Просмотр владельцем списка всех его вещей
    @GetMapping
    public ResponseEntity<Object> getItemList(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PositiveOrZero @RequestParam(required = false) Integer from,
                                     @PositiveOrZero @RequestParam(required = false) Integer size) {
        log.info("Получен запрос просмотра всех вещей пользователя");
        return itemClient.findItemsByUser(userId, from, size);
    }

    //Поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                    @PositiveOrZero @RequestParam(required = false) Integer from,
                                    @PositiveOrZero @RequestParam(required = false) Integer size) {
        log.info("Получен запрос поиска вещей");
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @NotNull @PathVariable long itemId,
                                 @Valid @RequestBody Comment comment) {
        log.info("Получен запрос добавления комментария");
        return itemClient.addComment(userId, itemId, comment);
    }
}

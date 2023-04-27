package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
//@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос добавления нового запроса");
        return itemRequestClient.create(requestorId, MappingRequest.mapToRequest(itemRequestDto));
    }

    @GetMapping
    public ResponseEntity<Object> getRequestList(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Получен запрос просмотра запросов пользователя");
        return itemRequestClient.getRequestList(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                               @PositiveOrZero @RequestParam(required = false) Integer from,
                                               @PositiveOrZero @RequestParam(required = false) Integer size) {
        log.info("Получен запрос просмотра всех запросов");
        return itemRequestClient.getAllRequests(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @PathVariable Long requestId) {
        log.info("Получен запрос просмотра конкретного запроса");
        return itemRequestClient.getRequestById(requestId, requestorId);
    }
}

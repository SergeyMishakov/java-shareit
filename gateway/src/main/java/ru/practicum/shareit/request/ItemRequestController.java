package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос добавления нового запроса");
        return itemRequestClient.create(requestorId, MappingRequest.mapToRequest(itemRequestDto));
    }

    @GetMapping
    public ResponseEntity<Object> getRequestList(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Получен запрос просмотра запросов пользователя");
        return itemRequestClient.getRequestList(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос просмотра всех запросов");
        return itemRequestClient.getAllRequests(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @PathVariable Long requestId) {
        log.info("Получен запрос просмотра конкретного запроса");
        return itemRequestClient.getRequestById(requestId, requestorId);
    }
}

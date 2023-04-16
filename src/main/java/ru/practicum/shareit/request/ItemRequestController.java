package ru.practicum.shareit.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.UserService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public ItemRequestController(ItemRequestService itemRequestService, UserService userService) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
    }

    @PostMapping
    public ItemRequestDto create(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        LOG.info("Получен запрос добавления нового запроса");
        userService.checkUser(requestorId);
        return itemRequestService.create(requestorId, MappingRequest.mapToRequest(itemRequestDto));
    }

    @GetMapping
    public List<ItemRequestDto> getRequestList(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        LOG.info("Получен запрос просмотра запросов пользователя");
        userService.checkUser(requestorId);
        return itemRequestService.getRequestList(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                               @PositiveOrZero @RequestParam(required = false) Integer from,
                                               @PositiveOrZero @RequestParam(required = false) Integer size) {
        LOG.info("Получен запрос просмотра всех запросов");
        userService.checkUser(requestorId);
        return itemRequestService.getAllRequests(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@NotNull @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                               @PathVariable Long requestId) {
        LOG.info("Получен запрос просмотра конкретного запроса");
        userService.checkUser(requestorId);
        return itemRequestService.getRequestById(requestId);
    }
}

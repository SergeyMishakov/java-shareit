package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(long requestorId, ItemRequest request);

    List<ItemRequestDto> getRequestList(long requestorId);

    List<ItemRequestDto> getAllRequests(long requestorId, Integer from, Integer size);

    ItemRequestDto getRequestById(long requestId);
}

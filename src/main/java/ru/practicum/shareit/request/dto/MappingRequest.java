package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

public class MappingRequest {

    public static ItemRequestDto mapToRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());
        return dto;
    }
    
    public static ItemRequest mapToRequest(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());
        return request;
    }
}

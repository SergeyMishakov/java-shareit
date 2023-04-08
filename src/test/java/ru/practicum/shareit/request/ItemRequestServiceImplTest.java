package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;

class ItemRequestServiceImplTest {

    @Test
    void create() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Веник нужен");
        ItemRequest savedItemRequest = new ItemRequest();
        savedItemRequest.setId(1L);
        savedItemRequest.setDescription("Веник нужен");
        savedItemRequest.setRequestorId(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Веник нужен");
        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository,
                                                                            mockItemRepository);
        Mockito
                .when(mockItemRequestRepository.save(itemRequest))
                .thenReturn(savedItemRequest);
        ItemRequestDto irid = itemRequestService.create(1L, itemRequest);
        Assertions.assertEquals(itemRequestDto, irid);
    }

    @Test
    void getRequestList() {
    }

    @Test
    void getAllRequests() {
    }

    @Test
    void getRequestById() {
    }
}
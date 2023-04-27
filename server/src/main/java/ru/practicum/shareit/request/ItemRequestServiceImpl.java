package ru.practicum.shareit.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto create(long requestorId, ItemRequest request) {
        request.setRequestorId(requestorId);
        request.setCreated(LocalDateTime.now());
        return MappingRequest.mapToRequestDto(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getRequestList(long requestorId) {
        List<ItemRequest> requestList = itemRequestRepository.findByRequestorId(requestorId,
                Sort.by("created").descending());
        List<ItemRequestDto> requestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : requestList) {
            ItemRequestDto requestDto = MappingRequest.mapToRequestDto(itemRequest);
            List<Item> itemList = itemRepository.findByRequestId(requestDto.getId());
            requestDto.setItems(itemList);
            requestDtoList.add(requestDto);
        }
        return requestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long requestorId, Integer from, Integer size) {
        List<ItemRequest> requestList;
        if (from != null && size != null) {
            if (from == 0 && size == 0) {
                LOG.warn("Нулевое количество запршиваемых данных");
                throw new ValidationException();
            }
            requestList = itemRequestRepository.findByRequestorIdNot(requestorId, size, from);
        } else {
            requestList = itemRequestRepository.findByRequestorIdNot(requestorId,
                    Sort.by("created").descending());
        }
        List<ItemRequestDto> requestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : requestList) {
            ItemRequestDto requestDto = MappingRequest.mapToRequestDto(itemRequest);
            List<Item> itemList = itemRepository.findByRequestId(requestDto.getId());
            requestDto.setItems(itemList);
            requestDtoList.add(requestDto);
        }
        return requestDtoList;
    }

    @Override
    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        ItemRequestDto itemRequestDto = MappingRequest.mapToRequestDto(itemRequest);
        List<Item> itemList = itemRepository.findByRequestId(requestId);
        itemRequestDto.setItems(itemList);
        return itemRequestDto;
    }
}

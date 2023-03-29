package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingComment;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public ItemServiceImpl(ItemRepository itemRepository, ItemValidator itemValidator, CommentRepository commentRepository, CommentValidator commentValidator, UserRepository userRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
        this.commentRepository = commentRepository;
        this.commentValidator = commentValidator;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Item createItem(long userId, Item item) {
        //проверить предмет
        if (!itemValidator.validate(item)) {
            LOG.warn("Валидация предмета не пройдена");
            throw new ValidationException();
        }
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    @Override
    public Item changeItem(long userId, long itemId, Item item) {
        Item changedItem = itemRepository.findById(itemId).get();
        if (item.getName() != null) {
            changedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            changedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            changedItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(changedItem);
    }

    @Override
    public ItemDto findItemDtoById(long userId, long id) {
        if (itemRepository.findById(id).isEmpty()) {
            LOG.warn("Не найдено вещи с таким идентификатором");
            throw new NotFoundException();
        }
        Item item = itemRepository.findById(id).get();
        ItemDto itemDto = MappingItem.mapToItemDto(item);
        if (item.getOwner() == userId) {
            Booking lastBooking = bookingRepository.findLastBookingByItemId(itemDto.getId());
            if (lastBooking != null) {
                itemDto.setLastBooking(MappingBooking.mapToBookingDto(lastBooking));
            }
            Booking nextBooking = bookingRepository.findNextBookingByItemId(itemDto.getId());
            if (nextBooking != null) {
                itemDto.setNextBooking(MappingBooking.mapToBookingDto(nextBooking));
            }
        }
        List<Comment> commentList = commentRepository.findByItemId(id);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = MappingComment.mapToCommentDto(comment);
            commentDto.setAuthorName(userRepository.findById(comment.getAuthor()).get().getName());
            commentDtoList.add(commentDto);
        }
        itemDto.setComments(commentDtoList);
        return itemDto;
    }

    @Override
    public Item findItemById(long id) {
        if (itemRepository.findById(id).isEmpty()) {
            LOG.warn("Не найдено вещи с таким идентификатором");
            throw new NotFoundException();
        }
        return itemRepository.findById(id).get();
    }

    @Override
    public List<ItemDto> findItemsByUser(long userId) {
        List<Item> itemList = itemRepository.findItemsByUser(userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemDto itemDto = MappingItem.mapToItemDto(item);
            Booking lastBooking = bookingRepository.findLastBookingByItemId(itemDto.getId());
            if (lastBooking != null) {
                itemDto.setLastBooking(MappingBooking.mapToBookingDto(lastBooking));
            }
            Booking nextBooking = bookingRepository.findNextBookingByItemId(itemDto.getId());
            if (nextBooking != null) {
                itemDto.setNextBooking(MappingBooking.mapToBookingDto(nextBooking));
            }
            List<Comment> commentList = commentRepository.findByItemId(item.getId());
            List<CommentDto> commentDtoList = MappingComment.mapToCommentDtoList(commentList);
            itemDto.setComments(commentDtoList);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    @Override
    public void checkOwner(long userId, long itemId) {
        Optional<Item> optItem = itemRepository.findById(itemId);
        if (optItem.isEmpty()) {
            throw new NotFoundException();
        }
        Item item = optItem.get();
        if (item.getOwner() != userId) {
            LOG.warn("У пользователя нет такого предмета");
            throw new NotFoundException();
        }
    }

    @Override
    public void checkItem(long itemId) {
        Optional<Item> optItem = itemRepository.findById(itemId);
        if (optItem.isEmpty()) {
            LOG.warn("Предмет для бронирования не найден");
            throw new NotFoundException();
        }
        if (!optItem.get().getAvailable()) {
            LOG.warn("Предмет не доступен для бронирования");
            throw new ValidationException();
        }
    }

    @Override
    public CommentDto addComment(long userId, long itemId, Comment comment) {
        comment.setItem(itemId);
        comment.setAuthor(userId);
        comment.setCreated(LocalDateTime.now());
        if (!commentValidator.validate(comment)) {
            LOG.warn("Валидация отзыва не пройдена");
            throw new ValidationException();
        }
        Comment savedComment = commentRepository.save(comment);
        CommentDto commentDto = MappingComment.mapToCommentDto(savedComment);
        commentDto.setAuthorName(userRepository.findById(userId).get().getName());
        LOG.info("Сохранение нового комментария");
        return commentDto;
    }
}

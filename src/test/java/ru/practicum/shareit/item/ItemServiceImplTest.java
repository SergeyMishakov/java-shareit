package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ItemServiceImplTest {

    @Test
    void createItem() {
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        Item resultItem = new Item();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setOwner(1L);
        resultItem.setAvailable(true);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                                                      mockCommentRepository,
                                                      commentValidator,
                                                      mockUserRepository,
                                                      mockBookingRepository);
        Mockito
                .when(mockItemRepository.save(item))
                .thenReturn(resultItem);
        Item i = itemService.createItem(1L, item);
        Assertions.assertEquals(resultItem, i);
    }

    @Test
    void changeItem() {
        Item oldItem = new Item();
        oldItem.setId(1L);
        oldItem.setName("Вещь1");
        oldItem.setDescription("Описание вещи");
        oldItem.setAvailable(true);
        Item updateItem = new Item();
        updateItem.setId(1L);
        updateItem.setName("Обновленная Вещь1");
        updateItem.setDescription("Описание обновленной вещи");
        updateItem.setOwner(1L);
        updateItem.setAvailable(true);
        Item resultItem = new Item();
        resultItem.setId(1L);
        resultItem.setName("Обновленная Вещь1");
        resultItem.setDescription("Описание обновленной вещи");
        resultItem.setOwner(1L);
        resultItem.setAvailable(true);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(oldItem));
        Mockito
                .when(mockItemRepository.save(Mockito.any()))
                .thenReturn(resultItem);
        Item i = itemService.changeItem(1L, 1L, updateItem);
        Assertions.assertEquals(resultItem, i);
    }

    @Test
    void findItemDtoById() {
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        ItemDto resultItem = new ItemDto();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setAvailable(true);
        resultItem.setComments(new ArrayList<>());
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Mockito
                .when(mockBookingRepository.findLastBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockBookingRepository.findNextBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockCommentRepository.findByItemId(1L))
                .thenReturn(new ArrayList<>());
        ItemDto i = itemService.findItemDtoById(1L, 1L);
        Assertions.assertEquals(resultItem, i);
    }

    @Test
    void findItemDtoByIdWithBooking() {
        /*Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Вещь");
        item1.setDescription("Полезная вещь");
        item1.setOwner(1L);
        item1.setAvailable(true);*/
        User user = new User();
        user.setId(1L);
        user.setName("Василий");
        user.setEmail("vasya@gmail.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Иван");
        user2.setEmail("ivan@gmail.com");
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        Booking lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStatus(Status.WAITING);
        lastBooking.setItem(item);
        lastBooking.setStart(LocalDateTime.now().plusDays(4));
        lastBooking.setEnd(LocalDateTime.now().plusDays(2));
        lastBooking.setBooker(user2);
        Booking nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStatus(Status.WAITING);
        nextBooking.setItem(item);
        nextBooking.setStart(LocalDateTime.now().plusDays(4));
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));
        nextBooking.setBooker(user2);
        ItemDto resultItem = new ItemDto();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setAvailable(true);
        resultItem.setComments(new ArrayList<>());
        resultItem.setLastBooking(MappingBooking.mapToBookingDto(lastBooking));
        resultItem.setNextBooking(MappingBooking.mapToBookingDto(nextBooking));
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Mockito
                .when(mockBookingRepository.findLastBookingByItemId(1L))
                .thenReturn(lastBooking);
        Mockito
                .when(mockBookingRepository.findNextBookingByItemId(1L))
                .thenReturn(nextBooking);
        Mockito
                .when(mockCommentRepository.findByItemId(1L))
                .thenReturn(new ArrayList<>());
        ItemDto i = itemService.findItemDtoById(1L, 1L);
        Assertions.assertEquals(resultItem.getName(), i.getName());
    }

    @Test
    void findItemDtoByIdWithComment() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@gmail.com");
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Текст коммента");
        comment.setAuthor(1L);
        comment.setItem(1L);
        comment.setCreated(LocalDateTime.now());
        CommentDto commentDto = MappingComment.mapToCommentDto(comment);
        List<Comment> commentList = List.of(comment);
        ItemDto resultItem = new ItemDto();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setAvailable(true);
        resultItem.setComments(List.of(commentDto));
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Mockito
                .when(mockBookingRepository.findLastBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockBookingRepository.findNextBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockCommentRepository.findByItemId(1L))
                .thenReturn(commentList);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));
        ItemDto i = itemService.findItemDtoById(1L, 1L);
        Assertions.assertEquals(resultItem, i);
    }

    @Test
    void findItemById() {
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        Item resultItem = new Item();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setOwner(1L);
        resultItem.setAvailable(true);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Item i = itemService.findItemById(1L);
        Assertions.assertEquals(resultItem, i);
    }

    @Test
    void findItemsByUser() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        ItemDto resultItem = new ItemDto();
        resultItem.setId(1L);
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setAvailable(true);
        resultItem.setComments(new ArrayList<>());
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(resultItem);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findItemsByUser(1L))
                .thenReturn(itemList);
        Mockito
                .when(mockBookingRepository.findLastBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockBookingRepository.findNextBookingByItemId(1L))
                .thenReturn(null);
        Mockito
                .when(mockCommentRepository.findByItemId(1L))
                .thenReturn(new ArrayList<>());
        List<ItemDto> il = itemService.findItemsByUser(1L, null, null);
        Assertions.assertEquals(itemDtoList, il);
    }

    @Test
    void searchItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.search("Вещь"))
                .thenReturn(itemList);
        List<Item> il = itemService.searchItem("Вещь", null, null);
        Assertions.assertEquals(itemList, il);
    }

    @Test
    void searchItemEmpty() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.search("Вещь"))
                .thenReturn(itemList);
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.searchItem("Вещь", 0, 0));
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Test
    void searchItemBlank() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.search(""))
                .thenReturn(itemList);
        List<Item> il = itemService.searchItem("", null, null);
        Assertions.assertEquals(new ArrayList<>(), il);
    }

    @Test
    void addComment() {
        Comment comment = new Comment();
        comment.setText("Хорошая вещь");
        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setText("Хорошая вещь");
        savedComment.setAuthor(1L);
        savedComment.setItem(1L);
        User user = new User();
        user.setName("Иннокентий");
        CommentDto resultCommentDto = new CommentDto();
        resultCommentDto.setId(1L);
        resultCommentDto.setText("Хорошая вещь");
        resultCommentDto.setAuthorName("Иннокентий");
        Booking booking = new Booking();
        booking.setId(1L);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockCommentRepository.save(comment))
                .thenReturn(savedComment);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockBookingRepository.findLastBookingByBookerId(1L, 1L))
                .thenReturn(booking);
        CommentDto cd =  itemService.addComment(1L, 1L, comment);
        Assertions.assertEquals(resultCommentDto, cd);
    }

    @Test
    void addCommentError() {
        Comment comment = new Comment();
        comment.setText("");
        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setText("");
        savedComment.setAuthor(1L);
        savedComment.setItem(1L);
        User user = new User();
        user.setName("Иннокентий");
        CommentDto resultCommentDto = new CommentDto();
        resultCommentDto.setId(1L);
        resultCommentDto.setText("");
        resultCommentDto.setAuthorName("Иннокентий");
        Booking booking = new Booking();
        booking.setId(1L);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockCommentRepository.save(comment))
                .thenReturn(savedComment);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito
                .when(mockBookingRepository.findLastBookingByBookerId(1L, 1L))
                .thenReturn(booking);
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.addComment(1L, 1L, comment));
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Test
    void checkOwner() {
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(true);
        item.setOwner(1L);
        Item resultItem = new Item();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setOwner(1L);
        resultItem.setAvailable(true);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.checkOwner(2L, 1L));
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Test
    void checkItem() {
        Item item = new Item();
        item.setName("Вещь1");
        item.setDescription("Описание вещи");
        item.setAvailable(false);
        item.setOwner(1L);
        Item resultItem = new Item();
        resultItem.setName("Вещь1");
        resultItem.setDescription("Описание вещи");
        resultItem.setOwner(1L);
        resultItem.setAvailable(true);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        CommentValidator commentValidator = new CommentValidator(mockBookingRepository);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository,
                mockCommentRepository,
                commentValidator,
                mockUserRepository,
                mockBookingRepository);
        Mockito
                .when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.checkItem(1L));
        Assertions.assertEquals(null, exception.getMessage());
    }
}
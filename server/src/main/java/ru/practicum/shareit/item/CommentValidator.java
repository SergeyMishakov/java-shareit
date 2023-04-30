package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.model.Comment;

@Service
public class CommentValidator {

    private final BookingRepository bookingRepository;

    public CommentValidator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean validate(Comment comment) {
        return checkText(comment.getText()) && checkBooking(comment.getItem(), comment.getAuthor());
    }

    private boolean checkText(String text) {
        return !text.isBlank();
    }

    private boolean checkBooking(long itemId, long userId) {
        Booking booking = bookingRepository.findLastBookingByBookerId(itemId, userId);
        return booking != null;
    }
}

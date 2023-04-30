package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

@Service
public class BookingValidator {
    private boolean checkEnd(BookItemRequestDto booking) {
        LocalDateTime dateNow = LocalDateTime.now();
        return !booking.getEnd().isBefore(dateNow);
    }

    private boolean checkStart(BookItemRequestDto booking) {
        return !booking.getStart().isBefore(LocalDateTime.now());
    }

    private boolean checkStartAndEnd(BookItemRequestDto booking) {
        return booking.getStart().isBefore(booking.getEnd()) &&
                booking.getStart().compareTo(booking.getEnd()) != 0;
    }

    public boolean validate(BookItemRequestDto booking) {
        return checkEnd(booking) && checkStart(booking) && checkStartAndEnd(booking);
    }
}

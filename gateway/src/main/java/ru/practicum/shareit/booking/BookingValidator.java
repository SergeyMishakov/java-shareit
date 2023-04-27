package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingValidator {
    private boolean checkEnd(Booking booking) {
        LocalDateTime dateNow = LocalDateTime.now();
        return !booking.getEnd().isBefore(dateNow);
    }

    private boolean checkStart(Booking booking) {
        return !booking.getStart().isBefore(LocalDateTime.now());
    }

    private boolean checkStartAndEnd(Booking booking) {
        return booking.getStart().isBefore(booking.getEnd()) &&
                booking.getStart().compareTo(booking.getEnd()) != 0;
    }

    public boolean validate(Booking booking) {
        return checkEnd(booking) && checkStart(booking) && checkStartAndEnd(booking);
    }
}

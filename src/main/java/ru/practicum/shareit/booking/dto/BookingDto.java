package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class BookingDto {
    private long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Status status;
    private long itemId;
    private long bookerId;
}

package ru.practicum.shareit.booking;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class Booking {
    private long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingState status;

    public Booking() {
    }
}

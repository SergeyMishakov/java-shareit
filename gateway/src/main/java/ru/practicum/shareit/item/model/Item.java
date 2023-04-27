package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class Item {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private long owner;
    private List<Booking> bookingList;
    private long requestId;
}

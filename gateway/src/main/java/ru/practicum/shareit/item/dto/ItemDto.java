package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ItemDto {
    private long id;
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 512)
    private String description;
    @NotNull
    private Boolean available;
    private BookItemRequestDto lastBooking;
    private BookItemRequestDto nextBooking;
    private List<CommentDto> comments;
    private long requestId;
}

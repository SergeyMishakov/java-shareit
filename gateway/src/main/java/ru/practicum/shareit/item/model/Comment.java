package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class Comment {
    private long id;
    @NotBlank
    @Size(max = 512)
    private String text;
    private long item;
    private long author;
    LocalDateTime created;
}

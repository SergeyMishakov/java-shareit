package ru.practicum.shareit.item.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name="item_id")
    private Long item;
    @Column(name = "author_id", nullable = false)
    private long author;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
}

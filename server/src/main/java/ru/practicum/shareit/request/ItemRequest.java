package ru.practicum.shareit.request;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "description")
    private String description;
    @Column(name = "requestor_id")
    private long requestorId;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}

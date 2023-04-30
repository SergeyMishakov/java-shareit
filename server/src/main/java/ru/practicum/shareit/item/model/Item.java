package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available", nullable = false)
    private Boolean available;
    @Column(name = "owner", nullable = false)
    private long owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Booking> bookingList;
    @Column(name = "request_id")
    private long requestId;
}

package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(max=255)
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Size(max=100)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email missing")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}

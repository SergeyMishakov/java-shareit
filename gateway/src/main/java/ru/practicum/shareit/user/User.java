package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
public class User {
    private long id;
    @NotBlank
    @Size(max = 255)
    private String name;
    @Size(max = 100)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email missing")
    private String email;
}

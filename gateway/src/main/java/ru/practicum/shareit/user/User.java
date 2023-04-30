package ru.practicum.shareit.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class User {

    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;
    @Size(max = 100, groups = {Create.class, Update.class})
    @Email(message = "Email should be valid", groups = {Create.class, Update.class})
    @NotBlank(message = "Email missing", groups = {Create.class})
    private String email;
}

package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserValidator {


    public boolean validate(User user) {
        return emailValidate(user.getEmail()) && nameValidate(user.getName());
    }

    public boolean emailValidate(String email) {
        return email != null && email.contains("@");
    }

    public boolean nameValidate(String name) {
        return name != null && !name.isBlank();
    }
}

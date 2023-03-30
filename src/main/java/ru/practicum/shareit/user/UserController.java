package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public User change(@NotNull @PathVariable long id, @RequestBody User user) {
        return userService.changeUser(id, user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@NotNull @PathVariable long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@NotNull @PathVariable long id) {
        userService.deleteUser(id);
    }
}

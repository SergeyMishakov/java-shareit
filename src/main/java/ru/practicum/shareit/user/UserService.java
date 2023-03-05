package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User changeUser(long id, User user);

    List<User> getAllUsers();

    User findUserById(long id);

    void deleteUser(long id);

    void checkUser(Long userId);
}

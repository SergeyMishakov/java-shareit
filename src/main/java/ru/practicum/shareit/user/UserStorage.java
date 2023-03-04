package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User changeUser(User user);

    List<User> getAllUsers();

    User findUserById(long id);

    void deleteUser(long id);

    boolean checkUniqueEmail(Long id, String email);
}

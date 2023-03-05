package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> userList = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Override
    public User createUser(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUser(User user) {
        User userToUpdate = userList.get(user.getId());
        if (userToUpdate == null) {
            LOG.warn("Пользователь не найден");
            throw new NotFoundException();
        }
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        userList.put(userToUpdate.getId(), userToUpdate);
        return userToUpdate;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(userList.get(id));
    }

    @Override
    public void deleteUser(long id) {
        userList.remove(id);
    }

    @Override
    public boolean checkUniqueEmail(Long id, String email) {
        for (User user : userList.values()) {
            if (user.getEmail().equals(email) && user.getId() != id) {
                return false;
            }
        }
        return true;
    }
}

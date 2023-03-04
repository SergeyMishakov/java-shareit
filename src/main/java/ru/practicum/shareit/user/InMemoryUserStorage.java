package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> userList = new HashMap<>();

    @Override
    public User createUser(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUser(User user) {
        User userToUpdate = userList.get(user.getId());
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
    public User findUserById(long id) {
        return userList.get(id);
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

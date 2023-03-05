package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserValidator userValidator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private long idIncrement = 0;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserValidator userValidator) {
        this.userStorage = userStorage;
        this.userValidator = userValidator;
    }

    @Override
    public User createUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        //проверить уникальность эмейла
        if (!userStorage.checkUniqueEmail(user.getId(), user.getEmail())) {
            LOG.warn("Нарушение уникальности эмейла");
            throw new RuntimeException();
        }
        idIncrement++;
        user.setId(idIncrement);
        return userStorage.createUser(user);
    }

    @Override
    public User changeUser(long id, User user) {
        user.setId(id);
        if (user.getEmail() != null) {
            if (!userStorage.checkUniqueEmail(id, user.getEmail())) {
                LOG.warn("Нарушение уникальности эмейла");
                throw new RuntimeException();
            }
        }
        return userStorage.changeUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User findUserById(long id) {
        if (userStorage.findUserById(id).isEmpty()) {
            LOG.warn("Пользователь не найден");
            throw new NotFoundException();
        }
        return userStorage.findUserById(id).get();
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public void checkUser(Long userId) {
        if (userId == null) {
            LOG.warn("Владелец не указан. Проверка не пройдена");
            throw new ValidationException();
        }
        if (userStorage.findUserById(userId).isEmpty()) {
            LOG.warn("Владелец не найден. Проверка не пройдена");
            throw new NotFoundException();
        }
    }
}

package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public User createUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        return userRepository.save(user);
    }

    @Override
    public User changeUser(long id, User user) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            LOG.warn("Пользователь не найден");
            throw new NotFoundException();
        }
        User changedUser = optUser.get();
        if (user.getName() != null) {
            changedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            changedUser.setEmail(user.getEmail());
        }
        return userRepository.save(changedUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            throw new NotFoundException();
        }
        return optUser.get();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void checkUser(Long userId) {
        if (userId == null) {
            LOG.warn("Владелец не указан. Проверка не пройдена");
            throw new ValidationException();
        }
        if (userRepository.findById(userId).isEmpty()) {
            LOG.warn("Владелец не найден. Проверка не пройдена");
            throw new NotFoundException();
        }
    }
}

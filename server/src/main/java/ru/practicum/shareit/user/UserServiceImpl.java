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
        User changedUser = userRepository.findById(id).orElseThrow(NotFoundException::new);
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
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public boolean deleteUser(long id) {
        userRepository.deleteById(id);
        LOG.info("Пользователь {} удален", id);
        return true;
    }

    @Override
    public void checkUser(Long userId) {
        if (userId == null) {
            LOG.warn("Владелец не указан. Проверка не пройдена");
            throw new ValidationException();
        }
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
    }
}

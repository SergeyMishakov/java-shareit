package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest {

    @Test
    void createUser() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("email@gmail.com");
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setName("TestName");
        resultUser.setEmail("email@gmail.com");
        UserValidator userValidator = new UserValidator();
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.save(user))
                .thenReturn(resultUser);
        User checkUser = new User();
        checkUser.setId(1);
        checkUser.setName("TestName");
        checkUser.setEmail("email@gmail.com");
        User u = userService.createUser(user);
        Assertions.assertEquals(checkUser, u);
    }

    @Test
    void changeUser() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setName("updateTestName");
        resultUser.setEmail("updateEmail@gmail.com");
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setName("TestName");
        oldUser.setEmail("email@gmail.com");
        UserValidator userValidator = new UserValidator();
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.save(resultUser))
                .thenReturn(resultUser);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(oldUser));
        User u = userService.changeUser(1, resultUser);
        Assertions.assertEquals(resultUser, u);
    }

    @Test
    void checkUser() {
        User oldUser = new User();
        oldUser.setId(99);
        oldUser.setName("TestName");
        oldUser.setEmail("email@gmail.com");
        UserValidator userValidator = new UserValidator();
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.findById(99L))
                .thenReturn(Optional.empty());
        userService.checkUser(1L);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.checkUser(99L));
        Assertions.assertEquals(new NotFoundException(), exception);
    }
}
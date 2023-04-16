package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import java.util.Optional;

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
    void createUserValidationException() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("");
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
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userService.createUser(user));
        Assertions.assertEquals(null, exception.getMessage());
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
    void findUserById() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setName("updateTestName");
        resultUser.setEmail("updateEmail@gmail.com");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenThrow(new NotFoundException());
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findUserById(1L));
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Test
    void checkUser() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setName("updateTestName");
        resultUser.setEmail("updateEmail@gmail.com");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenThrow(new NotFoundException());
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.checkUser(1L));
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Test
    void checkEmptyUser() {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setName("updateTestName");
        resultUser.setEmail("updateEmail@gmail.com");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserValidator userValidator = new UserValidator();
        UserService userService = new UserServiceImpl(mockUserRepository, userValidator);
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenThrow(new NotFoundException());
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userService.checkUser(null));
        Assertions.assertEquals(null, exception.getMessage());
    }
}
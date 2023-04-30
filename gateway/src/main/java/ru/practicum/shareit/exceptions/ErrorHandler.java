package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handle(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad Request {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return new ValidationException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handle(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad Request {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return new ValidationException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RuntimeException handle(final RuntimeException e) {
        log.debug("Получен статус 500 Internal Server Error {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return new RuntimeException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handle(final ValidationException e) {
        log.debug("Получен статус 400 Bad Request {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return new ValidationException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handle(final IllegalStateException e) {
        log.debug("Получен статус 500 Internal Server Error {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handle(final IllegalArgumentException e) {
        log.debug("Получен статус 500 Internal Server Error {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RuntimeException handle(final Exception e) {
        log.debug("Получен статус 500 Internal Server Error {},\n stackTrace {}", e.getMessage(), e.getStackTrace(), e);
        return new RuntimeException(e.getMessage());
    }
}

package ru.practicum.shareit.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        this.message = message;
    }

    String message;
    public ValidationException() {
        }
}

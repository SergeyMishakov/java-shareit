package ru.practicum.shareit.exceptions;

public class BadRequest {


    public BadRequest(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    private String error;

}

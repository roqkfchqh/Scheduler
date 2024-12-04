package com.schedule.controller.common;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}

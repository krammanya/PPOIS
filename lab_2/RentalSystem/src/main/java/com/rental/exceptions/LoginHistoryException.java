package com.rental.exceptions;

public class LoginHistoryException extends RuntimeException {
    public LoginHistoryException(String message) {
        super(message);
    }

    public LoginHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.system.exceptions;

public class PropertyNotAvailableException extends Exception {
    public PropertyNotAvailableException(String message) {
        super(message);
    }

    public PropertyNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
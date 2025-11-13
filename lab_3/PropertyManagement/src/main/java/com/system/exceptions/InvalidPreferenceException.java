package com.system.exceptions;

public class InvalidPreferenceException extends RuntimeException {
    public InvalidPreferenceException(String message) {
        super("Некорректные предпочтения: " + message);
    }
}
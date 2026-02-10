package com.system.exceptions;

public class AvailabilityConflictException extends RuntimeException {
    public AvailabilityConflictException(String message) {
        super("Конфликт доступности: " + message);
    }
}
package com.rental.exceptions;

public class RentalHistoryException extends RuntimeException {
    public RentalHistoryException(String message) {
        super(message);
    }

    public RentalHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
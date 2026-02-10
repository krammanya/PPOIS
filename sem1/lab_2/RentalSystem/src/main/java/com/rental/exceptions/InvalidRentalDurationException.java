package com.rental.exceptions;

public class InvalidRentalDurationException extends RuntimeException {
    public InvalidRentalDurationException(int days) {
        super("Cannot set rental duration to " + days + " days. Rental must be for at least 1 day");
    }

    public InvalidRentalDurationException(String message) {
        super(message);
    }
}

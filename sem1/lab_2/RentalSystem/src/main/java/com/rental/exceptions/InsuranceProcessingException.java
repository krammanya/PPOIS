package com.rental.exceptions;

public class InsuranceProcessingException extends RuntimeException {
    public InsuranceProcessingException(String message) {
        super(" Insurance Processing Error: " + message);
    }

    public InsuranceProcessingException(String message, Throwable cause) {
        super(" Insurance Processing Error: " + message, cause);
    }
}
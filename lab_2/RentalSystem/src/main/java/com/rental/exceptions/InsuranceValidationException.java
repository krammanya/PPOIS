package com.rental.exceptions;

public class InsuranceValidationException extends RuntimeException {
    public InsuranceValidationException(String message) { super(message); }
    public InsuranceValidationException(String message, Throwable cause) { super(message, cause); }
}
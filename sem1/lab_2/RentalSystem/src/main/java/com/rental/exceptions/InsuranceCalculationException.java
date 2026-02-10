package com.rental.exceptions;

public class InsuranceCalculationException extends RuntimeException {
    public InsuranceCalculationException(String message) { super(message); }
    public InsuranceCalculationException(String message, Throwable cause) { super(message, cause); }
}
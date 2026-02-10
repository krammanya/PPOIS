package com.system.exceptions;

public class PropertySaleException extends RuntimeException {

    public PropertySaleException(String message) {
        super(message);
    }

    public PropertySaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
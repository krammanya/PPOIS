package com.system.exceptions;

public class NoPropertyMatchException extends RuntimeException {
    public NoPropertyMatchException(String tenantName, int propertiesChecked) {
        super("Не найдено подходящей недвижимости для " + tenantName +
                ". Проверено: " + propertiesChecked + " объектов");
    }
}
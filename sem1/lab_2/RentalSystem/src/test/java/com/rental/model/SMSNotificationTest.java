package com.rental.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SMSNotificationTest {

    @Test
    void smsNotification_shouldHandleAllScenarios() {
        SMSNotification notification = new SMSNotification();
        Customer customer = new Customer("Ivan", "Ivanov", "+79991234567");

        String validMessage = "Ваша аренда подтверждена";
        notification.send(customer, validMessage);

        assertEquals(validMessage, notification.getLastSentMessage());
        assertTrue(notification.isMessageValid(validMessage));

        String longMessage = "Очень длинное сообщение которое должно быть обрезано потому что превышает максимальную длину в 160 символов и должно быть укорочено до допустимого размера для SMS сообщения";
        notification.send(customer, longMessage);

        assertEquals(160, notification.getLastSentMessage().length());
        assertFalse(notification.isMessageValid(longMessage));

        assertFalse(notification.isMessageValid(null));
        assertTrue(notification.isMessageValid(""));
        assertTrue(notification.isMessageValid("Короткое"));
    }
}
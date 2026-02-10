package com.rental.model;

import com.rental.interfaces.Notification;
import com.rental.model.Customer;

public class SMSNotification implements Notification{
    private String provider = "DEFAULT_SMS_PROVIDER";
    private int maxMessageLength = 160;
    private String lastSentMessage;

    @Override
    public void send(Customer customer, String message) {
        if (message.length() > maxMessageLength) {
            message = message.substring(0, maxMessageLength);
        }
        System.out.println("Отправляем SMS через " + provider + " на " + customer.getPhoneNumber() + ": " + message);
        this.lastSentMessage = message;
    }

    public boolean isMessageValid(String message) {
        return message != null && message.length() <= maxMessageLength;
    }

    public String getLastSentMessage() {
        return lastSentMessage;
    }
}

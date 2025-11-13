package com.system.interfaces;

public interface Notifier {
    void sendNotification(String recipient, String message);
    String getNotifierType();
    boolean supportsUrgentNotifications();
}
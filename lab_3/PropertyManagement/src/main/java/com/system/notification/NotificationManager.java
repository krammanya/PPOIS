package com.system.notification;

import java.util.*;

public class NotificationManager {
    private List<Notification> notifications;

    public NotificationManager() {
        this.notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getUnreadForRecipient(String recipient) {
        return notifications.stream()
                .filter(n -> n.getRecipient().equals(recipient) && !n.isRead())
                .toList();
    }

    public void markAllAsRead(String recipient) {
        notifications.stream()
                .filter(n -> n.getRecipient().equals(recipient))
                .forEach(Notification::markAsRead);
    }

    public int getUnreadCount(String recipient) {
        return (int) notifications.stream()
                .filter(n -> n.getRecipient().equals(recipient) && !n.isRead())
                .count();
    }
}
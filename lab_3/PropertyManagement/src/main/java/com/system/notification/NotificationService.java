package com.system.notification;

import java.util.*;
import com.system.interfaces.Notifier;

public class NotificationService {
    private List<Notifier> notifiers;
    private NotificationManager notificationManager;

    public NotificationService(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.notifiers = new ArrayList<>();
    }

    public void registerNotifier(Notifier notifier) {
        notifiers.add(notifier);
    }

    public void sendUrgentNotification(String recipient, String message) {
        notifiers.stream()
                .filter(Notifier::supportsUrgentNotifications)
                .forEach(notifier -> notifier.sendNotification(recipient, message));
    }

    public void broadcastNotification(String message, List<String> recipients) {
        recipients.parallelStream().forEach(recipient ->
                notifiers.forEach(notifier ->
                        notifier.sendNotification(recipient, message)
                )
        );
    }

    public List<String> getNotifierTypes() {
        return notifiers.stream()
                .map(Notifier::getNotifierType)
                .toList();
    }

    public void disableNotifier(String notifierType) {
        notifiers.removeIf(notifier -> notifier.getNotifierType().equals(notifierType));
    }
}
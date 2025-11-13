package com.system.notification;

import com.system.news.*;
import java.util.List;
import com.system.interfaces.Notifier;

public class PublicationNotifier implements Notifier {
    private NotificationManager notificationManager;

    public PublicationNotifier(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public void sendNotification(String recipient, String message) {
        try {
            Notification notification = new Notification(recipient, message);
            notificationManager.addNotification(notification);
        } catch (Exception e) {
            System.err.println("Ошибка отправки уведомления для " + recipient + ": " + e.getMessage());
        }
    }

    @Override
    public String getNotifierType() {
        return "PUBLICATION_NOTIFIER";
    }

    @Override
    public boolean supportsUrgentNotifications() {
        return false;
    }

    public void notifyNewPublication(Publication publication, List<String> subscribers) {
        String message = String.format(
                "Опубликована новая %s: '%s' от %s",
                publication.getType().toLowerCase(),
                publication.getTitle(),
                publication.getAuthor().getFullName()
        );

        for (String subscriber : subscribers) {
            sendNotification(subscriber, message);
        }
    }

    public void notifyAuthorAboutTrending(Author author, String tag) {
        String message = String.format(
                "Тема '%s' сейчас популярна! Можете написать новую публикацию",
                tag
        );
        sendNotification(author.getEmail(), message);
    }
}
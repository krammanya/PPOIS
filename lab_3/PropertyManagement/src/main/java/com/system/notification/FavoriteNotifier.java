package com.system.notification;

import com.system.property.Property;
import com.system.users.Liked;
import com.system.interfaces.Notifier;

public class FavoriteNotifier implements Notifier{
    private NotificationManager notificationManager;

    public FavoriteNotifier(NotificationManager notificationManager) {
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
        return "FAVORITE_NOTIFIER";
    }

    @Override
    public boolean supportsUrgentNotifications() {
        return true;
    }

    public void notifyPriceDrop(Liked liked, Property property, double oldPrice, double newPrice) {
        String message = String.format(
                "Цена на '%s' из вашего избранного снизилась! Было: %.2f, стало: %.2f",
                property.getName(), oldPrice, newPrice
        );
        sendNotification(liked.getTenant().getEmail(), message);
    }

    public void notifyNewSimilar(Liked liked, Property similarProperty) {
        String message = String.format(
                "Найден похожий объект на '%s' из вашего избранного: %s",
                liked.getProperties().get(0).getName(), similarProperty.getName()
        );
        sendNotification(liked.getTenant().getEmail(), message);
    }
}

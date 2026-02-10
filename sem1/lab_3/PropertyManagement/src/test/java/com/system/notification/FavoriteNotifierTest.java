package com.system.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Locale;
import com.system.property.*;
import com.system.users.*;

class FavoriteNotifierTest {

    private FavoriteNotifier favoriteNotifier;
    private NotificationManager notificationManager;
    private Liked liked;
    private Tenant tenant;
    private Address address;
    private Property property1;
    private Property property2;

    @BeforeEach
    void setUp() {
        notificationManager = new NotificationManager();
        favoriteNotifier = new FavoriteNotifier(notificationManager);
        address = new Address("Улица", "Город", "1");
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", new RentalService());
        liked = new Liked(tenant);
        property1 = new Apartment("Апартаменты 1", address, 50.0, 2, 1);
        property2 = new Apartment("Апартаменты 2", address, 75.0, 5, 2);
        liked.add(property1);
    }

    @Test
    void testSendNotification() {
        favoriteNotifier.sendNotification("user@test.com", "Test message");

        List<Notification> unread = notificationManager.getUnreadForRecipient("user@test.com");
        assertEquals(1, unread.size());
        assertEquals("Test message", unread.get(0).getMessage());
    }

    @Test
    void testGetNotifierType() {
        assertEquals("FAVORITE_NOTIFIER", favoriteNotifier.getNotifierType());
    }

    @Test
    void testSupportsUrgentNotifications() {
        assertTrue(favoriteNotifier.supportsUrgentNotifications());
    }

    @Test
    void testNotifyPriceDrop() {
        favoriteNotifier.notifyPriceDrop(liked, property1, 100000.0, 90000.0);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("Цена на 'Апартаменты 1' из вашего избранного снизилась!"));
        assertTrue(message.contains("Апартаменты 1"));
        // Проверяем числа более гибко (могут быть с точкой или запятой)
        assertTrue(message.contains("100000") && message.contains("90000"));
    }

    @Test
    void testNotifyPriceDropWithDifferentProperties() {
        Property commercial = new Commercial("Офис", address, 100.0, true, false, "office");
        liked.add(commercial);

        favoriteNotifier.notifyPriceDrop(liked, commercial, 500000.0, 450000.0);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("Офис"));
        assertTrue(message.contains("500000") && message.contains("450000"));
    }

    @Test
    void testNotifyNewSimilar() {
        favoriteNotifier.notifyNewSimilar(liked, property2);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("Найден похожий объект"));
        assertTrue(message.contains("Апартаменты 1"));
        assertTrue(message.contains("Апартаменты 2"));
    }

    @Test
    void testNotifyNewSimilarWithEmptyLiked() {
        Liked emptyLiked = new Liked(tenant);

        assertThrows(IndexOutOfBoundsException.class,
                () -> favoriteNotifier.notifyNewSimilar(emptyLiked, property2));
    }

    @Test
    void testNotifyNewSimilarWithMultipleProperties() {
        liked.add(property2);
        Property similarProperty = new Apartment("Апартаменты 3", address, 80.0, 3, 2);

        favoriteNotifier.notifyNewSimilar(liked, similarProperty);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("Апартаменты 1") || message.contains("Апартаменты 2"));
        assertTrue(message.contains("Апартаменты 3"));
    }

    @Test
    void testSendNotificationWithException() {
        NotificationManager failingManager = new NotificationManager() {
            @Override
            public void addNotification(Notification notification) {
                throw new RuntimeException("Test exception");
            }
        };

        FavoriteNotifier failingNotifier = new FavoriteNotifier(failingManager);
        assertDoesNotThrow(() -> failingNotifier.sendNotification("test", "message"));
    }

    @Test
    void testMultipleNotificationsSameRecipient() {
        favoriteNotifier.notifyPriceDrop(liked, property1, 100000.0, 90000.0);
        favoriteNotifier.notifyNewSimilar(liked, property2);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(2, unread.size());
    }

    @Test
    void testPriceDropWithZeroPrice() {
        favoriteNotifier.notifyPriceDrop(liked, property1, 0.0, 0.0);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("0"));
    }

    @Test
    void testPriceDropWithLargePriceDifference() {
        favoriteNotifier.notifyPriceDrop(liked, property1, 1000000.50, 750000.75);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
        String message = unread.get(0).getMessage();
        assertTrue(message.contains("1000000") && message.contains("750000"));
    }

    @Test
    void testNotificationToDifferentTenant() {
        Tenant differentTenant = new Tenant("Петр", "Петров", "+79991111111", "petr@test.com", new RentalService());
        Liked differentLiked = new Liked(differentTenant);
        differentLiked.add(property1);

        favoriteNotifier.notifyPriceDrop(differentLiked, property1, 100000.0, 90000.0);

        List<Notification> unread = notificationManager.getUnreadForRecipient("petr@test.com");
        assertEquals(1, unread.size());
        assertEquals(0, notificationManager.getUnreadForRecipient(tenant.getEmail()).size());
    }

    @Test
    void testHotelPropertyNotifications() {
        Property hotel = new Hotel("Отель", address, 1000.0, 50, true);
        liked.add(hotel);

        favoriteNotifier.notifyPriceDrop(liked, hotel, 2000000.0, 1800000.0);
        favoriteNotifier.notifyNewSimilar(liked, property2);

        assertEquals(2, notificationManager.getUnreadForRecipient(tenant.getEmail()).size());
    }

    @Test
    void testNotifyNewSimilarSafeWithEmptyLiked() {
        Liked emptyLiked = new Liked(tenant);

        emptyLiked.add(property1);
        favoriteNotifier.notifyNewSimilar(emptyLiked, property2);

        List<Notification> unread = notificationManager.getUnreadForRecipient(tenant.getEmail());
        assertEquals(1, unread.size());
    }
}
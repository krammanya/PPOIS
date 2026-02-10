package com.system.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class PublicationNotifierTest {

    private PublicationNotifier publicationNotifier;
    private NotificationManager notificationManager;

    @BeforeEach
    void setUp() {
        notificationManager = new NotificationManager();
        publicationNotifier = new PublicationNotifier(notificationManager);
    }

    @Test
    void testSendNotification() {
        publicationNotifier.sendNotification("user@test.com", "Test message");

        List<Notification> unread = notificationManager.getUnreadForRecipient("user@test.com");
        assertEquals(1, unread.size());
        assertEquals("Test message", unread.get(0).getMessage());
    }

    @Test
    void testGetNotifierType() {
        assertEquals("PUBLICATION_NOTIFIER", publicationNotifier.getNotifierType());
    }

    @Test
    void testSupportsUrgentNotifications() {
        assertFalse(publicationNotifier.supportsUrgentNotifications());
    }

    @Test
    void testSendNotificationWithException() {
        NotificationManager failingManager = new NotificationManager() {
            @Override
            public void addNotification(Notification notification) {
                throw new RuntimeException("Test exception");
            }
        };

        PublicationNotifier failingNotifier = new PublicationNotifier(failingManager);
        assertDoesNotThrow(() -> failingNotifier.sendNotification("test", "message"));
    }
}
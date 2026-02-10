package com.system.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import com.system.interfaces.Notifier;

class NotificationServiceTest {

    private NotificationService notificationService;
    private NotificationManager notificationManager;
    private TestNotifier emailNotifier;
    private TestNotifier smsNotifier;

    @BeforeEach
    void setUp() {
        notificationManager = new NotificationManager();
        notificationService = new NotificationService(notificationManager);
        emailNotifier = new TestNotifier("EMAIL", true);
        smsNotifier = new TestNotifier("SMS", false);
    }

    @Test
    void testRegisterNotifier() {
        notificationService.registerNotifier(emailNotifier);
        assertEquals(List.of("EMAIL"), notificationService.getNotifierTypes());
    }

    @Test
    void testSendUrgentNotification() {
        notificationService.registerNotifier(emailNotifier);
        notificationService.registerNotifier(smsNotifier);

        notificationService.sendUrgentNotification("user@test.com", "Urgent message");

        assertEquals(1, emailNotifier.getNotificationCount());
        assertEquals(0, smsNotifier.getNotificationCount());
        assertEquals("user@test.com", emailNotifier.getLastRecipient());
        assertEquals("Urgent message", emailNotifier.getLastMessage());
    }

    @Test
    void testBroadcastNotification() {
        notificationService.registerNotifier(emailNotifier);
        notificationService.registerNotifier(smsNotifier);

        List<String> recipients = List.of("user1@test.com", "user2@test.com");
        notificationService.broadcastNotification("Broadcast message", recipients);

        assertEquals(2, emailNotifier.getNotificationCount());
        assertEquals(2, smsNotifier.getNotificationCount());
    }

    @Test
    void testGetNotifierTypes() {
        notificationService.registerNotifier(emailNotifier);
        notificationService.registerNotifier(smsNotifier);

        List<String> types = notificationService.getNotifierTypes();

        assertEquals(2, types.size());
        assertTrue(types.contains("EMAIL"));
        assertTrue(types.contains("SMS"));
    }

    @Test
    void testDisableNotifier() {
        notificationService.registerNotifier(emailNotifier);
        notificationService.registerNotifier(smsNotifier);

        notificationService.disableNotifier("EMAIL");

        assertEquals(List.of("SMS"), notificationService.getNotifierTypes());
    }

    @Test
    void testEmptyNotifiers() {
        assertTrue(notificationService.getNotifierTypes().isEmpty());

        notificationService.sendUrgentNotification("test", "message");
        notificationService.broadcastNotification("message", List.of("recipient"));

        assertDoesNotThrow(() -> notificationService.disableNotifier("NON_EXISTENT"));
    }

    private static class TestNotifier implements Notifier {
        private String type;
        private boolean supportsUrgent;
        private int notificationCount;
        private String lastRecipient;
        private String lastMessage;

        public TestNotifier(String type, boolean supportsUrgent) {
            this.type = type;
            this.supportsUrgent = supportsUrgent;
        }

        @Override
        public void sendNotification(String recipient, String message) {
            notificationCount++;
            lastRecipient = recipient;
            lastMessage = message;
        }

        @Override
        public boolean supportsUrgentNotifications() {
            return supportsUrgent;
        }

        @Override
        public String getNotifierType() {
            return type;
        }

        public int getNotificationCount() { return notificationCount; }
        public String getLastRecipient() { return lastRecipient; }
        public String getLastMessage() { return lastMessage; }
    }
}

class NotificationManagerTest {

    private NotificationManager notificationManager;

    @BeforeEach
    void setUp() {
        notificationManager = new NotificationManager();
    }

    @Test
    void testAddNotification() {
        Notification notification = new Notification("recipient", "message");
        notificationManager.addNotification(notification);
        assertEquals(1, notificationManager.getUnreadCount("recipient"));
    }

    @Test
    void testGetUnreadForRecipient() {
        Notification n1 = new Notification("user1", "message1");
        Notification n2 = new Notification("user1", "message2");
        Notification n3 = new Notification("user2", "message3");

        notificationManager.addNotification(n1);
        notificationManager.addNotification(n2);
        notificationManager.addNotification(n3);

        List<Notification> unread = notificationManager.getUnreadForRecipient("user1");
        assertEquals(2, unread.size());
    }

    @Test
    void testMarkAllAsRead() {
        Notification n1 = new Notification("user1", "message1");
        Notification n2 = new Notification("user1", "message2");

        notificationManager.addNotification(n1);
        notificationManager.addNotification(n2);

        notificationManager.markAllAsRead("user1");

        assertEquals(0, notificationManager.getUnreadCount("user1"));
    }

    @Test
    void testGetUnreadCount() {
        Notification n1 = new Notification("user1", "message1");
        Notification n2 = new Notification("user1", "message2");
        n2.markAsRead();

        notificationManager.addNotification(n1);
        notificationManager.addNotification(n2);

        assertEquals(1, notificationManager.getUnreadCount("user1"));
    }

    @Test
    void testNoUnreadForRecipient() {
        assertEquals(0, notificationManager.getUnreadCount("nonexistent"));
        assertTrue(notificationManager.getUnreadForRecipient("nonexistent").isEmpty());
    }
}
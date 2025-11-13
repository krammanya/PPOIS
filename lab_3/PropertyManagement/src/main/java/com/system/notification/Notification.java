package com.system.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private String id;
    private String recipient;
    private String message;
    private LocalDateTime createdDate;
    private boolean isRead;

    public Notification(String recipient, String message) {
        this.id = "NOTIF_" + UUID.randomUUID();
        this.recipient = recipient;
        this.message = message;
        this.createdDate = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public String getId() { return id; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public boolean isRead() { return isRead; }

}
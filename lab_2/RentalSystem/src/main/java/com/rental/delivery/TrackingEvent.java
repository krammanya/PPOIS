package com.rental.delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackingEvent {
    private String description;
    private String updatedBy;
    private LocalDateTime timestamp;

    public TrackingEvent(String description, String updatedBy, LocalDateTime timestamp) {
        this.description = description;
        this.updatedBy = updatedBy;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s (обновлено: %s)",
                timestamp.format(formatter), description, updatedBy);
    }
}
package com.system.property;

import com.system.interfaces.Availability;

public class IsAvailable implements Availability {
    private boolean available;
    private String reason;

    public IsAvailable(boolean available) {
        this.available = available;
    }

    public IsAvailable(boolean available, String reason) {
        this.available = available;
        this.reason = reason;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
        if (available) this.reason = null;
    }

    @Override
    public String getAvailabilityReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        if (available) {
            return "Доступно для аренды";
        } else if (reason != null) {
            return "Недоступно: " + reason;
        }
        return "Недоступно";
    }
}
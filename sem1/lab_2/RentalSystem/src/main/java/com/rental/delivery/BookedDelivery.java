package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;
import java.time.LocalDateTime;

public class BookedDelivery extends Delivery {
    private LocalDateTime scheduledDate;
    private boolean isScheduled;

    public BookedDelivery(DeliveryOrder order, DeliveryAddress deliveryAddress, DeliveryRecipient recipient, LocalDateTime scheduledDate) {
        super(order, deliveryAddress, recipient);
        if (scheduledDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new DeliveryException("Доставка должна быть запланирована минимум за 1 час");
        }
        this.scheduledDate = scheduledDate;
        this.isScheduled = true;
        setEstimatedDeliveryDate(scheduledDate);
    }

    public BookedDelivery(DeliveryOrder order, DeliveryAddress deliveryAddress, DeliveryRecipient recipient) {
        super(order, deliveryAddress, recipient);
        this.isScheduled = false;
    }

    public void scheduleDelivery(LocalDateTime scheduledDate) {
        if (scheduledDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new DeliveryException("Доставка должна быть запланирована минимум за 1 час");
        }
        this.scheduledDate = scheduledDate;
        this.isScheduled = true;
        setEstimatedDeliveryDate(scheduledDate);
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    @Override
    public boolean isOverdue() {
        if (isScheduled) {
            return !isDelivered() && LocalDateTime.now().isAfter(scheduledDate);
        }
        return super.isOverdue();
    }

    public boolean isScheduledDatePassed() {
        return isScheduled && LocalDateTime.now().isAfter(scheduledDate);
    }
}
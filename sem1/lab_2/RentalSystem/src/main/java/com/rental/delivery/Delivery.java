package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;

import java.time.LocalDateTime;

public class Delivery {
    private DeliveryOrder order;
    private DeliveryAddress deliveryAddress;
    private DeliveryRecipient recipient;
    private LocalDateTime createdDate;
    private LocalDateTime estimatedDeliveryDate;
    private boolean delivered;
    private LocalDateTime actualDeliveryDate;

    public Delivery(DeliveryOrder order, DeliveryAddress deliveryAddress, DeliveryRecipient recipient) {
        if (order == null || deliveryAddress == null || recipient == null) {
            throw new DeliveryException("Все параметры доставки должны быть указаны");
        }
        this.order = order;
        this.deliveryAddress = deliveryAddress;
        this.recipient = recipient;
        this.createdDate = LocalDateTime.now();
        this.delivered = false;
        calculateEstimatedDelivery();
    }

    public DeliveryOrder getOrder() { return order; }
    public DeliveryAddress getDeliveryAddress() { return deliveryAddress; }
    public DeliveryRecipient getRecipient() { return recipient; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public boolean isDelivered() { return delivered; }
    public LocalDateTime getActualDeliveryDate() { return actualDeliveryDate; }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        if (deliveryAddress == null) {
            throw new DeliveryException("Адрес доставки не может быть null");
        }
        this.deliveryAddress = deliveryAddress;
    }

    public void setRecipient(DeliveryRecipient recipient) {
        if (recipient == null) {
            throw new DeliveryException("Получатель не может быть null");
        }
        this.recipient = recipient;
    }

    public void markAsDelivered() {
        if (delivered) {
            throw new DeliveryException("Доставка уже была отмечена как завершенная");
        }
        this.delivered = true;
        this.actualDeliveryDate = LocalDateTime.now();
    }

    private void calculateEstimatedDelivery() {
        this.estimatedDeliveryDate = createdDate.plusDays(2);
    }

    protected void setEstimatedDeliveryDate(LocalDateTime date) {
        if (date.isBefore(createdDate)) {
            throw new DeliveryException("Дата доставки не может быть раньше даты создания");
        }
        this.estimatedDeliveryDate = date;
    }

    public boolean isOverdue() {
        return !delivered && LocalDateTime.now().isAfter(estimatedDeliveryDate);
    }
}
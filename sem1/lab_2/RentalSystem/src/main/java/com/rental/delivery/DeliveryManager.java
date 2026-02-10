package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DeliveryManager {
    private Map<String, Delivery> deliveries;
    private Map<String, Tracking> trackingMap;

    public DeliveryManager() {
        this.deliveries = new HashMap<>();
        this.trackingMap = new HashMap<>();
    }

    public Delivery createDelivery(DeliveryOrder order, DeliveryAddress deliveryAddress, DeliveryRecipient recipient) {
        Delivery delivery = new Delivery(order, deliveryAddress, recipient);
        String deliveryId = "DELIVERY-" + System.currentTimeMillis();
        deliveries.put(deliveryId, delivery);

        Tracking tracking = new Tracking(delivery);
        trackingMap.put(deliveryId, tracking);

        return delivery;
    }

    public BookedDelivery createBookedDelivery(DeliveryOrder order, DeliveryAddress deliveryAddress, DeliveryRecipient recipient, LocalDateTime scheduledDate) {
        BookedDelivery delivery = new BookedDelivery(order, deliveryAddress, recipient, scheduledDate);
        String deliveryId = "BOOKED-" + System.currentTimeMillis();
        deliveries.put(deliveryId, delivery);

        Tracking tracking = new Tracking(delivery);
        trackingMap.put(deliveryId, tracking);

        return delivery;
    }

    public Tracking getTracking(String deliveryId) {
        return trackingMap.get(deliveryId);
    }

    public void markDeliveryAsDelivered(String deliveryId, String deliveredBy) {
        Tracking tracking = trackingMap.get(deliveryId);
        if (tracking != null) {
            tracking.markAsDelivered(deliveredBy);
        } else {
            throw new DeliveryException("Delivery with ID " + deliveryId + " not found");
        }
    }

    public boolean isDeliveryOverdue(String deliveryId) {
        Delivery delivery = deliveries.get(deliveryId);
        return delivery != null && delivery.isOverdue();
    }

    public void addTrackingEvent(String deliveryId, String description, String updatedBy) {
        Tracking tracking = trackingMap.get(deliveryId);
        if (tracking != null) {
            tracking.addEvent(description, updatedBy);
        } else {
            throw new DeliveryException("Delivery with ID " + deliveryId + " not found");
        }
    }

    public Map<String, Delivery> getDeliveries() {
        return new HashMap<>(deliveries);
    }

    public Map<String, Tracking> getTrackingMap() {
        return new HashMap<>(trackingMap);
    }
}

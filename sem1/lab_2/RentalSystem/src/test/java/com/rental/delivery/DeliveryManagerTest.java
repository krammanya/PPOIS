package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryManagerTest {

    private DeliveryManager manager;
    private DeliveryOrder order;
    private DeliveryAddress address;
    private DeliveryRecipient recipient;

    @BeforeEach
    void setUp() {
        manager = new DeliveryManager();
        order = new com.rental.bank.Order(new com.rental.model.Customer("Ivan", "Ivanov", "+1234567890"));
        address = new com.rental.model.Address("Gikalo", "Minsk", "1", "123");
        recipient = new com.rental.model.Customer("Ivan", "Ivanov", "+1234567890");
    }

    @Test
    void deliveryManager_shouldHandleAllDeliveryOperations() {
        Delivery delivery = manager.createDelivery(order, address, recipient);
        assertNotNull(delivery);

        BookedDelivery bookedDelivery = manager.createBookedDelivery(order, address, recipient, LocalDateTime.now().plusDays(1));
        assertNotNull(bookedDelivery);

        Map<String, Delivery> deliveries = manager.getDeliveries();
        assertEquals(2, deliveries.size());

        Map<String, Tracking> trackingMap = manager.getTrackingMap();
        assertEquals(2, trackingMap.size());

        String deliveryId = deliveries.keySet().iterator().next();
        Tracking tracking = manager.getTracking(deliveryId);
        assertNotNull(tracking);

        manager.addTrackingEvent(deliveryId, "Package picked up", "driver1");

        manager.markDeliveryAsDelivered(deliveryId, "driver1");

        assertFalse(manager.isDeliveryOverdue(deliveryId));
    }

    @Test
    void deliveryManager_shouldHandleNonExistentDeliveries() {

        assertNull(manager.getTracking("NON_EXISTENT"));

        assertThrows(DeliveryException.class, () -> manager.markDeliveryAsDelivered("NON_EXISTENT", "driver"));

        assertThrows(DeliveryException.class, () -> manager.addTrackingEvent("NON_EXISTENT", "event", "user"));

        assertFalse(manager.isDeliveryOverdue("NON_EXISTENT"));
    }

    @Test
    void deliveryManager_shouldReturnDefensiveCopies() {
        manager.createDelivery(order, address, recipient);

        Map<String, Delivery> deliveries = manager.getDeliveries();
        Map<String, Tracking> trackingMap = manager.getTrackingMap();

        deliveries.clear();
        trackingMap.clear();

        assertEquals(1, manager.getDeliveries().size());
        assertEquals(1, manager.getTrackingMap().size());
    }
}
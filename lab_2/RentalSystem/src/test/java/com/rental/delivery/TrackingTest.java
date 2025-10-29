package com.rental.delivery;

import com.rental.exceptions.DeliveryException;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryRecipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TrackingTest {

    private Delivery delivery;
    private Tracking tracking;

    @BeforeEach
    void setUp() {
        DeliveryOrder order = new com.rental.bank.Order(new com.rental.model.Customer("John", "Doe", "+1234567890"));
        DeliveryAddress address = new com.rental.model.Address("Moscow", "Tverskaya", "1", "123");
        DeliveryRecipient recipient = new com.rental.model.Customer("John", "Doe", "+1234567890");

        delivery = new Delivery(order, address, recipient);
        tracking = new Tracking(delivery);
    }

    @Test
    void tracking_shouldHandleAllTrackingOperations() {

        assertEquals(delivery, tracking.getDelivery());
        assertNotNull(tracking.getLastUpdated());

        List<TrackingEvent> events = tracking.getEvents();
        assertEquals(1, events.size());
        assertEquals("Доставка создана", events.get(0).getDescription());

        tracking.addEvent("Посылка принята в сортировочном центре", "operator1");
        tracking.addEvent("Посылка в пути"); // без указания пользователя

        assertEquals(3, tracking.getEvents().size());
        assertNotNull(tracking.getLastUpdated());

        events.clear();
        assertEquals(3, tracking.getEvents().size());

        tracking.markAsDelivered("courier1");
        assertTrue(delivery.isDelivered());
        assertEquals(4, tracking.getEvents().size());
    }

    @Test
    void tracking_shouldThrowExceptionsForInvalidOperations() {

        assertThrows(DeliveryException.class, () -> new Tracking(null));

        assertThrows(DeliveryException.class, () -> tracking.addEvent("", "user"));
        assertThrows(DeliveryException.class, () -> tracking.addEvent(null, "user"));
        assertThrows(DeliveryException.class, () -> tracking.addEvent("   ", "user"));

        tracking.markAsDelivered("courier1");
        assertThrows(DeliveryException.class, () -> tracking.markAsDelivered("courier2"));
    }

    @Test
    void tracking_shouldPrintHistoryWithoutErrors() {

        assertDoesNotThrow(() -> tracking.printTrackingHistory());

        tracking.addEvent("Тестовое событие", "testuser");
        tracking.markAsDelivered("testcourier");

        assertDoesNotThrow(() -> tracking.printTrackingHistory());
    }
}
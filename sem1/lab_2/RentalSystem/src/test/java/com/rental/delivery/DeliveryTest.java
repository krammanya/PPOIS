package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    private DeliveryOrder order;
    private DeliveryAddress address;
    private DeliveryRecipient recipient;

    @BeforeEach
    void setUp() {
        order = new com.rental.bank.Order(new com.rental.model.Customer("John", "Doe", "+1234567890"));
        address = new com.rental.model.Address("Moscow", "Tverskaya", "1", "123");
        recipient = new com.rental.model.Customer("John", "Doe", "+1234567890");
    }

    @Test
    void delivery_shouldHandleAllDeliveryOperations() {
        Delivery delivery = new Delivery(order, address, recipient);

        assertEquals(order, delivery.getOrder());
        assertEquals(address, delivery.getDeliveryAddress());
        assertEquals(recipient, delivery.getRecipient());
        assertNotNull(delivery.getCreatedDate());
        assertNotNull(delivery.getEstimatedDeliveryDate());
        assertFalse(delivery.isDelivered());
        assertNull(delivery.getActualDeliveryDate());
        assertFalse(delivery.isOverdue());

        DeliveryAddress newAddress = new com.rental.model.Address("SPb", "Nevsky", "10", "45");
        DeliveryRecipient newRecipient = new com.rental.model.Customer("Jane", "Smith", "+0987654321");

        delivery.setDeliveryAddress(newAddress);
        delivery.setRecipient(newRecipient);

        assertEquals(newAddress, delivery.getDeliveryAddress());
        assertEquals(newRecipient, delivery.getRecipient());

        delivery.markAsDelivered();
        assertTrue(delivery.isDelivered());
        assertNotNull(delivery.getActualDeliveryDate());
    }

    @Test
    void delivery_shouldThrowExceptionsForInvalidOperations() {

        assertThrows(DeliveryException.class, () -> new Delivery(null, address, recipient));
        assertThrows(DeliveryException.class, () -> new Delivery(order, null, recipient));
        assertThrows(DeliveryException.class, () -> new Delivery(order, address, null));

        Delivery delivery = new Delivery(order, address, recipient);

        assertThrows(DeliveryException.class, () -> delivery.setDeliveryAddress(null));
        assertThrows(DeliveryException.class, () -> delivery.setRecipient(null));

        delivery.markAsDelivered();
        assertThrows(DeliveryException.class, () -> delivery.markAsDelivered());

        Delivery testDelivery = new Delivery(order, address, recipient);
        assertThrows(DeliveryException.class, () ->
                testDelivery.setEstimatedDeliveryDate(LocalDateTime.now().minusDays(1)));
    }

    @Test
    void delivery_shouldHandleDeliveryStatus() {
        Delivery delivery = new Delivery(order, address, recipient);

        assertFalse(delivery.isDelivered());
        assertFalse(delivery.isOverdue());

        delivery.markAsDelivered();
        assertTrue(delivery.isDelivered());
        assertFalse(delivery.isOverdue());
    }
}
package com.rental.delivery;

import com.rental.interfaces.DeliveryOrder;
import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.exceptions.DeliveryException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookedDeliveryTest {

    @Test
    void bookedDelivery_shouldHandleAllScheduledScenarios() {
        DeliveryOrder order = new com.rental.bank.Order(new com.rental.model.Customer("Ivan", "Ivanov", "+1234567890"));
        DeliveryAddress address = new com.rental.model.Address("Гикало", "Минск", "125009", "Беларусь");
        DeliveryRecipient recipient = new com.rental.model.Customer("Ivan", "Ivanov", "+1234567890");

        LocalDateTime futureDate = LocalDateTime.now().plusHours(2);
        BookedDelivery delivery = new BookedDelivery(order, address, recipient, futureDate);

        assertTrue(delivery.isScheduled());
        assertEquals(futureDate, delivery.getScheduledDate());
        assertFalse(delivery.isOverdue());
        assertFalse(delivery.isScheduledDatePassed());

        LocalDateTime newDate = LocalDateTime.now().plusHours(3);
        delivery.scheduleDelivery(newDate);
        assertEquals(newDate, delivery.getScheduledDate());

        LocalDateTime pastDate = LocalDateTime.now().minusHours(1);
        assertThrows(DeliveryException.class, () -> delivery.scheduleDelivery(pastDate));
    }

    @Test
    void bookedDelivery_shouldHandleUnscheduledAndEdgeCases() {
        DeliveryOrder order = new com.rental.bank.Order(new com.rental.model.Customer("Petr", "Petrov", "+79997654321"));
        DeliveryAddress address = new com.rental.model.Address("Невский", "Санкт-Петербург", "191186", "Россия");
        DeliveryRecipient recipient = new com.rental.model.Customer("Petr", "Petrov", "+79997654321");

        BookedDelivery delivery = new BookedDelivery(order, address, recipient);

        assertFalse(delivery.isScheduled());
        assertNull(delivery.getScheduledDate());
        assertFalse(delivery.isScheduledDatePassed());

        LocalDateTime invalidDate = LocalDateTime.now().plusMinutes(30);
        assertThrows(DeliveryException.class, () ->
                new BookedDelivery(order, address, recipient, invalidDate));
    }
}
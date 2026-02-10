package com.rental.service;

import com.rental.bank.Order;
import com.rental.exceptions.RentalHistoryException;
import com.rental.item.Item;
import com.rental.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private RentalService service;
    private Order validOrder;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item("Samsung", "Galaxy", 2022, 30.0);
        item2 = new Item("Sony", "WH-1000XM4", 2021, 25.0);

        Customer customer = new Customer("Ivan", "Ivanov", "ivan@example.com");
        validOrder = new Order(customer);

        validOrder.addItem(item1);
        validOrder.addItem(item2);

        service = new RentalService(customer);
    }


    @Test
    void createRentalFromOrder_shouldCreateRentalRecordWhenValid() {
        RentalRecord record = service.createRentalFromOrder(validOrder, 5);

        assertNotNull(record);
        assertEquals("ACTIVE", record.getStatus());
        assertEquals(55.0, record.getTotalCost(), 0.01); // 30 + 25
        assertEquals(2, record.getRentedItems().size());
        assertTrue(record.getRentedItems().contains("Samsung Galaxy (2022)"));
        assertTrue(record.getRentedItems().contains("Sony WH-1000XM4 (2021)"));
        assertEquals(LocalDate.now().plusDays(5), record.getReturnDate());
    }

    @Test
    void createRentalFromOrder_shouldThrowExceptionWhenOrderIsNull() {
        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> service.createRentalFromOrder(null, 3)
        );
        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    void createRentalFromOrder_shouldThrowExceptionWhenRentalDaysNotPositive() {
        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> service.createRentalFromOrder(validOrder, 0)
        );
        assertEquals("Rental days must be positive", exception.getMessage());
    }


    @Test
    void completeRental_shouldMarkRecordAsCompletedWhenActive() {
        RentalRecord record = service.createRentalFromOrder(validOrder, 3);
        String orderId = record.getOrderId();

        boolean result = service.completeRental(orderId);

        assertTrue(result);
        assertEquals("COMPLETED", service.getRentalHistory().findRecordByOrderId(orderId).getStatus());
    }

    @Test
    void completeRental_shouldReturnFalseWhenRecordNotFound() {
        boolean result = service.completeRental("NONEXISTENT-123");
        assertFalse(result);
    }

    @Test
    void completeRental_shouldReturnFalseWhenRecordAlreadyCompleted() {
        RentalRecord record = service.createRentalFromOrder(validOrder, 3);
        String orderId = record.getOrderId();
        service.completeRental(orderId);

        boolean result = service.completeRental(orderId);
        assertFalse(result);
    }

    @Test
    void extendRental_shouldExtendReturnDateWhenActive() {
        RentalRecord record = service.createRentalFromOrder(validOrder, 5);
        String orderId = record.getOrderId();
        LocalDate originalReturnDate = record.getReturnDate();

        boolean result = service.extendRental(orderId, 3);

        assertTrue(result);
        LocalDate newReturnDate = service.getRentalHistory().findRecordByOrderId(orderId).getReturnDate();
        assertEquals(originalReturnDate.plusDays(3), newReturnDate);
    }

    @Test
    void extendRental_shouldReturnFalseWhenRecordNotFound() {
        boolean result = service.extendRental("NONEXISTENT-123", 2);
        assertFalse(result);
    }

    @Test
    void extendRental_shouldReturnFalseWhenRecordNotActive() {
        RentalRecord record = service.createRentalFromOrder(validOrder, 3);
        String orderId = record.getOrderId();
        service.completeRental(orderId);

        boolean result = service.extendRental(orderId, 2);
        assertFalse(result);
    }
}
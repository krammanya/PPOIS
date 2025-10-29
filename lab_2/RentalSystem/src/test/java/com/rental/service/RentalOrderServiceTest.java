package com.rental.service;

import com.rental.interfaces.PaymentProcessor;
import com.rental.interfaces.InsuranceCalculator;
import com.rental.interfaces.RentalHistory;
import com.rental.bank.Order;
import com.rental.exceptions.RentalHistoryException;
import com.rental.item.Item;
import com.rental.bank.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalOrderServiceTest {

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private InsuranceCalculator insuranceCalculator;

    @Mock
    private RentalHistory rentalHistory;

    @InjectMocks
    private RentalOrderService rentalOrderService;

    private Order order;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        item = new Item("Samsung", "Galaxy", 2022, 30.0);
        order = mock(Order.class);
        Price price = new Price(30.0, "RUB");
        when(order.getOrderPrice()).thenReturn(price);
        when(order.getTotalPrice()).thenReturn(30.0);
        when(order.getItems()).thenReturn(List.of(item));
    }


    @Test
    void processFullRental_shouldCreateRentalRecordOnSuccessfulPayment() {
        when(paymentProcessor.process(any(Price.class))).thenReturn(true);
        RentalRecord expectedRecord = new RentalRecord(
                "RENT-123", LocalDate.now(), LocalDate.now().plusDays(5),
                List.of("Samsung Galaxy (2022)"), 30.0, "ACTIVE"
        );

        RentalRecord record = rentalOrderService.processFullRental(order, 5, "basic");

        assertNotNull(record);
        assertEquals("ACTIVE", record.getStatus());
        assertEquals(30.0, record.getTotalCost(), 0.01);
        assertEquals(1, record.getRentedItems().size());
        assertTrue(record.getRentedItems().contains("Samsung Galaxy (2022)"));
        verify(rentalHistory).addRentalRecord(any(RentalRecord.class));
    }

    @Test
    void processFullRental_shouldThrowExceptionOnPaymentFailure() {
        when(paymentProcessor.process(any(Price.class))).thenReturn(false);

        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> rentalOrderService.processFullRental(order, 3, "premium")
        );
        assertEquals("Payment failed", exception.getMessage());
        verify(rentalHistory, never()).addRentalRecord(any());
    }


    @Test
    void cancelRental_shouldCancelActiveRental() {
        RentalRecord activeRecord = mock(RentalRecord.class);
        when(activeRecord.isActive()).thenReturn(true);
        when(rentalHistory.findRecordByOrderId("RENT-123")).thenReturn(activeRecord);

        boolean result = rentalOrderService.cancelRental("RENT-123");

        assertTrue(result);
        verify(activeRecord).setStatus("CANCELLED");
    }

    @Test
    void cancelRental_shouldReturnFalseIfRecordNotFound() {
        when(rentalHistory.findRecordByOrderId("UNKNOWN")).thenReturn(null);
        assertFalse(rentalOrderService.cancelRental("UNKNOWN"));
    }

    @Test
    void cancelRental_shouldReturnFalseIfRecordNotActive() {
        RentalRecord completedRecord = mock(RentalRecord.class);
        when(completedRecord.isActive()).thenReturn(false);
        when(rentalHistory.findRecordByOrderId("RENT-123")).thenReturn(completedRecord);

        assertFalse(rentalOrderService.cancelRental("RENT-123"));
    }

    @Test
    void extendRental_shouldExtendActiveRental() {
        RentalRecord activeRecord = mock(RentalRecord.class);
        when(activeRecord.isActive()).thenReturn(true);
        when(activeRecord.getReturnDate()).thenReturn(LocalDate.of(2025, 11, 1));
        when(rentalHistory.findRecordByOrderId("RENT-123")).thenReturn(activeRecord);

        boolean result = rentalOrderService.extendRental("RENT-123", 5);

        assertTrue(result);
        verify(activeRecord).setReturnDate(LocalDate.of(2025, 11, 6)); // +5 дней
    }

    @Test
    void extendRental_shouldReturnFalseIfRecordNotActive() {
        RentalRecord inactiveRecord = mock(RentalRecord.class);
        when(inactiveRecord.isActive()).thenReturn(false);
        when(rentalHistory.findRecordByOrderId("RENT-123")).thenReturn(inactiveRecord);

        assertFalse(rentalOrderService.extendRental("RENT-123", 3));
    }


    @Test
    void getRentalStatus_shouldReturnStatusWhenFound() {
        RentalRecord record = mock(RentalRecord.class);
        when(record.getStatus()).thenReturn("ACTIVE");
        when(rentalHistory.findRecordByOrderId("RENT-123")).thenReturn(record);

        assertEquals("ACTIVE", rentalOrderService.getRentalStatus("RENT-123"));
    }

    @Test
    void getRentalStatus_shouldReturnNotFoundWhenRecordMissing() {
        when(rentalHistory.findRecordByOrderId("UNKNOWN")).thenReturn(null);
        assertEquals("NOT_FOUND", rentalOrderService.getRentalStatus("UNKNOWN"));
    }


    @Test
    void getDeliveryByRentalId_shouldReturnNull() {
        assertNull(rentalOrderService.getDeliveryByRentalId("ANY_ID"));
    }
}
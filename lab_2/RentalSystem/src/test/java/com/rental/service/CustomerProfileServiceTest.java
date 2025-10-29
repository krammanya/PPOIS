package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;
import com.rental.model.SMSNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerProfileServiceTest {

    @Mock
    private RentalHistory rentalHistory;

    @Mock
    private SMSNotification notification;

    private Customer customer;
    private CustomerProfileService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer("Ivan", "Ivanov", "ivan@example.com");
        service = new CustomerProfileService(customer, rentalHistory, notification);
    }

    @Test
    void sendRentalConfirmation_shouldSendNotification() {
        String orderId = "RENT-123";

        service.sendRentalConfirmation(orderId);

        verify(notification).send(customer, "Ваша аренда RENT-123 подтверждена.");
    }

    @Test
    void getTotalRentals_shouldDelegateToRentalHistory() {
        when(rentalHistory.getTotalRentals()).thenReturn(7);

        int total = service.getTotalRentals();

        assertEquals(7, total);
        verify(rentalHistory).getTotalRentals();
    }

    @Test
    void hasOverdueRentals_shouldReturnTrueIfAnyRentalIsOverdue() {
        RentalRecord overdueRecord = mock(RentalRecord.class);
        when(overdueRecord.isOverdue()).thenReturn(true);
        when(rentalHistory.getActiveRentals()).thenReturn(List.of(overdueRecord));

        assertTrue(service.hasOverdueRentals());
    }

    @Test
    void hasOverdueRentals_shouldReturnFalseIfNoOverdueRentals() {
        RentalRecord activeRecord = mock(RentalRecord.class);
        when(activeRecord.isOverdue()).thenReturn(false);
        when(rentalHistory.getActiveRentals()).thenReturn(List.of(activeRecord));

        assertFalse(service.hasOverdueRentals());
    }

    @Test
    void hasOverdueRentals_shouldReturnFalseIfNoActiveRentals() {
        when(rentalHistory.getActiveRentals()).thenReturn(List.of());

        assertFalse(service.hasOverdueRentals());
    }
}
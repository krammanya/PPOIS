package com.rental.service;

import com.rental.model.Customer;
import com.rental.bank.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentalAgreementTest {

    @Test
    void constructor_ShouldInitializeAllFieldsAndCalculateDates() {
        String agreementNumber = "AGR-001";
        Customer customer = mock(Customer.class);
        Order order = mock(Order.class);
        int rentalDays = 7;
        double expectedTotalAmount = 350.0;

        when(order.getTotalPrice()).thenReturn(expectedTotalAmount);
        when(customer.getFirstName()).thenReturn("John");
        when(customer.getLastName()).thenReturn("Doe");

        RentalAgreement agreement = new RentalAgreement(agreementNumber, customer, order, rentalDays);

        assertEquals(agreementNumber, agreement.getAgreementNumber());
        assertEquals(customer, agreement.getCustomer());
        assertEquals(order, agreement.getOrder());
        assertEquals(LocalDate.now(), agreement.getStartDate());
        assertEquals(LocalDate.now().plusDays(rentalDays), agreement.getEndDate());
        assertEquals(expectedTotalAmount, agreement.getTotalAmount());
        assertEquals("ACTIVE", agreement.getStatus());
        assertTrue(agreement.isActive());
        assertFalse(agreement.isExpired());
        assertEquals(7, agreement.getRentalDurationInDays());
        assertFalse(agreement.isOverdue());

        String toStringResult = agreement.toString();
        assertTrue(toStringResult.contains("AGR-001"));
        assertTrue(toStringResult.contains("John Doe"));
        assertTrue(toStringResult.contains("350.0"));
    }
}
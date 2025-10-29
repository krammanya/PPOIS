package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerLoyaltyServiceTest {

    private Customer customer;
    private RentalHistory rentalHistory;
    private CustomerLoyaltyService loyaltyService;

    @BeforeEach
    void setUp() {
        customer = mock(Customer.class);
        rentalHistory = mock(RentalHistory.class);
        loyaltyService = new CustomerLoyaltyService();
    }

    @Test
    void isEligibleForDiscount_ShouldReturnTrue_WhenCustomerHasEnoughPoints() {
        when(rentalHistory.getTotalRentals()).thenReturn(15);
        assertTrue(loyaltyService.isEligibleForDiscount(customer, rentalHistory));
    }

    @Test
    void isEligibleForDiscount_ShouldReturnFalse_WhenCustomerHasNotEnoughPoints() {
        when(rentalHistory.getTotalRentals()).thenReturn(5);
        assertFalse(loyaltyService.isEligibleForDiscount(customer, rentalHistory));
    }

    @Test
    void getLoyaltyLevel_ShouldReturnGold_For10OrMoreRentals() {
        assertEquals("GOLD", loyaltyService.getLoyaltyLevel(10));
        assertEquals("GOLD", loyaltyService.getLoyaltyLevel(15));
    }

    @Test
    void getLoyaltyLevel_ShouldReturnSilver_For5To9Rentals() {
        assertEquals("SILVER", loyaltyService.getLoyaltyLevel(5));
        assertEquals("SILVER", loyaltyService.getLoyaltyLevel(9));
    }

    @Test
    void getLoyaltyLevel_ShouldReturnBronze_ForLessThan5Rentals() {
        assertEquals("BRONZE", loyaltyService.getLoyaltyLevel(4));
        assertEquals("BRONZE", loyaltyService.getLoyaltyLevel(0));
    }
}
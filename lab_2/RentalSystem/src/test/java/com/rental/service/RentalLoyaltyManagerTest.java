package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalLoyaltyManagerTest {

    @Mock
    private CustomerLoyaltyService loyaltyService;

    @Mock
    private RentalHistory rentalHistory;

    @InjectMocks
    private RentalLoyaltyManager manager;

    private Customer customer;
    private RentalRecord rentalRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer("John", "Doe", "john@example.com");
        rentalRecord = new RentalRecord(
                "RENT-123",
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(5),
                List.of("Item1", "Item2"),
                100.0,
                "ACTIVE"
        );
    }

    @Test
    void isCustomerEligibleForDiscount_shouldDelegateToLoyaltyService() {
        when(loyaltyService.isEligibleForDiscount(customer, rentalHistory)).thenReturn(true);

        boolean result = manager.isCustomerEligibleForDiscount(customer);

        assertTrue(result);
        verify(loyaltyService).isEligibleForDiscount(customer, rentalHistory);
    }

    @Test
    void getCustomerLoyaltyLevel_shouldReturnLevelBasedOnTotalRentals() {
        when(rentalHistory.getTotalRentals()).thenReturn(15);
        when(loyaltyService.getLoyaltyLevel(15)).thenReturn("GOLD");

        String level = manager.getCustomerLoyaltyLevel(customer);

        assertEquals("GOLD", level);
        verify(rentalHistory).getTotalRentals();
        verify(loyaltyService).getLoyaltyLevel(15);
    }

    @Test
    void calculateBonusPointsForRental_shouldApplyBonusMultiplier() {
        when(loyaltyService.calculateBonusPoints(2)).thenReturn(200);

        int points = manager.calculateBonusPointsForRental(rentalRecord);

        assertEquals(200, points);
        verify(loyaltyService).calculateBonusPoints(2);
    }

    @Test
    void calculateBonusPointsForRental_shouldWorkWithCustomMultiplier() {

    }
}
package com.rental.bank;

import com.rental.exceptions.InvalidRentalDurationException;
import com.rental.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(new Customer("Ivan", "Ivanov", "+1234567890"));
    }

    @Test
    void setRentalPeriod_shouldHandleAllValidationScenarios() {
        Status status = new Status(order);

        status.setRentalPeriod(1);
        status.setRentalPeriod(365);

        assertThrows(InvalidRentalDurationException.class, () -> status.setRentalPeriod(0));
        assertThrows(InvalidRentalDurationException.class, () -> status.setRentalPeriod(-1));
        assertThrows(InvalidRentalDurationException.class, () -> status.setRentalPeriod(366));
    }
}
package com.rental.service;

import com.rental.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentalReportServiceTest {

    private RentalRecord record1;
    private RentalRecord record2;
    private Customer customer;
    private List<RentalRecord> rentalRecords;

    @BeforeEach
    void setUp() {
        record1 = mock(RentalRecord.class);
        record2 = mock(RentalRecord.class);
        customer = mock(Customer.class);

        when(record1.getRentalDate()).thenReturn(LocalDate.now().minusDays(10));
        when(record2.getRentalDate()).thenReturn(LocalDate.now().minusDays(5));

        rentalRecords = List.of(record1, record2);
    }

    @Test
    void getTotalRevenue_ShouldCalculateRevenueForAllValidRecords() {

        when(record1.getTotalCost()).thenReturn(100.0);
        when(record2.getTotalCost()).thenReturn(200.0);
        RentalReportService service = new RentalReportService(rentalRecords);

        double result = service.getTotalRevenue();

        assertEquals(300.0, result);
    }

    @Test
    void getVehicleRentalCount_ShouldCountAllRentedItems() {
        when(record1.getRentedItems()).thenReturn(List.of("Car", "Bike"));
        when(record2.getRentedItems()).thenReturn(List.of("Car", "Scooter"));
        RentalReportService service = new RentalReportService(rentalRecords);

        Map<String, Long> result = service.getVehicleRentalCount();

        assertEquals(2, result.get("Car"));
        assertEquals(1, result.get("Bike"));
        assertEquals(1, result.get("Scooter"));
    }

    @Test
    void getRentalCountByCustomer_ShouldCountRecordsWithItems() {

        when(record1.getRentedItems()).thenReturn(List.of("Car"));
        when(record2.getRentedItems()).thenReturn(List.of("Bike"));
        RentalReportService service = new RentalReportService(rentalRecords);

        long result = service.getRentalCountByCustomer(customer);

        assertEquals(2, result);
    }
}
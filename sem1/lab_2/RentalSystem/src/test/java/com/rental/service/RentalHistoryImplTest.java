package com.rental.service;

import com.rental.exceptions.RentalHistoryException;
import com.rental.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentalHistoryImplTest {

    private Customer validCustomer;
    private RentalRecord activeRecord;
    private RentalRecord completedRecord;

    @BeforeEach
    void setUp() {
        validCustomer = mock(Customer.class);
        activeRecord = mock(RentalRecord.class);
        completedRecord = mock(RentalRecord.class);

        when(activeRecord.getOrderId()).thenReturn("ORD-001");
        when(activeRecord.isActive()).thenReturn(true);
        when(activeRecord.isCompleted()).thenReturn(false);

        when(completedRecord.getOrderId()).thenReturn("ORD-002");
        when(completedRecord.isActive()).thenReturn(false);
        when(completedRecord.isCompleted()).thenReturn(true);
    }

    @Test
    void constructor_ShouldThrowException_WhenCustomerIsNull() {
        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> new RentalHistoryImpl(null)
        );
        assertEquals("Customer cannot be null", exception.getMessage());
    }

    @Test
    void addRentalRecord_ShouldThrowException_WhenRecordIsNull() {
        RentalHistoryImpl history = new RentalHistoryImpl(validCustomer);
        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> history.addRentalRecord(null)
        );
        assertEquals("Rental record cannot be null", exception.getMessage());
    }

    @Test
    void addRentalRecord_ShouldThrowException_WhenDuplicateOrderIdExists() {
        RentalHistoryImpl history = new RentalHistoryImpl(validCustomer);
        history.addRentalRecord(activeRecord);

        RentalHistoryException exception = assertThrows(
                RentalHistoryException.class,
                () -> history.addRentalRecord(activeRecord)
        );
        assertEquals("Rental record with orderId ORD-001 already exists", exception.getMessage());
    }

    @Test
    void removeRentalRecord_ShouldThrowException_WhenOrderIdIsInvalid() {
        RentalHistoryImpl history = new RentalHistoryImpl(validCustomer);

        assertThrows(RentalHistoryException.class, () -> history.removeRentalRecord(null));
        assertThrows(RentalHistoryException.class, () -> history.removeRentalRecord(""));
        assertThrows(RentalHistoryException.class, () -> history.removeRentalRecord("   "));
    }

    @Test
    void findRecordByOrderId_ShouldThrowException_WhenOrderIdIsInvalid() {
        RentalHistoryImpl history = new RentalHistoryImpl(validCustomer);

        assertThrows(RentalHistoryException.class, () -> history.findRecordByOrderId(null));
        assertThrows(RentalHistoryException.class, () -> history.findRecordByOrderId(""));
        assertThrows(RentalHistoryException.class, () -> history.findRecordByOrderId("   "));
    }
    @Test
    void shouldManageRecordsCorrectly() {
        RentalHistoryImpl history = new RentalHistoryImpl(validCustomer);

        history.addRentalRecord(activeRecord);
        history.addRentalRecord(completedRecord);

        assertEquals(2, history.getTotalRentals());
        assertEquals(validCustomer, history.getCustomer());

        assertEquals(activeRecord, history.findRecordByOrderId("ORD-001"));
        assertEquals(completedRecord, history.findRecordByOrderId("ORD-002"));
        assertNull(history.findRecordByOrderId("NON-EXISTENT"));

        assertTrue(history.removeRentalRecord("ORD-001"));
        assertEquals(1, history.getTotalRentals());
        assertNull(history.findRecordByOrderId("ORD-001"));

        assertFalse(history.removeRentalRecord("NON-EXISTENT"));

        assertEquals(0, history.getActiveRentals().size());
        assertEquals(1, history.getCompletedRentals().size());

        RentalRecord newActive = mock(RentalRecord.class);
        when(newActive.getOrderId()).thenReturn("ORD-003");
        when(newActive.isActive()).thenReturn(true);
        when(newActive.isCompleted()).thenReturn(false);

        history.addRentalRecord(newActive);
        assertEquals(1, history.getActiveRentals().size());
        assertEquals(2, history.getTotalRentals());

        assertNotSame(history.getRecords(), history.getRecords());
        assertNotSame(history.getAllRecords(), history.getAllRecords());
    }
}
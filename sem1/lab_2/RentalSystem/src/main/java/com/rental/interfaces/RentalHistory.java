package com.rental.interfaces;

import com.rental.model.Customer;
import com.rental.service.RentalRecord;
import java.util.List;

public interface RentalHistory {
    void addRentalRecord(RentalRecord record);
    boolean removeRentalRecord(String orderId);
    RentalRecord findRecordByOrderId(String orderId);
    List<RentalRecord> getAllRecords();
    List<RentalRecord> getActiveRentals();
    List<RentalRecord> getCompletedRentals();
    int getTotalRentals();
    Customer getCustomer();
    List<RentalRecord> getRecords();
}

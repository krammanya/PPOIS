package com.rental.service;

import java.time.LocalDate;
import java.util.List;

public class RentalRecord {
    private String orderId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private List<String> rentedItems;
    private double totalCost;
    private String status;

    public RentalRecord(String orderId, LocalDate rentalDate, LocalDate returnDate,
                        List<String> rentedItems, double totalCost, String status) {
        this.orderId = orderId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.rentedItems = rentedItems;
        this.totalCost = totalCost;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public LocalDate getRentalDate() { return rentalDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public List<String> getRentedItems() { return rentedItems; }
    public double getTotalCost() { return totalCost; }
    public String getStatus() { return status; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isOverdue() {
        return "OVERDUE".equals(status) || (returnDate != null && LocalDate.now().isAfter(returnDate));
    }
}

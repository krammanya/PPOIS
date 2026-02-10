package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;
import com.rental.interfaces.Notification;
import com.rental.model.SMSNotification;

public class CustomerProfileService {
    private Customer customer;
    private RentalHistory rentalHistory;
    private Notification notification;

    public CustomerProfileService(Customer customer, RentalHistory rentalHistory, SMSNotification notification) {
        this.customer = customer;
        this.rentalHistory = rentalHistory;
        this.notification = notification;
    }

    public void sendRentalConfirmation(String orderId) {
        notification.send(customer, "Ваша аренда " + orderId + " подтверждена.");
    }

    public int getTotalRentals() {
        return rentalHistory.getTotalRentals();
    }

    public boolean hasOverdueRentals() {
        return rentalHistory.getActiveRentals().stream()
                .anyMatch(RentalRecord::isOverdue);
    }
}

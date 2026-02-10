package com.rental.service;

import com.rental.model.Customer;
import com.rental.bank.Order;
import java.time.LocalDate;

public class RentalAgreement {
    private String agreementNumber;
    private Customer customer;
    private Order order;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private String status = "ACTIVE";

    public RentalAgreement(String agreementNumber, Customer customer, Order order, int rentalDays) {
        this.agreementNumber = agreementNumber;
        this.customer = customer;
        this.order = order;
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusDays(rentalDays);
        this.totalAmount = order.getTotalPrice();
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order getOrder() {
        return order;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    public long getRentalDurationInDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    public boolean isOverdue() {
        return isExpired() && isActive();
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "agreementNumber='" + agreementNumber + '\'' +
                ", customer=" + customer.getFirstName() + " " + customer.getLastName() +
                ", totalAmount=" + totalAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}

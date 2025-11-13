package com.system.interfaces;

public interface FinancialProduct {
    double calculateTotalCost();
    double calculateMonthlyPayment();
    double calculateOverpayment();
    double getInitialPayment();

}

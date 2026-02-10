package com.system.bank;

import com.system.property.PropertyType;
import com.system.exceptions.InvalidPriceException;
import com.system.exceptions.InvalidPeriodException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BaseFinancialProductTest {
    private Price validPrice;
    private TestFinancialProduct product;

    @BeforeEach
    void setUp() throws InvalidPeriodException, InvalidPriceException {
        validPrice = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
    }

    @Test
    void testBaseFinancialProductCreation() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 10.0, 12);

        assertEquals(validPrice, product.getTotalPrice());
        assertEquals(20.0, product.getInitialPaymentPercent());
        assertEquals(10.0, product.getRate());
        assertEquals(12, product.getTermMonths());
        assertEquals(LocalDate.now(), product.getStartDate());
    }

    @Test
    void testBaseFinancialProductValidation() {
        assertThrows(InvalidPriceException.class, () ->
                new TestFinancialProduct(null, 20.0, 10.0, 12)
        );

        assertThrows(InvalidPriceException.class, () ->
                new TestFinancialProduct(validPrice, -5.0, 10.0, 12)
        );

        assertThrows(InvalidPriceException.class, () ->
                new TestFinancialProduct(validPrice, 120.0, 10.0, 12)
        );

        assertThrows(InvalidPriceException.class, () ->
                new TestFinancialProduct(validPrice, 20.0, -5.0, 12)
        );

        assertThrows(InvalidPriceException.class, () ->
                new TestFinancialProduct(validPrice, 20.0, 10.0, 0)
        );
    }

    @Test
    void testGetInitialPayment() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 10.0, 12);

        double expectedInitialPayment = 1000000.0 * 1.0 * 0.2;
        assertEquals(expectedInitialPayment, product.getInitialPayment(), 0.01);
    }

    @Test
    void testCalculateMonthlyPaymentWithInterest() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 10.0, 12);

        double monthlyPayment = product.calculateMonthlyPayment();
        assertTrue(monthlyPayment > 0);
        assertTrue(monthlyPayment < 1000000.0);
    }

    @Test
    void testCalculateMonthlyPaymentZeroInterest() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 0.0, 12);

        double amount = 1000000.0 * 1.0 - (1000000.0 * 1.0 * 0.2);
        double expectedMonthlyPayment = amount / 12;

        assertEquals(expectedMonthlyPayment, product.calculateMonthlyPayment(), 0.01);
    }

    @Test
    void testCalculateOverpayment() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 10.0, 12);

        double overpayment = product.calculateOverpayment();
        double totalCost = product.calculateTotalCost();
        double adjustedPrice = validPrice.calculateAdjustedPrice();

        assertEquals(totalCost - adjustedPrice, overpayment, 0.01);
        assertTrue(overpayment > 0);
    }

    @Test
    void testCalculateTotalCost() throws InvalidPriceException {
        product = new TestFinancialProduct(validPrice, 20.0, 10.0, 12);

        double totalCost = product.calculateTotalCost();
        double initialPayment = product.getInitialPayment();
        double monthlyPayment = product.calculateMonthlyPayment();
        double calculatedTotalCost = initialPayment + (monthlyPayment * 12);

        assertEquals(calculatedTotalCost, totalCost, 0.01);
    }

    private static class TestFinancialProduct extends BaseFinancialProduct {
        public TestFinancialProduct(Price totalPrice, double initialPaymentPercent,
                                    double rate, int termMonths) throws InvalidPriceException {
            super(totalPrice, initialPaymentPercent, rate, termMonths);
        }

        @Override
        public double calculateTotalCost() {
            return getInitialPayment() + (calculateMonthlyPayment() * termMonths);
        }

    }
}
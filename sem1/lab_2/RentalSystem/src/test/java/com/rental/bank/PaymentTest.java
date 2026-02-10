package com.rental.bank;

import com.rental.exceptions.PaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Price price;
    private CreditCard validCard;
    private Cash sufficientCash;

    @BeforeEach
    void setUp() {
        price = new Price(1000, "RUB");
        validCard = new CreditCard("1234567812345678", "Ivan Ivanov", "12/25");
        sufficientCash = new Cash(1500);
    }

    @Test
    void payment_shouldHandleCardPaymentScenario() {
        Payment payment = new Payment(price);
        payment.setCardPayment(validCard);

        assertTrue(payment.isValidPaymentMethod());
        assertTrue(payment.processPayment());

        assertThrows(PaymentException.class, () -> payment.calculateChange());
    }

    @Test
    void payment_shouldHandleCashPaymentScenario() {
        Payment payment = new Payment(price);
        payment.setCashPayment(sufficientCash);

        assertTrue(payment.isValidPaymentMethod());
        assertTrue(payment.processPayment());

        double change = payment.calculateChange();
        assertTrue(change > 0);
    }

    @Test
    void payment_shouldThrowExceptionsForInvalidScenarios() {
        Payment payment = new Payment(price);

        assertThrows(PaymentException.class, () -> payment.processPayment());
        assertFalse(payment.isValidPaymentMethod());

        payment.setCardPayment(validCard);

        try {
            java.lang.reflect.Field field = Payment.class.getDeclaredField("paymentType");
            field.setAccessible(true);
            field.set(payment, "unknown");

            assertThrows(PaymentException.class, () -> payment.processPayment());
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    void payment_shouldHandleNullPrice() {
        Payment payment = new Payment(null);
        payment.setCardPayment(validCard);

        assertThrows(PaymentException.class, () -> payment.processPayment());
    }
}
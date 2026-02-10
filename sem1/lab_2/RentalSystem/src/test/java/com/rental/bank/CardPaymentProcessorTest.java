package com.rental.bank;

import com.rental.exceptions.PaymentException;
import com.rental.exceptions.InsufficientCashException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CardPaymentProcessorTest {

    private CreditCard validCard;
    private CreditCard invalidCard;
    private Price validPrice;
    private Price zeroPrice;
    private Price largePrice;

    @BeforeEach
    void setUp() {
        validCard = new CreditCard("1234567812345678", "Ivan Ivanov", "12/25");
        invalidCard = new CreditCard("1234", "Ivan Ivanov", "12/25");
        validPrice = new Price(1000, "RUB");
        zeroPrice = new Price(0, "RUB");
        largePrice = new Price(100001, "RUB");
    }

    @Test
    void cardPaymentProcessor_shouldHandleAllPaymentScenarios() {
        CardPaymentProcessor processor = new CardPaymentProcessor(validCard);

        assertTrue(processor.process(validPrice));
        assertTrue(processor.isValid());

        processor.setPaymentGateway("STRIPE");

        assertEquals(0.0, processor.calculateChange(validPrice));
    }

    @Test
    void cardPaymentProcessor_shouldThrowExceptionsForInvalidCases() {

        CardPaymentProcessor invalidProcessor = new CardPaymentProcessor(invalidCard);
        assertThrows(PaymentException.class, () -> invalidProcessor.process(validPrice));
        assertFalse(invalidProcessor.isValid());

        CardPaymentProcessor processor = new CardPaymentProcessor(validCard);
        assertThrows(PaymentException.class, () -> processor.process(zeroPrice));

        assertThrows(PaymentException.class, () -> processor.process(largePrice));

        CardPaymentProcessor nullProcessor = new CardPaymentProcessor(null);
        assertThrows(PaymentException.class, () -> nullProcessor.process(validPrice));
        assertFalse(nullProcessor.isValid());
    }
}
package com.rental.bank;

import com.rental.exceptions.PaymentException;
import com.rental.exceptions.InsufficientCashException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CashPaymentProcessorTest {

    private Cash sufficientCash;
    private Cash insufficientCash;
    private Cash zeroCash;
    private Price validPrice;

    @BeforeEach
    void setUp() {
        sufficientCash = new Cash(1500);
        insufficientCash = new Cash(500);
        zeroCash = new Cash(0);
        validPrice = new Price(1000, "RUB");
    }

    @Test
    void cashPaymentProcessor_shouldHandleAllPaymentScenarios() {
        CashPaymentProcessor processor = new CashPaymentProcessor(sufficientCash);

        assertTrue(processor.process(validPrice));
        assertTrue(processor.isValid());

        assertEquals(500, processor.calculateChange(validPrice), 0.01);

        processor.setServiceFee(50);
        assertEquals(450, processor.calculateChange(validPrice), 0.01);

    }

    @Test
    void cashPaymentProcessor_shouldThrowExceptionsForInvalidCases() {

        CashPaymentProcessor insufficientProcessor = new CashPaymentProcessor(insufficientCash);
        assertThrows(InsufficientCashException.class, () -> insufficientProcessor.process(validPrice));

        CashPaymentProcessor zeroProcessor = new CashPaymentProcessor(zeroCash);
        assertThrows(PaymentException.class, () -> zeroProcessor.process(validPrice));
        assertFalse(zeroProcessor.isValid());

        CashPaymentProcessor nullProcessor = new CashPaymentProcessor(null);
        assertThrows(PaymentException.class, () -> nullProcessor.process(validPrice));
        assertThrows(PaymentException.class, () -> nullProcessor.calculateChange(validPrice));
        assertFalse(nullProcessor.isValid());
    }
}
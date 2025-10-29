package com.rental.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer("Ivan", "Ivanov", "+1234567890");
        account = new Account(customer, "ivanivanov", "password123");
    }

    @Test
    void account_shouldHandleAllAccountOperations() {

        assertEquals(customer, account.getCustomer());
        assertEquals("ivanivanov", account.getUsername());
        assertEquals("password123", account.getPassword());

        assertTrue(account.verifyPassword("password123"));
        assertFalse(account.verifyPassword("wrongpassword"));

        account.changeUsername("ivan_ivanov");
        assertEquals("ivan_ivanov", account.getUsername());

        account.changePassword("password123", "newpassword456");
        assertEquals("newpassword456", account.getPassword());
        assertTrue(account.verifyPassword("newpassword456"));

        assertDoesNotThrow(() -> account.validate());

        account.setId(1L);
        assertEquals(1L, account.getId());

        String toString = account.toString();
        assertTrue(toString.contains("ivan_ivanov"));
        assertTrue(toString.contains("Ivan Ivanov"));
    }

    @Test
    void account_shouldThrowExceptionsForInvalidData() {

        assertThrows(IllegalArgumentException.class, () -> new Account(null, "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> account.setCustomer(null));

        assertThrows(IllegalArgumentException.class, () -> account.setUsername(null));
        assertThrows(IllegalArgumentException.class, () -> account.setUsername(""));
        assertThrows(IllegalArgumentException.class, () -> account.setUsername("  "));
        assertThrows(IllegalArgumentException.class, () -> account.setUsername("ab"));
        assertThrows(IllegalArgumentException.class, () -> account.setPassword(null));
        assertThrows(IllegalArgumentException.class, () -> account.setPassword(""));
        assertThrows(IllegalArgumentException.class, () -> account.setPassword("  "));
        assertThrows(IllegalArgumentException.class, () -> account.setPassword("12345")); // слишком короткий

        assertThrows(SecurityException.class, () -> account.changePassword("wrongold", "newpass"));
    }

    @Test
    void account_shouldHandleEmptyConstructorAndValidation() {
        Account emptyAccount = new Account();

        assertThrows(IllegalStateException.class, () -> emptyAccount.validate());

        emptyAccount.setCustomer(customer);
        emptyAccount.setUsername("testuser");
        emptyAccount.setPassword("testpass");

        assertDoesNotThrow(() -> emptyAccount.validate());
    }
}
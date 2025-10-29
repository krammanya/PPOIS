package com.rental.model;

import com.rental.exceptions.LoginHistoryException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LoginHistoryTest {

    private Account createValidAccount() {
        Customer customer = new Customer("Ivan", "Ivanov", "ivan@example.com");
        return new Account(customer, "user123", "securePass123");
    }

    @Test
    void constructor_shouldInitializeAndValidate() {
        Account account = createValidAccount();
        LoginHistory history = new LoginHistory(account, "203.0.113.45", true);

        assertNotNull(history.getLoginTime());
        assertEquals(account, history.getAccount());
        assertEquals("203.0.113.45", history.getIpAddress());
        assertTrue(history.isSuccessful());
    }

    @Test
    void constructor_shouldThrowOnInvalidAccountOrIP() {
        Account validAccount = createValidAccount();

        assertThrows(LoginHistoryException.class, () ->
                new LoginHistory(null, "192.0.2.1", true)
        );

        assertThrows(LoginHistoryException.class, () ->
                new LoginHistory(validAccount, null, true)
        );

        assertThrows(LoginHistoryException.class, () ->
                new LoginHistory(validAccount, "", true)
        );
    }

    @Test
    void validate_shouldPassForValidInstance() {
        LoginHistory history = new LoginHistory(createValidAccount(), "198.51.100.1", true);
        assertDoesNotThrow(history::validate);
    }

    @Test
    void validate_shouldThrowIfRequiredFieldsMissing() {
        LoginHistory history = new LoginHistory();
        assertThrows(LoginHistoryException.class, history::validate);
    }

    @Test
    void isFromSuspiciousIP_shouldDetectLocalAndPrivateIPs() {
        assertTrue(new LoginHistory(createValidAccount(), "127.0.0.1", true).isFromSuspiciousIP());
        assertTrue(new LoginHistory(createValidAccount(), "192.168.1.10", true).isFromSuspiciousIP());
        assertFalse(new LoginHistory(createValidAccount(), "203.0.113.45", true).isFromSuspiciousIP());
    }

    @Test
    void isRecent_shouldReturnFalseForOldRecord() {
        LoginHistory recent = new LoginHistory(createValidAccount(), "203.0.113.45", true);
        assertTrue(recent.isRecent());

    }
}
package com.rental.model;

import com.rental.exceptions.LoginHistoryException;
import com.rental.interfaces.Identifiable;
import java.time.LocalDateTime;

public class LoginHistory implements Identifiable {
    private Long id;
    private Account account;
    private LocalDateTime loginTime;
    private String ipAddress;
    private boolean successful;

    public LoginHistory() {}

    public LoginHistory(Account account, String ipAddress, boolean successful) {
        setAccount(account);
        setIpAddress(ipAddress);
        this.successful = successful;
        this.loginTime = LocalDateTime.now();
    }

    @Override
    public Long getId() { return id; }
    public Account getAccount() { return account; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public String getIpAddress() { return ipAddress; }
    public boolean isSuccessful() { return successful; }

    @Override
    public void setId(Long id) { this.id = id; }

    public void setAccount(Account account) {
        if (account == null) {
            throw new LoginHistoryException("Account cannot be null");
        }
        this.account = account;
    }

    public void setIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new LoginHistoryException("IP address cannot be null or empty");
        }
        this.ipAddress = ipAddress.trim();
    }

    public void setSuccessful(boolean successful) { this.successful = successful; }

    public void validate() {
        if (account == null || loginTime == null || ipAddress == null) {
            throw new LoginHistoryException("LoginHistory required fields are not set");
        }
    }

    public boolean isFromSuspiciousIP() {
        return ipAddress.startsWith("192.168.") || ipAddress.equals("127.0.0.1");
    }

    public boolean isRecent() {
        return loginTime.isAfter(LocalDateTime.now().minusHours(24));
    }

    public void markAsFailed() {
        this.successful = false;
    }

    @Override
    public String toString() {
        return "LoginHistory{" +
                "id=" + id +
                ", account=" + (account != null ? account.getUsername() : "null") +
                ", loginTime=" + loginTime +
                ", ipAddress='" + ipAddress + '\'' +
                ", successful=" + successful +
                '}';
    }
}
package com.rental.model;

import com.rental.interfaces.Identifiable;

public class Account implements Identifiable {
    private Long id;
    private Customer customer;
    private String username;
    private String password;

    public Account() {}

    public Account(Customer customer, String username, String password) {
        setCustomer(customer);
        setUsername(username);
        setPassword(password);
    }

    public Customer getCustomer() { return customer; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        this.password = password;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword)) {
            throw new SecurityException("Current password is incorrect");
        }
        setPassword(newPassword);
    }

    public void changeUsername(String newUsername) {
        setUsername(newUsername);
    }

    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void validate() {
        if (customer == null || username == null || password == null) {
            throw new IllegalStateException("Account required fields are not set");
        }
    }

    @Override
    public Long getId() { return id; }
    @Override
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getFirstName() + " " + customer.getLastName() : "null") +
                ", username='" + username + '\'' +
                '}';
    }
}
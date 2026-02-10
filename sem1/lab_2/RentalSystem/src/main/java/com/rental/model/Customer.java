package com.rental.model;

import com.rental.interfaces.DeliveryAddress;
import com.rental.interfaces.DeliveryRecipient;
import com.rental.interfaces.Identifiable;
import com.rental.exceptions.InvalidAddressException;

public class Customer implements DeliveryRecipient, Identifiable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;

    public Customer() {}

    public Customer(String firstName, String lastName, String phoneNumber) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
    }

    public Customer(String firstName, String lastName, String phoneNumber, Address address) {
        this(firstName, lastName, phoneNumber);
        setAddress(address);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        this.phoneNumber = phoneNumber.trim();
    }

    public void setAddress(Address address) {
        if (address != null) {
            address.validate();
        }
        this.address = address;
    }

    public void validate() {
        if (firstName == null || lastName == null || phoneNumber == null) {
            throw new IllegalStateException("Customer required fields are not set");
        }
    }

    @Override
    public DeliveryAddress getAddress() {
        return address;
    }

    @Override
    public boolean hasAddress() {
        return address != null && address.isValid();
    }

    @Override
    public Long getId() { return id; }
    @Override
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + (address != null ? address.getFullAddress() : "null") +
                '}';
    }
}
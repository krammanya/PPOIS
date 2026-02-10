package com.rental.interfaces;

import com.rental.model.Customer;

public interface Notification {
    void send(Customer customer, String message);
}
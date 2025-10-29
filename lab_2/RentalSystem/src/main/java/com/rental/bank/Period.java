package com.rental.bank;

import java.time.LocalDate;

public class Period {
    private int rentalDays;
    public Period(int rentalDays) {this.rentalDays = rentalDays; }

    public LocalDate calculateEndDate() {
        return LocalDate.now().plusDays(rentalDays);
    }
    public LocalDate getStartDate() {
        return LocalDate.now();
    }
    public boolean isOverdue() {
        return LocalDate.now().isAfter(calculateEndDate());
    }
}

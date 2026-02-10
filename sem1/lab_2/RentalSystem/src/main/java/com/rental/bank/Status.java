package com.rental.bank;
import com.rental.exceptions.InvalidRentalDurationException;

public class Status {
    private Order order;
    private Period period;

    public Status(Order order) {
        this.order = order;
    }

    public void setRentalPeriod(int rentalDays) {
        if (rentalDays <= 0) {
            throw new InvalidRentalDurationException(rentalDays);
        }
        if (rentalDays > 365) {
            throw new InvalidRentalDurationException("Rental duration cannot exceed 1 year");
        }
        this.period = new Period(rentalDays);
    }
}

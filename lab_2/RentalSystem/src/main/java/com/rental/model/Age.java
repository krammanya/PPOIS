package com.rental.model;
import com.rental.exceptions.InvalidAgeException;

public class Age {
    private final int years;

    public Age(int years) {
        if (years < 0 || years > 120) {
            throw new InvalidAgeException("Invalid age: " + years + ". Age must be between 0 and 120 years.");
        }
        this.years = years;
    }

    public int getYears() {
        return years;
    }

    public boolean isAtLeast(int minAge) {
        return years >= minAge;
    }

    public boolean isBetween(int minAge, int maxAge) {
        return years >= minAge && years <= maxAge;
    }

    public boolean isAdult() {
        return years >= 18;
    }
}

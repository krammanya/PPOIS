package com.system.bank;

import com.system.exceptions.InvalidPeriodException;

public class Period {
    private int months;
    private String description;

    public Period(int months) throws InvalidPeriodException {
        if (months <= 0) {
            throw new InvalidPeriodException("Период должен быть положительным числом. Получено: " + months);
        }
        this.months = months;
        this.description = generateDescription(months);
    }

    private String generateDescription(int months) {
        if (months == 1) return "1 месяц";
        if (months < 12) return months + " месяцев";
        if (months == 12) return "1 год";
        if (months % 12 == 0) return (months / 12) + " лет";
        return (months / 12) + " лет " + (months % 12) + " месяцев";
    }

    public int getMonths() { return months; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return description;
    }
}
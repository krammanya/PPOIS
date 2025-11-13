package com.system.bank;

import com.system.property.PropertyType;
import com.system.exceptions.InvalidPriceException;
import com.system.exceptions.InvalidPeriodException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void testPriceCalculationsAndValidations() throws InvalidPeriodException, InvalidPriceException {
        Period period = new Period(12);
        Price price = new Price(1000000.0, "RUB", period, PropertyType.APARTMENT);

        assertEquals(1000000.0, price.getBasePrice());
        assertEquals("RUB", price.getCurrency());
        assertEquals(period, price.getPeriod());
        assertEquals(PropertyType.APARTMENT, price.getPropertyType());

        assertEquals(1000000.0, price.calculateAdjustedPrice());
        assertEquals(83333.33, price.calculateMonthlyPrice(), 0.01);
        assertEquals(500000.0, price.calculatePriceForPeriod(6), 0.01);
        assertEquals(900000.0, price.calculatePriceWithDiscount(), 0.01);

        String priceInfo = price.getPriceInfo();
        assertNotNull(priceInfo);
        assertFalse(priceInfo.isEmpty());
    }

    @Test
    void testPriceValidationsAndPropertyTypeMultipliers() throws InvalidPeriodException, InvalidPriceException {
        assertThrows(InvalidPriceException.class, () -> new Price(-1000.0, "RUB", new Period(1), PropertyType.APARTMENT));
        assertThrows(InvalidPriceException.class, () -> new Price(1000.0, "", new Period(1), PropertyType.APARTMENT));
        assertThrows(InvalidPriceException.class, () -> new Price(1000.0, "RUB", new Period(1), PropertyType.APARTMENT).calculatePriceForPeriod(0));

        assertEquals(1300000.0, new Price(1000000.0, "RUB", new Period(6), PropertyType.HOUSE).calculateAdjustedPrice(), 0.01);
        assertEquals(1800000.0, new Price(1000000.0, "USD", new Period(24), PropertyType.COMMERCIAL).calculateAdjustedPrice(), 0.01);
        assertEquals(1500000.0, new Price(1000000.0, "EUR", new Period(1), PropertyType.OFFICE).calculateAdjustedPrice(), 0.01);
        assertEquals(2000000.0, new Price(1000000.0, "EUR", new Period(1), PropertyType.HOTEL).calculateAdjustedPrice(), 0.01);
    }
}
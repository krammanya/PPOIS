package com.rental.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void price_shouldHandleAllDiscountScenariosAndBranches() {

        Price price1 = new Price(1000, "RUB");
        assertEquals(1000, price1.getBasePrice());
        assertEquals("RUB", price1.getCurrency());
        assertEquals(1000, price1.getFinalPrice());
        assertNull(price1.getDiscount());

        Discount nullDiscount = new Discount();
        nullDiscount.setDiscountType(null);
        Price price2 = new Price(2000, "RUB", nullDiscount);
        assertEquals(2000, price2.getFinalPrice());

        Discount percentageDiscount = new Discount("PERCENTAGE");
        Price price3 = new Price(1000, "RUB", percentageDiscount);
        assertEquals(900, price3.getFinalPrice(), 0.01);

        Discount fixedDiscount = new Discount("FIXED");
        Price price4 = new Price(1000, "RUB", fixedDiscount);
        assertEquals(500, price4.getFinalPrice(), 0.01);

        Price price5 = new Price(300, "RUB", fixedDiscount);
        assertEquals(0, price5.getFinalPrice(), 0.01);

        Discount seasonalDiscount = new Discount("SEASONAL");
        Price price6 = new Price(1000, "RUB", seasonalDiscount);
        assertEquals(850, price6.getFinalPrice(), 0.01);

        Discount unknownDiscount = new Discount("UNKNOWN");
        Price price7 = new Price(1000, "RUB", unknownDiscount);
        assertEquals(1000, price7.getFinalPrice());
    }

    @Test
    void price_shouldHandleSettersAndRegularPrice() {
        Price price = new Price();
        assertEquals("RUB", price.getCurrency());
        assertEquals(0, price.getBasePrice(), 0.01);
        assertEquals(0, price.getFinalPrice(), 0.01);

        price.setBasePrice(1500);
        price.setCurrency("USD");

        assertEquals(1500, price.getBasePrice());
        assertEquals("USD", price.getCurrency());

        Discount discount = new Discount("PERCENTAGE");
        price.setDiscount(discount);
        price.calculatePriceWithDiscount();
        assertEquals(1350, price.getFinalPrice(), 0.01);

        double regularPrice = price.calculateRegularPrice();
        assertEquals(1500, regularPrice);
        assertEquals(1500, price.getFinalPrice());
        assertNull(price.getDiscount());

        price.setDiscount(discount);
        price.calculatePriceWithDiscount();
        assertEquals(1350, price.getFinalPrice(), 0.01);

        price.setBasePrice(2000);
        price.calculatePriceWithDiscount();
        assertEquals(1800, price.getFinalPrice(), 0.01);
    }
}
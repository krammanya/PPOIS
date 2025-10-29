package com.rental.bank;

import com.rental.interfaces.DeliveryOrder;
import com.rental.model.Customer;
import com.rental.item.Item;
import com.rental.exceptions.OrderException;
import java.util.ArrayList;
import java.util.List;

public class Order implements DeliveryOrder {
    private Customer customer;
    private List<Item> items;
    private Price totalPrice;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalPrice = new Price(0, "RUB");
    }

    public void confirmOrder() {
        if (items.isEmpty()) {
            throw new OrderException("Cannot confirm empty order");
        }
        calculateTotalPrice();
    }

    public void addItem(Item item) {
        if (item.isAvailable()) {
            items.add(item);
            item.setAvailable(false);
            calculateTotalPrice();
        } else {
            throw new OrderException("Item " + item.getFullName() + " is not available");
        }
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            item.setAvailable(true);
            calculateTotalPrice();
        } else {
            throw new OrderException("Item " + item.getFullName() + " not found in order");
        }
    }

    private void calculateTotalPrice() {
        double basePrice = items.stream()
                .mapToDouble(Item::getRentalPrice)
                .sum();

        this.totalPrice.setBasePrice(basePrice);
        this.totalPrice.calculatePriceWithDiscount();
    }

    public void applyDiscount(Discount discount) {
        if (discount == null) {
            throw new OrderException("Discount cannot be null");
        }
        this.totalPrice.setDiscount(discount);
        this.totalPrice.calculatePriceWithDiscount();
    }

    public Customer getCustomer() { return customer; }
    public List<Item> getItems() { return new ArrayList<>(items); }

    @Override
    public String getId() {
        return "ORDER-" + System.currentTimeMillis();
    }

    @Override
    public double getTotalPrice() {
        return this.totalPrice.getFinalPrice();
    }

    public Price getOrderPrice() {
        return totalPrice;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
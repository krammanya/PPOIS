package com.rental.service;

import com.rental.item.Item;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Item> items;
    public InventoryManager(List<Item> items) {
        this.items = new ArrayList<>(items);
    }
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean rentItem(Item item) {
        if (item != null && item.isAvailable()) {
            item.setAvailable(false);
            return true;
        }
        return false;
    }

    public List<Item> getAvailableItems() {
        List<Item> availableItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    public List<Item> getRentedItems() {
        List<Item> rentedItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isAvailable()) {
                rentedItems.add(item);
            }
        }
        return rentedItems;
    }

    public boolean isItemAvailable(Item item) {
        return items.contains(item) && item.isAvailable();
    }

    public int getTotalItemsCount() {
        return items.size();
    }
}

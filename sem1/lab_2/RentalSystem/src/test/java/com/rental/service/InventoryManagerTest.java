package com.rental.service;

import com.rental.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private InventoryManager manager;
    private Item availableItem;
    private Item rentedItem;

    @BeforeEach
    void setUp() {
        availableItem = new Item("Toyota", "Corolla", 2022, 50.0);
        rentedItem = new Item("Giant", "Bike", 2021, 30.0);
        rentedItem.setAvailable(false);

        List<Item> initialItems = new ArrayList<>();
        initialItems.add(availableItem);
        initialItems.add(rentedItem);

        manager = new InventoryManager(initialItems);
    }

    @Test
    void constructor_shouldCreateCopyOfList() {
        List<Item> originalList = new ArrayList<>();
        Item item = new Item("Honda", "Scooter", 2020, 25.0);
        originalList.add(item);

        InventoryManager localManager = new InventoryManager(originalList);
        originalList.clear();

        assertEquals(1, localManager.getTotalItemsCount());
    }

    @Test
    void addItem_shouldAddItemWhenNonNull() {
        Item newItem = new Item("Ford", "F-150", 2023, 80.0);
        manager.addItem(newItem);
        assertEquals(3, manager.getTotalItemsCount());
        assertTrue(manager.getAvailableItems().contains(newItem));
    }

    @Test
    void addItem_shouldNotAddNullItem() {
        int initialCount = manager.getTotalItemsCount();
        manager.addItem(null);
        assertEquals(initialCount, manager.getTotalItemsCount());
    }

    @Test
    void removeItem_shouldRemoveExistingItem() {
        assertTrue(manager.removeItem(availableItem));
        assertEquals(1, manager.getTotalItemsCount());
        assertFalse(manager.getAvailableItems().contains(availableItem));
    }

    @Test
    void removeItem_shouldReturnFalseForNonExistingItem() {
        Item notInInventory = new Item("BMW", "X5", 2022, 100.0);
        assertFalse(manager.removeItem(notInInventory));
        assertEquals(2, manager.getTotalItemsCount());
    }

    @Test
    void rentItem_shouldRentAvailableItem() {
        assertTrue(manager.rentItem(availableItem));
        assertFalse(availableItem.isAvailable());
        assertTrue(manager.getRentedItems().contains(availableItem));
    }

    @Test
    void rentItem_shouldNotRentAlreadyRentedItem() {
        assertFalse(manager.rentItem(rentedItem));
        assertFalse(rentedItem.isAvailable());
    }

    @Test
    void rentItem_shouldNotRentNullItem() {
        assertFalse(manager.rentItem(null));
    }

    @Test
    void getAvailableItems_shouldReturnOnlyAvailableItems() {
        List<Item> available = manager.getAvailableItems();
        assertEquals(1, available.size());
        assertTrue(available.contains(availableItem));
        assertFalse(available.contains(rentedItem));
    }

    @Test
    void getRentedItems_shouldReturnOnlyRentedItems() {
        List<Item> rented = manager.getRentedItems();
        assertEquals(1, rented.size());
        assertTrue(rented.contains(rentedItem));
        assertFalse(rented.contains(availableItem));
    }

    @Test
    void isItemAvailable_shouldReturnTrueForAvailableItemInInventory() {
        assertTrue(manager.isItemAvailable(availableItem));
    }

    @Test
    void isItemAvailable_shouldReturnFalseForRentedItem() {
        assertFalse(manager.isItemAvailable(rentedItem));
    }

    @Test
    void isItemAvailable_shouldReturnFalseForItemNotInInventory() {
        Item externalItem = new Item("Audi", "A4", 2021, 90.0);
        assertFalse(manager.isItemAvailable(externalItem));
    }

    @Test
    void isItemAvailable_shouldReturnFalseForNullItem() {
        assertFalse(manager.isItemAvailable(null));
    }

    @Test
    void getTotalItemsCount_shouldReturnCorrectCount() {
        assertEquals(2, manager.getTotalItemsCount());
        manager.addItem(new Item("Tesla", "Model 3", 2023, 120.0));
        assertEquals(3, manager.getTotalItemsCount());
    }
}
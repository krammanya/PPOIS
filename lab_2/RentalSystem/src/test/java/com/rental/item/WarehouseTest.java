package com.rental.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void getTotalItemCount_shouldReturnSumOfAllItems() {
        Warehouse warehouse = new Warehouse("Main");
        warehouse.addElectronic(new ElectronicItem("Dell", "XPS", 2023, 45.0, "Laptop", 24));
        warehouse.addSport(new SportItem("Nike", "Air Max", 2022, 15.0, "Running", "42"));
        warehouse.addGeneralItem(new Item("Chair", "Office", 2023, 10.0));

        assertEquals(3, warehouse.getTotalItemCount());
    }

    @Test
    void addMethods_shouldAddItemsToCorrectCategories() {
        Warehouse warehouse = new Warehouse("Test");
        ElectronicItem electronic = new ElectronicItem("Samsung", "Galaxy", 2023, 25.0, "Phone", 12);
        SportItem sport = new SportItem("Trek", "Bike", 2022, 35.0, "Cycling", "L");
        Item general = new Item("Table", "Wooden", 2023, 20.0);

        warehouse.addElectronic(electronic);
        warehouse.addSport(sport);
        warehouse.addGeneralItem(general);

        assertEquals(3, warehouse.getTotalItemCount());
    }
}
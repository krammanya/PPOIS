package com.rental.item;
import java.util.ArrayList;
import java.util.List;

class Warehouse {
    private String name;
    private List<ElectronicItem> electronics;
    private List<SportItem> sports;
    private List<Item> generalItems;

    public Warehouse(String name) {
        this.name = name;
        this.electronics = new ArrayList<>();
        this.sports = new ArrayList<>();
        this.generalItems = new ArrayList<>();
    }

    public void addElectronic(ElectronicItem item) {
        electronics.add(item);
    }

    public void addSport(SportItem item) {
        sports.add(item);
    }

    public void addGeneralItem(Item item) {
        generalItems.add(item);
    }

    public int getTotalItemCount() {
        return electronics.size() + sports.size() + generalItems.size();
    }

    public void displayInventory() {
        System.out.println("Склад '" + name + "':");
        System.out.println("Электроника: " + electronics.size() + " items");
        System.out.println("Спорт: " + sports.size() + " items");
        System.out.println("Общие товары: " + generalItems.size() + " items");
    }
}
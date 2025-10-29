package com.rental.delivery;

import com.rental.exceptions.DeliveryException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tracking {
    private Delivery delivery;
    private List<TrackingEvent> events;
    private LocalDateTime lastUpdated;

    public Tracking(Delivery delivery) {
        if (delivery == null) {
            throw new DeliveryException("Доставка не может быть null");
        }
        this.delivery = delivery;
        this.events = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
        addEvent("Доставка создана", "Система");
    }

    public void addEvent(String description, String updatedBy) {
        if (description == null || description.trim().isEmpty()) {
            throw new DeliveryException("Описание события не может быть пустым");
        }
        TrackingEvent event = new TrackingEvent(description, updatedBy, LocalDateTime.now());
        events.add(event);
        this.lastUpdated = LocalDateTime.now();
    }

    public void addEvent(String description) {
        addEvent(description, "Система");
    }

    public void markAsDelivered(String deliveredBy) {
        if (delivery.isDelivered()) {
            throw new DeliveryException("Доставка уже была отмечена как завершенная");
        }
        delivery.markAsDelivered();
        addEvent("Доставка завершена", deliveredBy);
    }

    public List<TrackingEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void printTrackingHistory() {
        System.out.println("=== История отслеживания доставки ===");
        System.out.println("Дата создания: " + delivery.getCreatedDate());
        System.out.println("Предполагаемая дата доставки: " + delivery.getEstimatedDeliveryDate());
        System.out.println("Статус: " + (delivery.isDelivered() ? "Доставлено" :
                delivery.isOverdue() ? "Просрочено" : "В процессе"));

        if (delivery.isDelivered()) {
            System.out.println("Фактическая дата доставки: " + delivery.getActualDeliveryDate());
        }

        System.out.println("\nСобытия доставки:");
        for (int i = 0; i < events.size(); i++) {
            System.out.println((i + 1) + ". " + events.get(i));
        }
    }
}
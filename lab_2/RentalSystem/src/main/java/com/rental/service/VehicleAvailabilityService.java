package com.rental.service;

import com.rental.vehicle.Vehicle;
import com.rental.vehicle.VehicleMaintenance;
import com.rental.service.RentalRecord;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для проверки доступности транспортных средств для аренды.
 * <p>
 * Анализирует текущее состояние транспортных средств на основе записей об арендах
 * и техническом обслуживании. Определяет, доступно ли транспортное средство
 * для новой аренды в данный момент времени.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see Vehicle
 * @see VehicleMaintenance
 * @see RentalRecord
 */
public class VehicleAvailabilityService {
    private List<VehicleMaintenance> maintenanceRecords;
    private List<RentalRecord> rentalRecords;
    private LocalDateTime lastCheckedDate;

    /**
     * Создает сервис проверки доступности транспортных средств.
     *
     * @param maintenanceRecords список записей о техническом обслуживании транспортных средств
     * @param rentalRecords список записей об арендах транспортных средств
     */
    public VehicleAvailabilityService(List<VehicleMaintenance> maintenanceRecords,
                                      List<RentalRecord> rentalRecords) {
        this.maintenanceRecords = maintenanceRecords;
        this.rentalRecords = rentalRecords;
        this.lastCheckedDate = LocalDateTime.now();
    }

    /**
     * Проверяет, доступно ли транспортное средство для аренды.
     * <p>
     * Выполняет две основные проверки:
     * <ol>
     *   <li>Проверяет, не арендовано ли транспортное средство в данный момент (активные аренды)</li>
     *   <li>Проверяет, не требуется ли транспортному средству техническое обслуживание</li>
     * </ol>
     * Транспортное средство считается доступным только если оно не арендовано
     * и не требует технического обслуживания.
     * </p>
     *
     * @param vehicle транспортное средство для проверки доступности
     * @return true если транспортное средство доступно для аренды, false в противном случае
     * @see RentalRecord#isActive()
     * @see VehicleMaintenance#isDueForMaintenance()
     */
    public boolean isVehicleAvailable(Vehicle vehicle) {
        boolean isRented = rentalRecords.stream()
                .anyMatch(r -> r.isActive() && r.getRentedItems().contains(vehicle.getFullName()));

        boolean needsMaintenance = maintenanceRecords.stream()
                .anyMatch(m -> m.getVehicle().equals(vehicle) && m.isDueForMaintenance());

        this.lastCheckedDate = LocalDateTime.now();

        return !isRented && !needsMaintenance;
    }

    /**
     * Возвращает дату и время последней проверки доступности.
     * <p>
     * Обновляется при каждом вызове метода {@link #isVehicleAvailable(Vehicle)}.
     * </p>
     *
     * @return дата и время последней проверки доступности транспортного средства
     */
    public LocalDateTime getLastCheckedDate() {
        return lastCheckedDate;
    }
}
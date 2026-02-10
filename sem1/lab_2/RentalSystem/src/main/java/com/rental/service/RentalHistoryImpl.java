package com.rental.service;

import com.rental.interfaces.RentalHistory;
import com.rental.model.Customer;
import com.rental.exceptions.RentalHistoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса RentalHistory для управления историей аренд клиента.
 * <p>
 * Класс предоставляет функционал для добавления, удаления, поиска и фильтрации
 * записей об аренде для конкретного клиента. Обеспечивает валидацию входных данных
 * и поддерживает разделение записей на активные и завершенные аренды.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see RentalHistory
 * @see Customer
 * @see RentalRecord
 */
public class RentalHistoryImpl implements RentalHistory {
    private Customer customer;
    private List<RentalRecord> records;

    /**
     * Создает новый объект истории аренд для указанного клиента.
     *
     * @param customer клиент, для которого создается история аренд
     * @throws RentalHistoryException если передан null клиент
     */
    public RentalHistoryImpl(Customer customer) {
        if (customer == null) {
            throw new RentalHistoryException("Customer cannot be null");
        }
        this.customer = customer;
        this.records = new ArrayList<>();
    }

    /**
     * Добавляет новую запись об аренде в историю.
     * <p>
     * Проверяет, что запись не равна null и что запись с таким orderId
     * еще не существует в истории.
     * </p>
     *
     * @param record добавляемая запись об аренде
     * @throws RentalHistoryException если record равен null или запись с таким orderId уже существует
     */
    public void addRentalRecord(RentalRecord record) {
        if (record == null) {
            throw new RentalHistoryException("Rental record cannot be null");
        }
        if (findRecordByOrderId(record.getOrderId()) != null) {
            throw new RentalHistoryException("Rental record with orderId " + record.getOrderId() + " already exists");
        }
        records.add(record);
    }

    /**
     * Удаляет запись об аренде по идентификатору заказа.
     *
     * @param orderId идентификатор заказа для удаления
     * @return true если запись была найдена и удалена, false если запись не найдена
     * @throws RentalHistoryException если orderId равен null или пустой строке
     */
    public boolean removeRentalRecord(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new RentalHistoryException("OrderId cannot be null or empty");
        }
        return records.removeIf(record -> record.getOrderId().equals(orderId));
    }

    /**
     * Находит запись об аренде по идентификатору заказа.
     *
     * @param orderId идентификатор заказа для поиска
     * @return найденная запись об аренде или null если запись не найдена
     * @throws RentalHistoryException если orderId равен null или пустой строке
     */
    public RentalRecord findRecordByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new RentalHistoryException("OrderId cannot be null or empty");
        }
        return records.stream()
                .filter(record -> record.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает все записи об арендах клиента.
     * <p>
     * Возвращает копию списка записей для обеспечения инкапсуляции.
     * </p>
     *
     * @return список всех записей об арендах
     */
    public List<RentalRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Возвращает список активных аренд клиента.
     * <p>
     * Активные аренды определяются по методу {@link RentalRecord#isActive()}.
     * </p>
     *
     * @return список активных записей об арендах
     * @see RentalRecord#isActive()
     */
    public List<RentalRecord> getActiveRentals() {
        return records.stream()
                .filter(RentalRecord::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список завершенных аренд клиента.
     * <p>
     * Завершенные аренды определяются по методу {@link RentalRecord#isCompleted()}.
     * </p>
     *
     * @return список завершенных записей об арендах
     * @see RentalRecord#isCompleted()
     */
    public List<RentalRecord> getCompletedRentals() {
        return records.stream()
                .filter(RentalRecord::isCompleted)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает общее количество аренд в истории клиента.
     *
     * @return количество записей об арендах
     */
    public int getTotalRentals() {
        return records.size();
    }

    /**
     * Возвращает клиента, к которому относится данная история аренд.
     *
     * @return клиент истории аренд
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Возвращает все записи об арендах клиента.
     * <p>
     * Аналогичен {@link #getAllRecords()}, предоставлен для обратной совместимости.
     * </p>
     *
     * @return список всех записей об арендах
     * @see #getAllRecords()
     */
    public List<RentalRecord> getRecords() {
        return new ArrayList<>(records);
    }
}
package com.rental.service;

import com.rental.interfaces.RentalHistory;
import com.rental.model.Customer;
import com.rental.bank.Order;
import com.rental.item.Item;
import com.rental.exceptions.RentalHistoryException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Основной сервис для управления арендами в системе.
 * <p>
 * Предоставляет функционал для создания, завершения и продления аренд,
 * а также управления историей аренд клиентов. Интегрируется с системой заказов
 * для преобразования заказов в записи об арендах.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see RentalHistory
 * @see RentalRecord
 * @see Order
 * @see Customer
 */
public class RentalService {
    private RentalHistory rentalHistory;

    /**
     * Создает сервис аренд для указанного клиента.
     * <p>
     * Инициализирует новую историю аренд для клиента с использованием
     * реализации {@link RentalHistoryImpl}.
     * </p>
     *
     * @param customer клиент, для которого создается сервис аренд
     */
    public RentalService(Customer customer) {
        this.rentalHistory = new RentalHistoryImpl(customer);
    }

    /**
     * Создает сервис аренд с готовой историей аренд.
     * <p>
     * Позволяет использовать существующую реализацию истории аренд,
     * что полезно для тестирования или интеграции с внешними системами.
     * </p>
     *
     * @param rentalHistory готовая реализация истории аренд
     */
    public RentalService(RentalHistory rentalHistory) {
        this.rentalHistory = rentalHistory;
    }

    /**
     * Создает запись об аренде на основе заказа.
     * <p>
     * Выполняет следующие шаги:
     * <ol>
     *   <li>Проверяет валидность заказа и количества дней аренды</li>
     *   <li>Извлекает названия арендуемых предметов из заказа</li>
     *   <li>Генерирует уникальный идентификатор аренды</li>
     *   <li>Рассчитывает даты начала и окончания аренды</li>
     *   <li>Создает и сохраняет запись об аренде</li>
     * </ol>
     * </p>
     *
     * @param order заказ, на основе которого создается аренда
     * @param rentalDays количество дней аренды
     * @return созданная запись об аренде
     * @throws RentalHistoryException если order равен null или rentalDays не положительное число
     * @see Order#getItems()
     * @see Order#getTotalPrice()
     * @see RentalHistory#addRentalRecord(RentalRecord)
     */
    public RentalRecord createRentalFromOrder(Order order, int rentalDays) {
        if (order == null) {
            throw new RentalHistoryException("Order cannot be null");
        }
        if (rentalDays <= 0) {
            throw new RentalHistoryException("Rental days must be positive");
        }

        List<String> itemNames = order.getItems().stream()
                .map(Item::getFullName)
                .collect(Collectors.toList());

        double totalCost = order.getTotalPrice();
        String orderId = "RENT-" + System.currentTimeMillis();

        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(rentalDays);

        RentalRecord record = new RentalRecord(
                orderId,
                rentalDate,
                returnDate,
                itemNames,
                totalCost,
                "ACTIVE"
        );

        rentalHistory.addRentalRecord(record);
        return record;
    }

    /**
     * Завершает активную аренду по идентификатору заказа.
     * <p>
     * Находит активную аренду и изменяет ее статус на "COMPLETED".
     * Завершение возможно только для активных аренд.
     * </p>
     *
     * @param orderId идентификатор заказа для завершения
     * @return true если аренда была найдена и завершена, false если аренда не найдена или не активна
     * @see RentalHistory#findRecordByOrderId(String)
     * @see RentalRecord#isActive()
     */
    public boolean completeRental(String orderId) {
        RentalRecord record = rentalHistory.findRecordByOrderId(orderId);
        if (record != null && record.isActive()) {
            record.setStatus("COMPLETED");
            return true;
        }
        return false;
    }

    /**
     * Продлевает срок активной аренды на указанное количество дней.
     * <p>
     * Находит активную аренду и увеличивает дату возврата на дополнительные дни.
     * Продление возможно только для активных аренд.
     * </p>
     *
     * @param orderId идентификатор заказа для продления
     * @param additionalDays количество дней для продления
     * @return true если аренда была найдена и продлена, false если аренда не найдена или не активна
     * @see RentalHistory#findRecordByOrderId(String)
     * @see RentalRecord#isActive()
     */
    public boolean extendRental(String orderId, int additionalDays) {
        RentalRecord record = rentalHistory.findRecordByOrderId(orderId);
        if (record != null && record.isActive()) {
            LocalDate newReturnDate = record.getReturnDate().plusDays(additionalDays);
            record.setReturnDate(newReturnDate);
            return true;
        }
        return false;
    }

    /**
     * Возвращает историю аренд, связанную с этим сервисом.
     *
     * @return история аренд клиента
     */
    public RentalHistory getRentalHistory() {
        return rentalHistory;
    }
}
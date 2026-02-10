package com.rental.service;

import com.rental.interfaces.PaymentProcessor;
import com.rental.interfaces.InsuranceCalculator;
import com.rental.interfaces.RentalHistory;
import com.rental.bank.Order;
import com.rental.exceptions.RentalHistoryException;
import com.rental.delivery.Delivery;
import com.rental.service.RentalRecord;
import com.rental.service.InsurancePolicy;
import com.rental.bank.Price;

/**
 * Сервис для обработки заказов аренды.
 * <p>
 * Координирует процесс полной обработки аренды, включая оплату, создание записей
 * и управление статусами аренд. Интегрируется с системами оплаты, страхования
 * и историей аренд для предоставления комплексного сервиса.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see PaymentProcessor
 * @see InsuranceCalculator
 * @see RentalHistory
 * @see Order
 */
public class RentalOrderService {
    private PaymentProcessor paymentProcessor;
    private InsuranceCalculator insuranceCalculator;
    private RentalHistory rentalHistory;
    private Delivery defaultDelivery;

    /**
     * Создает сервис обработки заказов аренды с указанными зависимостями.
     *
     * @param paymentProcessor процессор для обработки платежей
     * @param insuranceCalculator калькулятор страховых полисов
     * @param rentalHistory история аренд для хранения записей
     */
    public RentalOrderService(PaymentProcessor paymentProcessor,
                              InsuranceCalculator insuranceCalculator,
                              RentalHistory rentalHistory) {
        this.paymentProcessor = paymentProcessor;
        this.insuranceCalculator = insuranceCalculator;
        this.rentalHistory = rentalHistory;
        this.defaultDelivery = null;
    }

    /**
     * Обрабатывает полный цикл аренды, включая оплату и создание записи.
     * <p>
     * Выполняет следующие шаги:
     * <ol>
     *   <li>Обрабатывает платеж через PaymentProcessor</li>
     *   <li>Создает уникальный идентификатор заказа</li>
     *   <li>Формирует даты аренды и возврата</li>
     *   <li>Создает запись об аренде в системе</li>
     *   <li>Добавляет запись в историю аренд</li>
     * </ol>
     * </p>
     *
     * @param order заказ для обработки
     * @param rentalDays количество дней аренды
     * @param coverageType тип страхового покрытия
     * @return созданная запись об аренде
     * @throws RentalHistoryException если обработка платежа не удалась
     * @see PaymentProcessor#process(Price)
     * @see RentalHistory#addRentalRecord(RentalRecord)
     */
    public RentalRecord processFullRental(Order order, int rentalDays, String coverageType) {
        Price orderPrice = order.getOrderPrice();

        boolean paymentSuccess = paymentProcessor.process(orderPrice);

        if (!paymentSuccess) {
            throw new RentalHistoryException("Payment failed");
        }

        String orderId = "RENT-" + System.currentTimeMillis();
        java.time.LocalDate rentalDate = java.time.LocalDate.now();
        java.time.LocalDate returnDate = rentalDate.plusDays(rentalDays);

        java.util.List<String> itemNames = order.getItems().stream()
                .map(com.rental.item.Item::getFullName)
                .collect(java.util.stream.Collectors.toList());

        RentalRecord record = new RentalRecord(
                orderId,
                rentalDate,
                returnDate,
                itemNames,
                order.getTotalPrice(),
                "ACTIVE"
        );

        rentalHistory.addRentalRecord(record);

        return record;
    }

    /**
     * Отменяет активную аренду по идентификатору заказа.
     * <p>
     * Находит запись об аренде и изменяет ее статус на "CANCELLED".
     * Отмена возможна только для активных аренд.
     * </p>
     *
     * @param orderId идентификатор заказа для отмены
     * @return true если аренда была найдена и отменена, false если аренда не найдена или не активна
     * @see RentalHistory#findRecordByOrderId(String)
     * @see RentalRecord#isActive()
     */
    public boolean cancelRental(String orderId) {
        RentalRecord record = rentalHistory.findRecordByOrderId(orderId);
        if (record != null && record.isActive()) {
            record.setStatus("CANCELLED");
            return true;
        }
        return false;
    }

    /**
     * Продлевает срок активной аренды на указанное количество дней.
     * <p>
     * Находит активную аренду и увеличивает дату возврата на дополнительные дни.
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
            java.time.LocalDate newReturnDate = record.getReturnDate().plusDays(additionalDays);
            record.setReturnDate(newReturnDate);
            return true;
        }
        return false;
    }

    /**
     * Возвращает текущий статус аренды по идентификатору заказа.
     *
     * @param orderId идентификатор заказа для проверки статуса
     * @return статус аренды или "NOT_FOUND" если аренда не найдена
     * @see RentalHistory#findRecordByOrderId(String)
     * @see RentalRecord#getStatus()
     */
    public String getRentalStatus(String orderId) {
        RentalRecord record = rentalHistory.findRecordByOrderId(orderId);
        if (record == null) {
            return "NOT_FOUND";
        }
        return record.getStatus();
    }

    /**
     * Получает информацию о доставке для указанной аренды.
     * <p>
     * В текущей реализации всегда возвращает null.
     * Метод зарезервирован для будущей реализации функциональности доставки.
     * </p>
     *
     * @param orderId идентификатор заказа для получения доставки
     * @return null в текущей реализации
     * @see Delivery
     */
    public Delivery getDeliveryByRentalId(String orderId) {
        RentalRecord record = rentalHistory.findRecordByOrderId(orderId);
        if (record != null) {
            return null;
        }
        return null;
    }
}
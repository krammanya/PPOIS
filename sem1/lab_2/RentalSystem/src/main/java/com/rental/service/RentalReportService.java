package com.rental.service;

import com.rental.service.RentalRecord;
import com.rental.model.Customer;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для генерации отчетов по арендам.
 * <p>
 * Предоставляет функционал для анализа статистики аренд за определенный период.
 * Включает расчет выручки, подсчет аренд по транспортным средствам и анализ
 * активности клиентов. По умолчанию анализирует данные за последние 30 дней.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see RentalRecord
 * @see Customer
 */
public class RentalReportService {
    private List<RentalRecord> rentalRecords;
    private LocalDate reportStartDate;
    private LocalDate reportEndDate;

    /**
     * Создает сервис отчетов с указанными записями об арендах.
     * <p>
     * По умолчанию устанавливает период отчета на последние 30 дней.
     * </p>
     *
     * @param rentalRecords список записей об арендах для анализа
     */
    public RentalReportService(List<RentalRecord> rentalRecords) {
        this.rentalRecords = rentalRecords;
        this.reportStartDate = LocalDate.now().minusDays(30);
        this.reportEndDate = LocalDate.now();
    }

    /**
     * Рассчитывает общую выручку от всех аренд за установленный период отчета.
     * <p>
     * Фильтрует записи по дате аренды в пределах установленного периода
     * и суммирует общую стоимость всех подходящих аренд.
     * </p>
     *
     * @return общая выручка за период отчета
     * @see #reportStartDate
     * @see #reportEndDate
     */
    public double getTotalRevenue() {
        return rentalRecords.stream()
                .filter(record -> !record.getRentalDate().isBefore(reportStartDate) && !record.getRentalDate().isAfter(reportEndDate))
                .mapToDouble(RentalRecord::getTotalCost)
                .sum();
    }

    /**
     * Генерирует статистику количества аренд по каждому транспортному средству за период отчета.
     * <p>
     * Возвращает карту, где ключ - название транспортного средства, значение - количество аренд.
     * Учитывает только аренды в пределах установленного периода отчета.
     * </p>
     *
     * @return карта с статистикой аренд по транспортным средствам
     * @see #reportStartDate
     * @see #reportEndDate
     */
    public Map<String, Long> getVehicleRentalCount() {
        return rentalRecords.stream()
                .filter(record -> !record.getRentalDate().isBefore(reportStartDate) && !record.getRentalDate().isAfter(reportEndDate))
                .flatMap(record -> record.getRentedItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item,
                        Collectors.counting()
                ));
    }

    /**
     * Подсчитывает количество аренд для указанного клиента за период отчета.
     * <p>
     * Фильтрует записи по клиенту и дате аренды в пределах установленного периода.
     * Учитывает только аренды, содержащие хотя бы один арендованный предмет.
     * </p>
     *
     * @param customer клиент для анализа арендной активности
     * @return количество аренд клиента за период отчета
     * @see #reportStartDate
     * @see #reportEndDate
     */
    public long getRentalCountByCustomer(Customer customer) {
        return rentalRecords.stream()
                .filter(record -> record.getRentedItems().size() > 0 &&
                        !record.getRentalDate().isBefore(reportStartDate) &&
                        !record.getRentalDate().isAfter(reportEndDate))
                .count();
    }
}
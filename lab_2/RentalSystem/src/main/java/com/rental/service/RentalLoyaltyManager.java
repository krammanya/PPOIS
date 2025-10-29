package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;
import com.rental.bank.Order;
import com.rental.service.RentalRecord;

/**
 * Менеджер программы лояльности для клиентов системы аренды.
 * <p>
 * Координирует взаимодействие между сервисом лояльности и историей аренд
 * для определения уровня лояльности клиентов, расчета бонусных баллов
 * и проверки eligibility для скидок. Поддерживает настраиваемый множитель бонусов.
 * </p>
 *
 * @author Rental System
 * @version 1.0
 * @see CustomerLoyaltyService
 * @see RentalHistory
 * @see Customer
 */
public class RentalLoyaltyManager {
    private CustomerLoyaltyService loyaltyService;
    private RentalHistory rentalHistory;
    private double bonusMultiplier = 1.0;

    /**
     * Создает менеджер лояльности с указанными сервисами.
     *
     * @param loyaltyService сервис для расчета лояльности и бонусов
     * @param rentalHistory история аренд для анализа активности клиента
     */
    public RentalLoyaltyManager(CustomerLoyaltyService loyaltyService, RentalHistory rentalHistory) {
        this.loyaltyService = loyaltyService;
        this.rentalHistory = rentalHistory;
    }

    /**
     * Проверяет, имеет ли клиент право на получение скидки.
     * <p>
     * Делегирует проверку сервису лояльности, передавая информацию
     * об истории аренд клиента для принятия решения.
     * </p>
     *
     * @param customer клиент для проверки eligibility
     * @return true если клиент имеет право на скидку, false в противном случае
     * @see CustomerLoyaltyService#isEligibleForDiscount(Customer, RentalHistory)
     */
    public boolean isCustomerEligibleForDiscount(Customer customer) {
        return loyaltyService.isEligibleForDiscount(customer, rentalHistory);
    }

    /**
     * Определяет уровень лояльности клиента на основе общего количества аренд.
     * <p>
     * Использует общее количество аренд из истории для определения
     * соответствующего уровня лояльности (например, BRONZE, SILVER, GOLD).
     * </p>
     *
     * @param customer клиент для определения уровня лояльности
     * @return строковое представление уровня лояльности клиента
     * @see CustomerLoyaltyService#getLoyaltyLevel(int)
     * @see RentalHistory#getTotalRentals()
     */
    public String getCustomerLoyaltyLevel(Customer customer) {
        int totalRentals = rentalHistory.getTotalRentals();
        return loyaltyService.getLoyaltyLevel(totalRentals);
    }

    /**
     * Рассчитывает бонусные баллы для конкретной аренды.
     * <p>
     * Базовые баллы рассчитываются на основе количества арендованных предметов,
     * затем применяется бонусный множитель для финального расчета.
     * </p>
     *
     * @param record запись об аренде для расчета бонусных баллов
     * @return количество начисляемых бонусных баллов
     * @see CustomerLoyaltyService#calculateBonusPoints(int)
     * @see #bonusMultiplier
     */
    public int calculateBonusPointsForRental(RentalRecord record) {
        int basePoints = loyaltyService.calculateBonusPoints(record.getRentedItems().size());
        return (int) (basePoints * bonusMultiplier);
    }
}
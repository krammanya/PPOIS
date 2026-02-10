package com.system.bank;

import com.system.exceptions.PaymentException;
import com.system.interfaces.PaymentMethod;

public class SalePaymentService {
    private PropertySale propertySale;
    private PaymentMethod paymentMethod;

    public SalePaymentService(PropertySale propertySale) {
        if (propertySale == null) {
            throw new PaymentException("Продажа недвижимости не может быть null");
        }
        this.propertySale = propertySale;
    }

    public void setCreditPayment(Credit credit) {
        if (credit == null) {
            throw new PaymentException("Кредит не может быть null");
        }
        this.paymentMethod = new CreditPayment(credit, propertySale.getSalePrice());
    }

    public void setLeasingPayment(Leasing leasing) {
        if (leasing == null) {
            throw new PaymentException("Лизинг не может быть null");
        }
        this.paymentMethod = new LeasingPayment(leasing, propertySale.getSalePrice());
    }

    public void processInitialPayment() {
        if (!propertySale.isActive()) {
            throw new PaymentException("Продажа не активна");
        }
        if (paymentMethod == null) {
            throw new PaymentException("Метод оплаты не выбран");
        }

        System.out.println("Первоначальный взнос обработан. Сумма: " + paymentMethod.getAmount());
    }

    public void completeSaleWithPayment() {
        if (!propertySale.isActive()) {
            throw new PaymentException("Продажа не активна");
        }
        if (paymentMethod == null) {
            throw new PaymentException("Метод оплаты не выбран");
        }

        propertySale.completeSale();
        System.out.println("Продажа завершена. Метод оплаты: " + paymentMethod.getType());
    }

    public Credit getCredit() {
        if (paymentMethod instanceof CreditPayment) {
            return ((CreditPayment) paymentMethod).getCredit();
        }
        return null;
    }

    public Leasing getLeasing() {
        if (paymentMethod instanceof LeasingPayment) {
            return ((LeasingPayment) paymentMethod).getLeasing();
        }
        return null;
    }

    public PropertySale getPropertySale() {
        return propertySale;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean hasPaymentMethod() {
        return paymentMethod != null;
    }

    public String getPaymentDetails() {
        if (paymentMethod == null) {
            return "Метод оплаты не выбран";
        }
        return String.format("Тип: %s, Сумма: %.2f",
                paymentMethod.getType(), paymentMethod.getAmount());
    }
}
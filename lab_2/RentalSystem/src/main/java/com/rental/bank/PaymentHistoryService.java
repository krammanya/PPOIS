package com.rental.bank;

import com.rental.model.Customer;
import com.rental.bank.Order;
import com.rental.bank.Price;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentHistoryService {
    private List<PaymentRecord> paymentRecords;

    public PaymentHistoryService() {
        this.paymentRecords = new ArrayList<>();
    }

    public void addPaymentRecord(Customer customer, Order order, Price price, String paymentMethod) {
        PaymentRecord record = new PaymentRecord(customer, order, price, paymentMethod);
        paymentRecords.add(record);
    }

    public List<PaymentRecord> getPaymentHistoryByCustomer(Customer customer) {
        return paymentRecords.stream()
                .filter(record -> record.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    public List<PaymentRecord> getPaymentHistoryByOrder(Order order) {
        return paymentRecords.stream()
                .filter(record -> record.getOrder().equals(order))
                .collect(Collectors.toList());
    }

    public double getTotalAmountByCustomer(Customer customer) {
        return paymentRecords.stream()
                .filter(record -> record.getCustomer().equals(customer))
                .mapToDouble(record -> record.getPrice().getFinalPrice())
                .sum();
    }

    public List<PaymentRecord> getAllRecords() {
        return new ArrayList<>(paymentRecords);
    }

    public boolean isPaymentSuccessful(String orderId) {
        return paymentRecords.stream()
                .anyMatch(record -> record.getOrder().getId().equals(orderId) && record.isSuccessful());
    }

    public static class PaymentRecord {
        private Customer customer;
        private Order order;
        private Price price;
        private String paymentMethod;
        private LocalDateTime paymentDate;
        private boolean successful;

        public PaymentRecord(Customer customer, Order order, Price price, String paymentMethod) {
            this.customer = customer;
            this.order = order;
            this.price = price;
            this.paymentMethod = paymentMethod;
            this.paymentDate = LocalDateTime.now();
            this.successful = true;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Order getOrder() {
            return order;
        }

        public Price getPrice() {
            return price;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public LocalDateTime getPaymentDate() {
            return paymentDate;
        }

        public boolean isSuccessful() {
            return successful;
        }
    }
}

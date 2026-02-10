package com.system.bank;

import com.system.property.Property;
import com.system.users.Landlord;
import com.system.users.Tenant;
import com.system.exceptions.PropertySaleException;
import java.time.LocalDate;

public class PropertySale {
    private Property property;
    private Landlord seller;
    private Tenant buyer;
    private Price salePrice;
    private LocalDate saleDate;
    private LocalDate transferDate;
    private boolean completed;
    private boolean cancelled;
    private String contractTerms;

    public PropertySale(Property property, Landlord seller, Tenant buyer,
                        Price salePrice, LocalDate transferDate) {
        this.property = property;
        this.seller = seller;
        this.buyer = buyer;
        this.salePrice = salePrice;
        this.saleDate = LocalDate.now();
        this.transferDate = transferDate;
        this.completed = false;
        this.cancelled = false;
    }

    public Property getProperty() { return property; }
    public Landlord getSeller() { return seller; }
    public Tenant getBuyer() { return buyer; }
    public Price getSalePrice() { return salePrice; }
    public LocalDate getSaleDate() { return saleDate; }
    public LocalDate getTransferDate() { return transferDate; }
    public String getContractTerms() { return contractTerms; }
    public boolean isCompleted() { return completed; }
    public boolean isCancelled() { return cancelled; }

    public void setContractTerms(String contractTerms) {
        this.contractTerms = contractTerms;
    }

    public void completeSale() {
        if (cancelled) {
            throw new PropertySaleException("Нельзя завершить отмененную продажу");
        }
        if (completed) {
            throw new PropertySaleException("Продажа уже завершена");
        }
        this.completed = true;
    }

    public void cancelSale() {
        if (completed) {
            throw new PropertySaleException("Нельзя отменить завершенную продажу");
        }
        if (cancelled) {
            throw new PropertySaleException("Продажа уже отменена");
        }
        this.cancelled = true;
    }

    public boolean isActive() {
        return !completed && !cancelled;
    }

    public boolean canBeCompleted() {
        return isActive();
    }

    public boolean canBeCancelled() {
        return isActive();
    }
}
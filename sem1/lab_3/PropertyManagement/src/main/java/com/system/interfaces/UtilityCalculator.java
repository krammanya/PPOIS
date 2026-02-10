package com.system.interfaces;

import com.system.utilities.Tariff;

public interface UtilityCalculator {
    void setTariff(Tariff tariff);
    Tariff getTariff();
}
package com.example.taxengine.model;

import java.math.BigDecimal;

public class Bracket {
    private final BigDecimal maxIncome;
    private final BigDecimal taxRate;

    public Bracket(BigDecimal maxIncome, BigDecimal taxRate) {
        this.maxIncome = maxIncome;
        this.taxRate = taxRate;
    }

    public BigDecimal getMaxIncome() {
        return maxIncome;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * Builder for input as double.
     */
    public static Bracket build(double maxIncome, double taxRate) {
        return new Bracket(BigDecimal.valueOf(maxIncome), BigDecimal.valueOf(taxRate));
    }
}

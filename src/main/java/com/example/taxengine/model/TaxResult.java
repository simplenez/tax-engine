package com.example.taxengine.model;

import com.example.taxengine.Utils;

import java.math.BigDecimal;

public class TaxResult {
    private final int year;
    private final BigDecimal income;
    private final BigDecimal tax;
    private final String incomeDisplay;
    private final String taxDisplay;

    public TaxResult(int year, BigDecimal income, BigDecimal tax) {
        this.year = year;
        this.income = income;
        this.tax = tax;
        this.incomeDisplay = Utils.formatCurrency(income);
        this.taxDisplay = Utils.formatCurrency(tax);
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public String getIncomeDisplay() {
        return incomeDisplay;
    }

    public String getTaxDisplay() {
        return taxDisplay;
    }
}

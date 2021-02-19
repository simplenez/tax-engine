package com.example.taxengine.model;

import java.math.BigDecimal;
import java.util.Date;

public class Response {
    private BigDecimal income;
    private BigDecimal tax;
    private Date date;

    public Response(BigDecimal income, BigDecimal tax) {
        this.income = income;
        this.tax = tax;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}

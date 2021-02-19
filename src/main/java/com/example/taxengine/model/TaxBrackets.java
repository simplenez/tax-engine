package com.example.taxengine.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaxBrackets {
    // tax rate for the rest of the income beyond the last bracket.
    private final BigDecimal defaultTaxRate;
    // list of brackets sorted by the max income in ascending order.
    private final List<Bracket> brackets;

    public TaxBrackets(double defaultTaxRate, Bracket... bracket) {
        this.defaultTaxRate = BigDecimal.valueOf(defaultTaxRate);
        this.brackets = bracket == null ? Collections.emptyList() : convertArgToSortedList(bracket);
    }

    /**
     * Return a list of brackets sorted by the max income in ascending order.
     */
    private static List<Bracket> convertArgToSortedList(Bracket[] bracket) {
        Arrays.sort(bracket, Comparator.comparing(Bracket::getMaxIncome));
        return Collections.unmodifiableList(Arrays.asList(bracket));
    }

    public List<Bracket> getBrackets() {
        return brackets;
    }

    public BigDecimal getDefaultTaxRate() {
        return defaultTaxRate;
    }
}

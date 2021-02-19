package com.example.taxengine;

import com.example.taxengine.model.Bracket;
import com.example.taxengine.model.TaxBrackets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class TaxCalculatorService {
    private final Map<Integer, TaxBrackets> yearToTaxBrackets;

    @Autowired
    public TaxCalculatorService(Map<Integer, TaxBrackets> yearToTaxBrackets) {
        this.yearToTaxBrackets = yearToTaxBrackets;
    }

    public boolean hasYear(int year) {
        return yearToTaxBrackets.get(year) != null;
    }

    public BigDecimal calculate(int year, BigDecimal income) {
        if (!hasYear(year)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax year not found.");
        }
        if (income.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        }

        BigDecimal bracketStart = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        final TaxBrackets taxBracketsForYear = this.yearToTaxBrackets.get(year);
        for (Bracket bracket : taxBracketsForYear.getBrackets()) {
            if (income.compareTo(bracket.getMaxIncome()) < 0) {
                totalTax = totalTax.add(calcTax(income, bracketStart, bracket.getTaxRate()));
                bracketStart = bracket.getMaxIncome();
                break; // no need to check the next bracket since the bracket were sorted by maxIncome
            } else {
                totalTax = totalTax.add(calcTax(bracket.getMaxIncome(), bracketStart, bracket.getTaxRate()));
                bracketStart = bracket.getMaxIncome();
            }
        }

        if (income.compareTo(bracketStart) > 0) {
            totalTax = totalTax.add(calcTax(income, bracketStart, taxBracketsForYear.getDefaultTaxRate()));
        }
        return totalTax.setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal calcTax(BigDecimal bracketEnd, BigDecimal bracketStart, BigDecimal taxRate) {
        BigDecimal incomeInBracket = bracketEnd.subtract(bracketStart);
        return incomeInBracket.multiply(taxRate);
    }
}

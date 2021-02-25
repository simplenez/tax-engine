package com.example.taxengine;

import com.example.taxengine.model.Bracket;
import com.example.taxengine.model.TaxBrackets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.example.taxengine.Utils.toBigDecimal;
import static com.example.taxengine.model.Bracket.build;
import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorServiceTest {
    private static final int YEAR_2020 = 2020;

    @Test
    void hasYear() {
        TaxCalculatorService tcs = year2020_10k_0p_default_30p();

        assertTrue(tcs.hasYear(YEAR_2020));
        assertFalse(tcs.hasYear(2021));
    }

    @Test
    void calculateInvalidYear() {
        TaxCalculatorService tcs = year2020_10k_0p_default_30p();

        Assertions.assertThrows(ResponseStatusException.class, () -> tcs.calculate(2022, BigDecimal.valueOf(150_000)));
    }

    @Test
    void calculateWithTwoBrackets() {
        TaxCalculatorService tcs = year2020_10k_0p_default_30p();

        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(-1)));
        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(0)));

        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(9_999.99)));
        assertEquals(toBigDecimal(0.01), tcs.calculate(YEAR_2020, BigDecimal.valueOf(10_000.03)));
        assertEquals(toBigDecimal(0.30), tcs.calculate(YEAR_2020, BigDecimal.valueOf(10_001)));
        assertEquals(toBigDecimal(3_000.00), tcs.calculate(YEAR_2020, BigDecimal.valueOf(20_000)));
    }

    @Test
    void calculateDefaultBracket() {
        TaxCalculatorService tcs = createYear2020TCS(0.30);

        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(-1)));
        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(0)));
        assertEquals(toBigDecimal(0.3), tcs.calculate(YEAR_2020, BigDecimal.valueOf(1)));

        assertEquals(toBigDecimal(6_000.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(20_000)));
        assertEquals(toBigDecimal(6_000_000.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(20_000_000)));
    }

    @Test
    void calculateWithMultipleBracketsNotInOrder() {
        TaxCalculatorService tcs = createYear2020TCS(0.30,
                build(20_000, 0.10),
                build(50_000, 0.20),
                build(10_000, 0));

        assertEquals(toBigDecimal(0.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(10_000)));

        assertEquals(toBigDecimal(0.1), tcs.calculate(YEAR_2020, BigDecimal.valueOf(10_001)));
        assertEquals(toBigDecimal(1000.00), tcs.calculate(YEAR_2020, BigDecimal.valueOf(20_000)));

        assertEquals(toBigDecimal(1000.20), tcs.calculate(YEAR_2020, BigDecimal.valueOf(20_001)));
        assertEquals(toBigDecimal(7_000.00), tcs.calculate(YEAR_2020, BigDecimal.valueOf(50_000)));

        assertEquals(toBigDecimal(7_000.30), tcs.calculate(YEAR_2020, BigDecimal.valueOf(50_001)));
    }

    @Test
    void calculateWithDecreasingRate() {
        TaxCalculatorService tcs = createYear2020TCS(0.10,
                build(50_000, 0.555555),
                build(100_000, 0.20)
        );

        assertEquals(toBigDecimal(27_777.75), tcs.calculate(YEAR_2020, BigDecimal.valueOf(50_000)));
        assertEquals(toBigDecimal(37_777.75), tcs.calculate(YEAR_2020, BigDecimal.valueOf(100_000)));
        assertEquals(toBigDecimal(37_777.85), tcs.calculate(YEAR_2020, BigDecimal.valueOf(100_001)));
    }

    @Test
    void multipleYears() {
        TaxBrackets y2020 = new TaxBrackets(0.1, build(100_000, 0));
        TaxBrackets y2019 = new TaxBrackets(0.2875, build(125_000, 0));

        int year_2019 = 2019;
        Map<Integer, TaxBrackets> taxBrackets = new HashMap<>();
        taxBrackets.put(YEAR_2020, y2020);
        taxBrackets.put(year_2019, y2019);
        TaxCalculatorService tcs = new TaxCalculatorService(taxBrackets);

        assertEquals(toBigDecimal(5_000.0), tcs.calculate(YEAR_2020, BigDecimal.valueOf(150_000)));
        assertEquals(toBigDecimal(7_187.50), tcs.calculate(year_2019, BigDecimal.valueOf(150_000)));
    }

    private static TaxCalculatorService createYear2020TCS(double defaultTaxRate, Bracket... brackets) {
        Map<Integer, TaxBrackets> taxBrackets = new HashMap<>();
        taxBrackets.put(YEAR_2020, new TaxBrackets(defaultTaxRate, brackets));
        return new TaxCalculatorService(taxBrackets);
    }

    private static TaxCalculatorService year2020_10k_0p_default_30p() {
        return createYear2020TCS(0.30, build(10_000, 0));
    }

}

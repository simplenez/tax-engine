package com.example.taxengine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    private static final ThreadLocal<NumberFormat> CURRENCY_FORMAT = ThreadLocal.withInitial(() -> NumberFormat.getCurrencyInstance(Locale.US));

    public static String formatCurrency(BigDecimal val) {
        return val == null ? "" : CURRENCY_FORMAT.get().format(val);
    }

    public static BigDecimal toBigDecimal(double val) {
        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_EVEN);
    }
}

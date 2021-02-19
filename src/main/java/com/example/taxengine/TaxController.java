package com.example.taxengine;

import com.example.taxengine.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class TaxController {

    private final TaxCalculatorService taxCalculatorService;

    @Autowired
    public TaxController(TaxCalculatorService taxCalculatorService) {
        this.taxCalculatorService = taxCalculatorService;
    }

    @GetMapping("/tax/{year}/{income}")
    public Response calculateTax(@PathVariable int year, @PathVariable BigDecimal income) {
        BigDecimal tax = taxCalculatorService.calculate(year, income);
        return new Response(income.setScale(2, RoundingMode.HALF_EVEN), tax);
    }

}

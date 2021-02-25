package com.example.taxengine;

import com.example.taxengine.model.TaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET)
    public TaxResult calculateTax(@PathVariable int year, @PathVariable BigDecimal income) {
        BigDecimal tax = taxCalculatorService.calculate(year, income);
        return new TaxResult(year, income.setScale(2, RoundingMode.HALF_EVEN), tax);
    }

}

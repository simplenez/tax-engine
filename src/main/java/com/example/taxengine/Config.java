package com.example.taxengine;

import com.example.taxengine.model.Bracket;
import com.example.taxengine.model.TaxBrackets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.example.taxengine.model.Bracket.build;

@Configuration
public class Config {

    @Bean
    public Map<Integer, TaxBrackets> taxBrackets() {
        Map<Integer, TaxBrackets> taxBrackets = new HashMap<>();
        taxBrackets.put(2020, year2020());
        return taxBrackets;
    }

    private static TaxBrackets year2020() {
        return new TaxBrackets(0.30,
                build(10_000, 0),
                build(20_000, 0.10),
                build(50_000, 0.20));
    }

}

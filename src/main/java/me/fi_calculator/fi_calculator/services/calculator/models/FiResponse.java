package me.fi_calculator.fi_calculator.services.calculator.models;


import java.math.BigDecimal;

public record FiResponse(Deterministic deterministic) {
    public record Deterministic(
            BigDecimal monthlyRealReturn,
            int monthsToFi,
            double yearsToFi,
            Integer fiAge
    ) {
    }
}
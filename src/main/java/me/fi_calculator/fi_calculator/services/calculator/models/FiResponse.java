package me.fi_calculator.fi_calculator.services.calculator.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FiResponse(
        BigDecimal fiTargetToday,
        Deterministic deterministic,
        MonteCarlo monteCarlo
) {
    public record Deterministic(
            BigDecimal monthlyRealReturn,
            int monthsToFi,
            double yearsToFi,
            Integer fiAge
    ) {}

    public record MonteCarlo(
            int runs,
            double reachedFiProbabilityWithinTargetYears,
            Percentiles monthsToFiPercentiles
    ) {}

    public record Percentiles(double p10, double p25, double p50, double p75, double p90) {}
}
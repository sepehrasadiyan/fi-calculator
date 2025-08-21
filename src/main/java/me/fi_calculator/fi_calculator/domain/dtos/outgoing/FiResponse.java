package me.fi_calculator.fi_calculator.domain.dtos.outgoing;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        description = "Unified FI result: deterministic path plus (optionally) Monte Carlo statistics.",
        example = """
                {
                  "fiTargetToday": 750000.0000,
                  "deterministic": {
                    "monthlyRealReturn": 0.0031,
                    "monthsToFi": 162,
                    "yearsToFi": 13.5,
                    "fiAge": 44
                  },
                  "monteCarlo": {
                    "runs": 10000,
                    "reachedFiProbabilityWithinTargetYears": 0.62,
                    "monthsToFiPercentiles": {
                      "p10": 120.0,
                      "p25": 144.0,
                      "p50": 168.0,
                      "p75": 204.0,
                      "p90": 240.0
                    }
                  }
                }
                """
)
public record FiResponse(
        @Schema(description = "Target wealth (today's currency) to sustain expenses at the given withdrawal rate.", example = "750000.0000")
        BigDecimal fiTargetToday,

        @Schema(description = "Deterministic compounding path result.")
        Deterministic deterministic,

        @Schema(description = "Monte Carlo statistics (present only if simulation.runMonteCarlo = true).")
        MonteCarlo monteCarlo
) {
    @Schema(description = "Deterministic path to FI.")
    public record Deterministic(
            @Schema(description = "Monthly real (inflation-adjusted) return used for compounding.", example = "0.0031")
            BigDecimal monthlyRealReturn,

            @Schema(description = "Months needed to reach FI target deterministically.", example = "162")
            int monthsToFi,

            @Schema(description = "Years to FI (monthsToFi / 12).", example = "13.5")
            double yearsToFi,

            @Schema(description = "Estimated age at FI (if currentAge provided).", example = "44")
            Integer fiAge
    ) {
    }

    @Schema(description = "Monte Carlo statistics across many simulated paths.")
    public record MonteCarlo(
            @Schema(description = "Number of simulation runs.", example = "10000")
            int runs,

            @Schema(description = "Probability of reaching FI within `simulation.targetYears`.", example = "0.62")
            double reachedFiProbabilityWithinTargetYears,

            @Schema(description = "Percentiles for months-to-FI distribution across runs.")
            Percentiles monthsToFiPercentiles
    ) {
    }

    @Schema(description = "Percentiles for months-to-FI.")
    public record Percentiles(
            @Schema(description = "10th percentile.", example = "120.0") double p10,
            @Schema(description = "25th percentile.", example = "144.0") double p25,
            @Schema(description = "Median.", example = "168.0") double p50,
            @Schema(description = "75th percentile.", example = "204.0") double p75,
            @Schema(description = "90th percentile.", example = "240.0") double p90
    ) {
    }
}

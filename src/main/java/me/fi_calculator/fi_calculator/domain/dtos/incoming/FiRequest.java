package me.fi_calculator.fi_calculator.domain.dtos.incoming;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Input payload for FI calculation: user's financial profile, " +
                "portfolio assumptions, and simulation settings.",
        example = """
                {
                  "profile": {
                    "currentAge": 30,
                    "currentSavings": 15000.00,
                    "currentInvestments": 35000.00,
                    "retirementAccounts": 20000.00,
                    "monthlyNetIncome": 4500.00,
                    "monthlyExpenses": 2500.00,
                    "monthlyContributions": 1000.00
                  },
                  "assumptions": {
                    "annualNominalReturnStocks": 0.07,
                    "annualNominalReturnBonds": 0.03,
                    "stockAllocation": 0.8,
                    "bondAllocation": 0.2,
                    "stocksBondsCorrelation": 0.2,
                    "annualInflationMean": 0.02,
                    "annualInflationStd": 0.01,
                    "withdrawalRate": 0.04
                  },
                  "simulation": {
                    "runMonteCarlo": true,
                    "simulations": 10000,
                    "maxYears": 60,
                    "targetYears": 25,
                    "parallel": true
                  }
                }
                """
)
public record FiRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Current financial profile.")
        FinancialProfile profile,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Portfolio & macro assumptions.")
        PortfolioAssumptions assumptions,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Simulation switches and bounds.")
        SimulationSettings simulation
) {
    @Schema(description = "Current financial profile.")
    public record FinancialProfile(
            @Min(0) @Max(120)
            @Schema(description = "Current age in years (optional).", example = "30")
            Integer currentAge,

            @DecimalMin("0")
            @Schema(description = "Liquid savings (cash) in today's currency.", example = "15000.00")
            BigDecimal currentSavings,

            @DecimalMin("0")
            @Schema(description = "Taxable investments (brokerage) in today's currency.", example = "35000.00")
            BigDecimal currentInvestments,

            @DecimalMin("0")
            @Schema(description = "Retirement/tax-advantaged accounts in today's currency.", example = "20000.00")
            BigDecimal retirementAccounts,

            @DecimalMin("0")
            @Schema(description = "Monthly net income (after tax).", example = "4500.00")
            BigDecimal monthlyNetIncome,

            @DecimalMin("0")
            @Schema(description = "Monthly living expenses.", example = "2500.00")
            BigDecimal monthlyExpenses,

            @DecimalMin("0")
            @Schema(description = "Monthly contributions to investments. If omitted/zero, defaults to income " +
                    "- expenses.", example = "1000.00")
            BigDecimal monthlyContributions
    ) {
    }

    @Schema(description = "Portfolio assumptions (used to project real, inflation-adjusted returns).")
    public record PortfolioAssumptions(
            @DecimalMin("0")
            @Schema(description = "Annual nominal return for stocks (0.07 = 7%).", example = "0.07")
            BigDecimal annualNominalReturnStocks,

            @DecimalMin("0")
            @Schema(description = "Annual nominal return for bonds.", example = "0.03")
            BigDecimal annualNominalReturnBonds,

            @DecimalMin("0") @DecimalMax("1")
            @Schema(description = "Allocation to stocks (0..1).", example = "0.8")
            BigDecimal stockAllocation,

            @DecimalMin("0") @DecimalMax("1")
            @Schema(description = "Allocation to bonds (0..1). Must sum with stocks to 1.0.", example = "0.2")
            BigDecimal bondAllocation,

            @DecimalMin("-1") @DecimalMax("1")
            @Schema(description = "Correlation between stock & bond returns (−1..1).", example = "0.2")
            BigDecimal stocksBondsCorrelation,

            @DecimalMin("0")
            @Schema(description = "Expected annual inflation mean.", example = "0.02")
            BigDecimal annualInflationMean,

            @DecimalMin("0")
            @Schema(description = "Annual inflation volatility (std dev).", example = "0.01")
            BigDecimal annualInflationStd,

            @DecimalMin("0.01") @DecimalMax("0.10")
            @Schema(description = "Safe withdrawal rate (e.g., 0.04 for 4%). FI target = annual expenses / rate.", example = "0.04")
            BigDecimal withdrawalRate
    ) {
        public void validate() {
            if (stockAllocation.add(bondAllocation).subtract(BigDecimal.ONE).abs().doubleValue() > 1e-9) {
                throw new IllegalArgumentException("stockAllocation + bondAllocation must equal 1.0");
            }
        }
    }

    @Schema(description = "Simulation options.")
    public record SimulationSettings(
            @Schema(description = "If true, run Monte Carlo on top of the deterministic projection.", example = "true")
            boolean runMonteCarlo,

            @Min(100) @Max(200000)
            @Schema(description = "Number of Monte Carlo runs.", example = "10000")
            int simulations,

            @Min(1) @Max(100)
            @Schema(description = "Maximum horizon in years.", example = "60")
            int maxYears,

            @Min(1) @Max(100)
            @Schema(description = "Target horizon in years for success probability reporting.", example = "25")
            int targetYears,

            @Schema(description = "If true and CPU allows, run simulations in parallel.", example = "true")
            boolean parallel
    ) {
    }
}


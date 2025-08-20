package me.fi_calculator.fi_calculator.domain.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FiRequest(
        @NotNull(message = "Financial profile is required")
        FinancialProfile profile,
        @NotNull(message = "Portfolio assumptions are required")
        PortfolioAssumptions assumptions,
        @NotNull(message = "Simulation settings are required")
        SimulationSettings simulation
) {
    public record FinancialProfile(
            @NotNull(message = "Current age is required")
            @Min(value = 18, message = "Must be at least 18 years old")
            @Max(value = 100, message = "Maximum age is 100")
            Integer currentAge,

            @DecimalMin(value = "0", message = "Savings cannot be negative")
            BigDecimal currentSavings,

            @DecimalMin(value = "0", message = "Investments cannot be negative")
            BigDecimal currentInvestments,

            @DecimalMin(value = "0", message = "Retirement accounts cannot be negative")
            BigDecimal retirementAccounts,

            @NotNull(message = "Monthly net income is required")
            @DecimalMin(value = "0", message = "Income cannot be negative")
            BigDecimal monthlyNetIncome,

            @NotNull(message = "Monthly expenses are required")
            @DecimalMin(value = "0", message = "Expenses cannot be negative")
            BigDecimal monthlyExpenses,

            @DecimalMin(value = "0", message = "Contributions cannot be negative")
            BigDecimal monthlyContributions
    ) {
        public FinancialProfile {
            currentSavings = currentSavings != null ? currentSavings : BigDecimal.ZERO;
            currentInvestments = currentInvestments != null ? currentInvestments : BigDecimal.ZERO;
            retirementAccounts = retirementAccounts != null ? retirementAccounts : BigDecimal.ZERO;
            monthlyContributions = monthlyContributions != null ? monthlyContributions : BigDecimal.ZERO;

            if (monthlyExpenses.compareTo(monthlyNetIncome) > 0) {
                throw new IllegalArgumentException("Monthly expenses cannot exceed net income");
            }
        }
    }

    public record PortfolioAssumptions(
            @DecimalMin(value = "0", message = "Stock return must be positive")
            BigDecimal annualNominalReturnStocks,

            @DecimalMin(value = "0", message = "Bond return must be positive")
            BigDecimal annualNominalReturnBonds,

            @DecimalMin(value = "0", message = "Stock allocation cannot be negative")
            @DecimalMax(value = "1", message = "Stock allocation cannot exceed 100%")
            BigDecimal stockAllocation,

            @DecimalMin(value = "0", message = "Bond allocation cannot be negative")
            @DecimalMax(value = "1", message = "Bond allocation cannot exceed 100%")
            BigDecimal bondAllocation,

            @DecimalMin(value = "-1", message = "Correlation must be >= -1")
            @DecimalMax(value = "1", message = "Correlation must be <= 1")
            BigDecimal stocksBondsCorrelation,

            @DecimalMin(value = "0", message = "Inflation must be positive")
            BigDecimal annualInflationMean,

            @DecimalMin(value = "0", message = "Inflation std dev must be positive")
            BigDecimal annualInflationStd,

            @DecimalMin(value = "0.01", message = "Withdrawal rate must be at least 1%")
            @DecimalMax(value = "0.10", message = "Withdrawal rate cannot exceed 10%")
            BigDecimal withdrawalRate
    ) {
        public PortfolioAssumptions {
            annualNominalReturnStocks = annualNominalReturnStocks != null ? annualNominalReturnStocks : new BigDecimal("0.07");
            annualNominalReturnBonds = annualNominalReturnBonds != null ? annualNominalReturnBonds : new BigDecimal("0.03");
            stockAllocation = stockAllocation != null ? stockAllocation : new BigDecimal("0.6");
            bondAllocation = bondAllocation != null ? bondAllocation : new BigDecimal("0.4");
            stocksBondsCorrelation = stocksBondsCorrelation != null ? stocksBondsCorrelation : new BigDecimal("0.2");
            annualInflationMean = annualInflationMean != null ? annualInflationMean : new BigDecimal("0.025");
            annualInflationStd = annualInflationStd != null ? annualInflationStd : new BigDecimal("0.01");
            withdrawalRate = withdrawalRate != null ? withdrawalRate : new BigDecimal("0.04");

            BigDecimal totalAllocation = stockAllocation.add(bondAllocation);
            if (totalAllocation.compareTo(BigDecimal.ONE) != 0) {
                throw new IllegalArgumentException("Stock and bond allocation must sum to 100%");
            }
        }
    }

    public record SimulationSettings(
            boolean runMonteCarlo,

            @Min(value = 100, message = "Minimum 100 simulations required")
            @Max(value = 100000, message = "Maximum 100,000 simulations allowed")
            Integer simulations,

            @Min(value = 1, message = "Minimum 1 year required")
            @Max(value = 100, message = "Maximum 100 years allowed")
            Integer maxYears,

            @Min(value = 1, message = "Minimum 1 target year required")
            @Max(value = 100, message = "Maximum 100 target years allowed")
            Integer targetYears,

            boolean parallel
    ) {
        public SimulationSettings {
            simulations = simulations != null ? simulations : 10000;
            maxYears = maxYears != null ? maxYears : 50;
            targetYears = targetYears != null ? targetYears : 30;

            if (targetYears > maxYears) {
                throw new IllegalArgumentException("Target years cannot exceed maximum simulation years");
            }
        }
    }
}

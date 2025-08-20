package me.fi_calculator.fi_calculator.domain.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FiRequest(
        @NotNull FinancialProfile profile
) {
    public record FinancialProfile(
            @Min(0) @Max(120) Integer currentAge,
            @DecimalMin("0") BigDecimal currentSavings,
            @DecimalMin("0") BigDecimal currentInvestments,
            @DecimalMin("0") BigDecimal retirementAccounts,
            @DecimalMin("0") BigDecimal monthlyNetIncome,
            @DecimalMin("0") BigDecimal monthlyExpenses
    ) {
    }
}

package me.fi_calculator.fi_calculator.services.calculator.models.dto;

import me.fi_calculator.fi_calculator.domain.dtos.FiRequest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public record PortfolioAssumptions(
        BigDecimal muStocksAnnual,
        BigDecimal muBondsAnnual,
        BigDecimal allocStocks,
        BigDecimal allocBonds,
        BigDecimal corr,
        BigDecimal inflationAnnualMean,
        BigDecimal inflationAnnualStd
) {
    public static final MathContext MC = new MathContext(12, RoundingMode.HALF_UP);

    public static PortfolioAssumptions from(FiRequest.PortfolioAssumptions a) {
        return new PortfolioAssumptions(
                a.annualNominalReturnStocks(),
                a.annualNominalReturnBonds(),
                a.stockAllocation(),
                a.bondAllocation(),
                a.stocksBondsCorrelation(),
                a.annualInflationMean(),
                a.annualInflationStd()
        );
    }

    public BigDecimal portfolioNominalAnnualMean() {
        return allocStocks.multiply(muStocksAnnual, MC).add(allocBonds.multiply(muBondsAnnual, MC), MC);
    }

    public double monthlyRealMeanDouble() {
        double muN = portfolioNominalAnnualMean().doubleValue();
        double i = inflationAnnualMean.doubleValue();
        double realAnnual = (1.0 + muN) / (1.0 + i) - 1.0;
        return Math.pow(1.0 + realAnnual, 1.0 / 12.0) - 1.0;
    }

}

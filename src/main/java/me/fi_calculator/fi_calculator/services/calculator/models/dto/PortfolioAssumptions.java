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

    public double monthlyRealStdDouble() {
        double sigmaS = 0.18; double sigmaB = 0.07; double as = allocStocks.doubleValue(); double ab = allocBonds.doubleValue();
        double corrD = corr.doubleValue();
        double var = as*as*sigmaS*sigmaS + ab*ab*sigmaB*sigmaB + 2*as*ab*sigmaS*sigmaB*corrD;
        double sigmaAnnual = Math.sqrt(Math.max(var, 1e-12));
        double sigmaMonthlyNominal = sigmaAnnual / Math.sqrt(12.0);
        double sigmaInflMonthly = inflationAnnualStd.doubleValue() / Math.sqrt(12.0);
        return Math.hypot(sigmaMonthlyNominal, sigmaInflMonthly);
    }

}

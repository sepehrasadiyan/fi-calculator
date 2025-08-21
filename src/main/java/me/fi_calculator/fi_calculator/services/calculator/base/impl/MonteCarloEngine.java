package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.services.calculator.models.dto.PortfolioAssumptions;
import me.fi_calculator.fi_calculator.services.calculator.models.dto.SimulationResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MonteCarloEngine {
    private static final MathContext MC = new MathContext(12, RoundingMode.HALF_UP);

    private final PortfolioAssumptions assumptions;
    private final int runs;
    private final int maxMonths;
    private final boolean parallel;

    public MonteCarloEngine(PortfolioAssumptions assumptions, int runs, int maxYears, boolean parallel) {
        this.assumptions = assumptions;
        this.runs = runs;
        this.maxMonths = maxYears * 12;
        this.parallel = parallel;
    }

    public SimulationResult simulateTimeToTarget(BigDecimal startWealth, BigDecimal targetWealth, BigDecimal monthlyContribution) {
        var stream = (parallel ? IntStream.range(0, runs).parallel() : IntStream.range(0, runs));
        final double mu = assumptions.monthlyRealMeanDouble();
        final double sigma = Math.max(assumptions.monthlyRealStdDouble(), 1e-6);

        int[] months = stream.map(run -> {
            Random rnd = ThreadLocalRandom.current();
            BigDecimal w = startWealth;
            for (int m = 0; m < maxMonths; m++) {
                if (w.compareTo(targetWealth) >= 0) return m;
                double shock = rnd.nextGaussian() * sigma + mu;
                BigDecimal factor = BigDecimal.ONE.add(BigDecimal.valueOf(shock), MC);
                w = w.add(monthlyContribution, MC).multiply(factor, MC);
                if (w.scale() > 8) w = w.setScale(8, RoundingMode.HALF_UP);
            }
            return -1;
        }).toArray();

        return new SimulationResult(months);
    }
}

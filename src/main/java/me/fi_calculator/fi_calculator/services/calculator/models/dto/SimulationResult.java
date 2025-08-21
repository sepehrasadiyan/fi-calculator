package me.fi_calculator.fi_calculator.services.calculator.models.dto;

import java.util.Arrays;

public class SimulationResult {
    private final int[] monthsToFi;

    public SimulationResult(int[] monthsToFi) { this.monthsToFi = monthsToFi; }

    public int runs() { return monthsToFi.length; }

    public PercentileStats percentiles() {
        int[] reached = Arrays.stream(monthsToFi).filter(m -> m >= 0).sorted().toArray();
        if (reached.length == 0) return new PercentileStats(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        return new PercentileStats(p(reached,10), p(reached,25), p(reached,50), p(reached,75), p(reached,90));
    }

    public double probabilityWithinYears(int years) {
        int limit = years * 12;
        long count = Arrays.stream(monthsToFi).filter(m -> m >= 0 && m <= limit).count();
        return count / (double) monthsToFi.length;
    }

    private static double p(int[] s, int p) {
        double rank = (p/100.0)*(s.length-1);
        int i=(int)Math.floor(rank), j=Math.min(i+1, s.length-1);
        double frac = rank - i; return s[i]*(1-frac) + s[j]*frac;
    }
}

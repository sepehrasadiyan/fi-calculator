package me.fi_calculator.fi_calculator.services.calculator.base;

public final class MonteCarloExtras implements EngineExtras {

    private final int totalSimulations;
    private final long seed;

    public MonteCarloExtras(int totalSimulations, long seed) {
        this.totalSimulations = totalSimulations;
        this.seed = seed;
    }

    public int getTotalSimulations() {
        return totalSimulations;
    }

    public long getSeed() {
        return seed;
    }
}

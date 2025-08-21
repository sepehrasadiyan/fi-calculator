package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.UserService;
import me.fi_calculator.fi_calculator.services.calculator.base.MonteCarloExtras;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import me.fi_calculator.fi_calculator.services.calculator.models.FiResponse;
import me.fi_calculator.fi_calculator.services.calculator.models.dto.PortfolioAssumptions;
import me.fi_calculator.fi_calculator.services.calculator.models.dto.SimulationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


@Component
public class MonteCarloFiEngine implements FiEngine<MonteCarloExtras> {

    private static final Logger log = LoggerFactory.getLogger(MonteCarloFiEngine.class);
    private static final MathContext MC = new MathContext(12, RoundingMode.HALF_UP);


    private final UserService users;

    public MonteCarloFiEngine(UserService users) {
        this.users = users;
    }

    @Override
    public EngineId id() {
        return EngineId.MONTE_CARLO;
    }

    @Override
    public FiEngineResult<MonteCarloExtras> calculate(FiCalcCommand req) {
        if (!users.validForEnginId(req.engine())) {
            //You can implement more thing like subscription here
            //For now just this :)
            log.warn("Invalid engine id {}", req.engine());
        }
        log.debug("Calculating FI={} for userMail={}", req.engine(), req.userEmail());

        FiResponse response = calc(req);

        MonteCarloExtras extras = new MonteCarloExtras(
                req.payload().simulation().simulations(),
                System.currentTimeMillis()
        );

        return new FiEngineResult<>(extras, response);
    }

    private FiResponse calc(FiCalcCommand req) {

        // Three main part of request More detail IN README.md :)
        var p = req.payload().profile();
        var a = req.payload().assumptions();
        var s = req.payload().simulation();

        BigDecimal monthlySavings = p.monthlyContributions() != null && p.monthlyContributions().compareTo(BigDecimal.ZERO) > 0
                ? p.monthlyContributions()
                : p.monthlyNetIncome().subtract(p.monthlyExpenses(), MC).max(BigDecimal.ZERO);

        BigDecimal totalStartWealth = p.currentSavings().add(p.currentInvestments(), MC).add(p.retirementAccounts(), MC);
        BigDecimal annualExpenses = p.monthlyExpenses().multiply(BigDecimal.valueOf(12L), MC);
        BigDecimal fiTarget = annualExpenses.divide(a.withdrawalRate(), MC);
        PortfolioAssumptions pa = PortfolioAssumptions.from(a);
        BigDecimal monthlyRealMeanBD = BigDecimal.valueOf(pa.monthlyRealMeanDouble());
        int monthsToFi = deterministicMonthsToTarget(totalStartWealth, fiTarget, monthlySavings, monthlyRealMeanBD);
        double yearsToFi = monthsToFi / 12.0;
        Integer fiAge = p.currentAge() != null ? (int)Math.round(p.currentAge() + yearsToFi) : null;
        var deterministic = new FiResponse.Deterministic(monthlyRealMeanBD, monthsToFi, yearsToFi, fiAge);
        FiResponse.MonteCarlo mcResp = null;
        if (s.runMonteCarlo()) {
            MonteCarloEngine engine = new MonteCarloEngine(pa, s.simulations(), s.maxYears(), s.parallel());
            SimulationResult r = engine.simulateTimeToTarget(totalStartWealth, fiTarget, monthlySavings);
            var perc = r.percentiles();
            var respPerc = new FiResponse.Percentiles(perc.p10(), perc.p25(), perc.p50(), perc.p75(), perc.p90());
            mcResp = new FiResponse.MonteCarlo(r.runs(), r.probabilityWithinYears(s.targetYears()), respPerc);
        }

        // You can save response As your wish

        return new FiResponse(fiTarget, deterministic, mcResp);
    }


    static int deterministicMonthsToTarget(BigDecimal start, BigDecimal target, BigDecimal monthlyContribution, BigDecimal monthlyRealReturn) {
        if (start.compareTo(target) >= 0) return 0;
        BigDecimal wealth = start;
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRealReturn, MC);
        for (int m = 1; m <= 1200; m++) {
            wealth = wealth.add(monthlyContribution, MC).multiply(onePlusR, MC);
            if (wealth.scale() > 8) wealth = wealth.setScale(8, java.math.RoundingMode.HALF_UP);
            if (wealth.compareTo(target) >= 0) return m;
        }
        return Integer.MAX_VALUE;
    }
}

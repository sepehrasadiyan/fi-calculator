package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.calculator.base.MonteCarloExtras;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import org.springframework.stereotype.Component;

@Component
public class MonteCarloFiEngine implements FiEngine<MonteCarloExtras> {

    @Override
    public EngineId id() {
        return EngineId.MONTE_CARLO;
    }

    @Override
    public FiEngineResult<MonteCarloExtras> calculate(FiCalcCommand req) {
        return null;
    }
}

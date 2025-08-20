package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.dtos.FiRequest;
import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.calculator.base.MonteCarloExtras;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import org.springframework.stereotype.Component;

@Component
public class MonteCarloFiEngine implements FiEngine<MonteCarloExtras> {


    @Override
    public EngineId id() {
        return null;
    }

    @Override
    public FiEngineResult<MonteCarloExtras> calculate(FiRequest req) {
        return null;
    }
}

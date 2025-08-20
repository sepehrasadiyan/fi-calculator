package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.calculator.base.EngineExtras;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;

public interface FiEngine<E extends EngineExtras> {
    EngineId id();

    FiEngineResult<E> calculate(FiCalcCommand req);
}

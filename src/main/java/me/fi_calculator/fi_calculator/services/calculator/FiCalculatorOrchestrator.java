package me.fi_calculator.fi_calculator.services.calculator;

import me.fi_calculator.fi_calculator.services.calculator.base.impl.FiEngine;
import me.fi_calculator.fi_calculator.services.calculator.base.impl.FiEngineRegistry;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import org.springframework.stereotype.Service;

@Service
public class FiCalculatorOrchestrator {

    private final FiEngineRegistry engines;

    public FiCalculatorOrchestrator(FiEngineRegistry engines) {
        this.engines = engines;
    }

    public void execute(FiCalcCommand cmd) {
        // You can add more future before every engine. :)
        FiEngine<?> engine = engines.get(cmd.engine());
        FiEngineResult<?> engineResult = engine.calculate(cmd);
    }
}

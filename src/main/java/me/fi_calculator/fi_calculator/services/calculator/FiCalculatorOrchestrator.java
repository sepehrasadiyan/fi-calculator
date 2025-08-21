package me.fi_calculator.fi_calculator.services.calculator;

import me.fi_calculator.fi_calculator.services.calculator.base.EngineExtras;
import me.fi_calculator.fi_calculator.services.calculator.base.impl.FiEngine;
import me.fi_calculator.fi_calculator.services.calculator.base.impl.FiEngineRegistry;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FiCalculatorOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(FiCalculatorOrchestrator.class);

    private final FiEngineRegistry engines;

    public FiCalculatorOrchestrator(FiEngineRegistry engines) {
        this.engines = engines;
    }

    public <E extends EngineExtras> FiEngineResult<E> execute(FiCalcCommand cmd) {
        @SuppressWarnings("unchecked")
        FiEngine<E> engine = (FiEngine<E>) engines.get(cmd.engine());
        log.debug("Executing engine={} for user={}", engine.id(), cmd.userEmail());
        return engine.calculate(cmd);
    }
}

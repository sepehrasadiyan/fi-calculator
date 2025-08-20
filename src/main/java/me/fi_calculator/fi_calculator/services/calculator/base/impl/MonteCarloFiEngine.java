package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.UserService;
import me.fi_calculator.fi_calculator.services.calculator.base.MonteCarloExtras;
import me.fi_calculator.fi_calculator.services.calculator.models.FiCalcCommand;
import me.fi_calculator.fi_calculator.services.calculator.models.FiEngineResult;
import me.fi_calculator.fi_calculator.services.calculator.models.FiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MonteCarloFiEngine implements FiEngine<MonteCarloExtras> {

    private static final Logger log = LoggerFactory.getLogger(MonteCarloFiEngine.class);

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

        return new FiEngineResult<>(null, calc(req));
    }

    private FiResponse calc(FiCalcCommand req) {
        return new FiResponse();
    }
}

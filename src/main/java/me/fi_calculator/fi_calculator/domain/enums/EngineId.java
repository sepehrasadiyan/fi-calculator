package me.fi_calculator.fi_calculator.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Engine selector. Extendable with deterministic/hybrid engines in the future.")
public enum EngineId {
    @Schema(description = "Stochastic engine that simulates random paths around the expected real return.")
    MONTE_CARLO,
    DETERMINISTIC
}

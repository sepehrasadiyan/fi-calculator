package me.fi_calculator.fi_calculator.services.calculator.models;

import me.fi_calculator.fi_calculator.domain.dtos.FiRequest;
import me.fi_calculator.fi_calculator.domain.enums.EngineId;

import java.util.Objects;

public record FiCalcCommand(
        EngineId engine,
        FiRequest payload
) {
    public FiCalcCommand {
        Objects.requireNonNull(payload, "Payload cannot be null");
    }
}
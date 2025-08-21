package me.fi_calculator.fi_calculator.services.calculator.models;

import me.fi_calculator.fi_calculator.domain.dtos.outgoing.FiResponse;
import me.fi_calculator.fi_calculator.services.calculator.base.EngineExtras;

public record FiEngineResult<E extends EngineExtras>(E extras, FiResponse response) {
}

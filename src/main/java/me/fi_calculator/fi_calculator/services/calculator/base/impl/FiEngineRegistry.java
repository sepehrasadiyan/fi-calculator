package me.fi_calculator.fi_calculator.services.calculator.base.impl;

import me.fi_calculator.fi_calculator.domain.enums.EngineId;
import me.fi_calculator.fi_calculator.services.calculator.base.EngineExtras;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class FiEngineRegistry {
    private final Map<EngineId, FiEngine<?>> byId = new EnumMap<>(EngineId.class);

    public FiEngineRegistry(List<FiEngine<?>> engines) {
        for (FiEngine<?> e : engines) byId.put(e.id(), e);
    }

    @SuppressWarnings("unchecked")
    public <E extends EngineExtras> FiEngine<E> get(EngineId id) {
        var e = byId.get(id);
        if (e == null) throw new IllegalArgumentException("Not Supported engine: " + id);
        return (FiEngine<E>) e;
    }
}

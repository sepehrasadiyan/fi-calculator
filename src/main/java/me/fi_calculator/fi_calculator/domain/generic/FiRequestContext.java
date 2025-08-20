package me.fi_calculator.fi_calculator.domain.generic;

import java.util.UUID;

public record FiRequestContext(
        String email,
        UUID requestId,
        long startedAtMillis
) {
    public FiRequestContext(String email) {
        this(email, UUID.randomUUID(), System.currentTimeMillis());
    }
}


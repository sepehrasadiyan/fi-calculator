package me.fi_calculator.fi_calculator.domain.generic;

import lombok.Getter;
import me.fi_calculator.fi_calculator.domain.UserEntity;

import java.util.UUID;

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


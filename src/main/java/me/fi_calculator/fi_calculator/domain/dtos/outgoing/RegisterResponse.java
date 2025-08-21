package me.fi_calculator.fi_calculator.domain.dtos.outgoing;

import java.util.Set;
import java.util.UUID;

public record RegisterResponse(UUID id, String email, Set<String> roles) {
}

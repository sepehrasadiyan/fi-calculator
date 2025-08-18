package me.fi_calculator.fi_calculator.domain.dtos;

import java.util.List;
import java.util.UUID;

public record RegisterResponse(UUID id, String email, List<String> roles) {
}

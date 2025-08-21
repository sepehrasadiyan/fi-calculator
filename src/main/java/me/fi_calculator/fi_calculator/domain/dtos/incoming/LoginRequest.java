package me.fi_calculator.fi_calculator.domain.dtos.incoming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        String email,
        @NotBlank(message = "Password is required")
        String password
) {}

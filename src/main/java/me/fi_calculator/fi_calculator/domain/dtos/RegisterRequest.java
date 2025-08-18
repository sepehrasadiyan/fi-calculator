package me.fi_calculator.fi_calculator.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank @Email String email, @NotBlank String password) {
}
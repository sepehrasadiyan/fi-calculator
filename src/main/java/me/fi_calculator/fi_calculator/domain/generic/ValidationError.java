package me.fi_calculator.fi_calculator.domain.generic;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationError(
        String field,
        String message,
        Object rejectedValue,
        String code
) {}

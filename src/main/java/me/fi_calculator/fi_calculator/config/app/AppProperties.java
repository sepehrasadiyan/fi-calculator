package me.fi_calculator.fi_calculator.config.app;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        @Valid Security security
) {
    public record Security(
            @NotEmpty(message = "Default user roles cannot be empty")
            Set<@Pattern(regexp = "^[A-Z_][A-Z0-9_]*$",
                    message = "Role codes must be UPPER_SNAKE_CASE") String> defaultUserRoles
    ) {
    }
}

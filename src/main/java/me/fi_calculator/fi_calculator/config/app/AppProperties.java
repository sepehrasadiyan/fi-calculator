package me.fi_calculator.fi_calculator.config.app;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        @Valid Security security,
        Jwt jwt,
        CookieAccess cookieAccess
) {
    public record Security(
            @NotEmpty(message = "Default user roles cannot be empty")
            Set<@Pattern(regexp = "^[A-Z_][A-Z0-9_]*$",
                    message = "Role codes must be UPPER_SNAKE_CASE") String> defaultUserRoles
    ) {
    }

    public record Jwt(
            @NotBlank(message = "app.jwt.secret-base64 must be set (Base64-encoded key)")
            String secretBase64,

            @Min(value = 1, message = "app.jwt.access-exp-min must be >=1")
            int accessExpMin,

            @Min(value = 0, message = "app.jwt.clock-skew-sec must be >=0")
            int clockSkewSec,

            String issuer,
            String audience
    ) {
    }

    public record CookieAccess(
            @NotBlank String name,
            @NotBlank String path,
            boolean httpOnly,
            boolean secure,
            @Pattern(regexp = "Strict|Lax|None", message = "sameSite must be Strict|Lax|None")
            String sameSite,
            int maxAgeMin
    ) {
    }

}

package me.fi_calculator.fi_calculator.config.app;

import lombok.Getter;

import java.util.Set;

@Getter
public final class AppSettings {

    private final Set<String> defaultUserRoles;
    private final String jwtSecretBase64;
    private final int jwtAccessExpMinutes;
    private final int jwtClockSkewSeconds;
    private final String jwtIssuer;
    private final String jwtAudience;
    private final String cookieName;
    private final String cookiePath;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieSameSite;
    private final int cookieMaxAgeMinutes;

    public AppSettings(AppProperties p) {
        this.defaultUserRoles = Set.copyOf(p.security().defaultUserRoles());
        this.jwtSecretBase64 = p.jwt().secretBase64();
        this.jwtAccessExpMinutes = p.jwt().accessExpMin();
        this.jwtClockSkewSeconds = p.jwt().clockSkewSec();
        this.jwtIssuer = p.jwt().issuer();
        this.jwtAudience = p.jwt().audience();

        this.cookieName = p.cookieAccess().name();
        this.cookiePath = p.cookieAccess().path();
        this.cookieHttpOnly = p.cookieAccess().httpOnly();
        this.cookieSecure = p.cookieAccess().secure();
        this.cookieSameSite = p.cookieAccess().sameSite();
        this.cookieMaxAgeMinutes = p.cookieAccess().maxAgeMin();
    }

}

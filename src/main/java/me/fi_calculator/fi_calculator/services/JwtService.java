package me.fi_calculator.fi_calculator.services;

import me.fi_calculator.fi_calculator.config.app.AppSettings;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;


import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {

    private final AppSettings settings;
    private volatile Key signingKey;

    public JwtService(AppSettings settings) {
        this.settings = settings;
    }

    public String generateAccess(String subjectEmail, UUID userId, Set<String> roleCodes) {
        Key key = key();
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(settings.getJwtAccessExpMinutes() * 60L);

        JwtBuilder b = Jwts.builder()
                .setSubject(subjectEmail)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("uid", userId.toString())
                .claim("roles", roleCodes == null ? List.of() : roleCodes)
                .signWith(key, SignatureAlgorithm.HS256);

        if (settings.getJwtIssuer() != null && !settings.getJwtIssuer().isBlank()) {
            b.setIssuer(settings.getJwtIssuer());
        }
        if (settings.getJwtAudience() != null && !settings.getJwtAudience().isBlank()) {
            b.setAudience(settings.getJwtAudience());
        }

        return b.compact();
    }

    private Key key() {
        Key k = signingKey;
        if (k == null) {
            synchronized (this) {
                k = signingKey;
                if (k == null) {
                    byte[] secret = decodeBase64(settings.getJwtSecretBase64());
                    if (secret.length < 32) {
                        throw new IllegalStateException("JWT secret too short: need >= 256 bits Base64");
                    }
                    signingKey = k = Keys.hmacShaKeyFor(secret);
                }
            }
        }
        return k;
    }

    private static byte[] decodeBase64(String s) {
        try {
            return Decoders.BASE64.decode(s);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Base64 in app.jwt.secret-base64", e);
        }
    }
}

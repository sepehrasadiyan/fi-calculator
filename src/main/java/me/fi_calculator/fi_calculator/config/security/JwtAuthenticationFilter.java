package me.fi_calculator.fi_calculator.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.fi_calculator.fi_calculator.config.app.ShareContext;
import me.fi_calculator.fi_calculator.domain.generic.FiRequestContext;
import me.fi_calculator.fi_calculator.services.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String ACCESS_COOKIE_NAME = "ACCESS_TOKEN";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String token = extractFromCookie(req);
        if (token == null) {
            token = extractFromAuthorization(req);
        }

        if (token != null) {
            try {
                Claims c = jwtService.parse(token).getBody();
                String email = c.getSubject();
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) c.get("roles");
                if (roles == null) roles = List.of();

                List<SimpleGrantedAuthority> auths = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .toList();

                var auth = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);
                ShareContext.set(new FiRequestContext(email));
            } catch (Exception ignore) {
                // unauthenticated
            } finally {
                // Todo: share Context here
            }
        }

        chain.doFilter(req, res);
    }

    private String extractFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (ACCESS_COOKIE_NAME.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    private String extractFromAuthorization(HttpServletRequest req) {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

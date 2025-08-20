package me.fi_calculator.fi_calculator.controller.auth;

import jakarta.validation.Valid;
import me.fi_calculator.fi_calculator.config.app.AppSettings;
import me.fi_calculator.fi_calculator.domain.entity.RoleEntity;
import me.fi_calculator.fi_calculator.domain.entity.UserEntity;
import me.fi_calculator.fi_calculator.domain.dtos.LoginRequest;
import me.fi_calculator.fi_calculator.domain.dtos.LoginResult;
import me.fi_calculator.fi_calculator.domain.generic.ApiResponse;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterRequest;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterResponse;
import me.fi_calculator.fi_calculator.services.JwtService;
import me.fi_calculator.fi_calculator.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AppSettings appSettings;

    public AuthController(UserService userService, JwtService jwtService, AppSettings appSettings) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.appSettings = appSettings;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest req) {
        var registerResponse = userService.createUser(req);
        return ResponseEntity.created(URI.create("/token")).body(
                ApiResponse.created(registerResponse)
        );
    }

    // Best practice is Just separate api for login not after SignIn user :)
    // and put jwt things in cookie we can manage it better
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<LoginResult>> token(@Valid @RequestBody LoginRequest req) {
        UserEntity u = userService.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!userService.passwordMatches(req.password(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        Set<String> roles = u.getRoles().stream().map(RoleEntity::getCode).collect(Collectors.toSet());
        String jwt = jwtService.generateAccess(u.getEmail(), u.getId(), roles);

        ResponseCookie cookie = ResponseCookie.from(appSettings.getCookieName(), jwt)
                .httpOnly(true)
                .secure(appSettings.isCookieSecure())
                .path(appSettings.getCookiePath())
                .sameSite(appSettings.getCookieSameSite())
                .maxAge(Duration.ofSeconds(accessTokenTtlSeconds()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.ok(new LoginResult("login successful",
                        accessTokenTtlSeconds())));
    }

    private long accessTokenTtlSeconds() {
        return appSettings.getJwtAccessExpMinutes() * 60L;
    }

    // You can write Logout api too
}

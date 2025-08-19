package me.fi_calculator.fi_calculator.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import me.fi_calculator.fi_calculator.domain.UserEntity;
import me.fi_calculator.fi_calculator.domain.dtos.LoginRequest;
import me.fi_calculator.fi_calculator.domain.dtos.LoginResult;
import me.fi_calculator.fi_calculator.domain.generic.ApiResponse;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterRequest;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterResponse;
import me.fi_calculator.fi_calculator.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<ApiResponse<LoginResult>> token(@Valid @RequestBody LoginRequest req,
                                                          HttpServletResponse response) {
        UserEntity u = userService.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!userService.passwordMatches(req.password(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return null;
    }
}

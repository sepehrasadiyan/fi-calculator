package me.fi_calculator.fi_calculator.services;

import me.fi_calculator.fi_calculator.config.app.AppSettings;
import me.fi_calculator.fi_calculator.domain.UserEntity;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterRequest;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterResponse;
import me.fi_calculator.fi_calculator.repository.RoleRepository;
import me.fi_calculator.fi_calculator.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final AppSettings appSettings;

    public UserService(UserRepository users, RoleRepository roles, AppSettings appSettings) {
        this.users = users;
        this.roles = roles;
        this.appSettings = appSettings;
    }

    @Transactional
    public RegisterResponse createUser(RegisterRequest registerRequest) {
        users.findByEmailIgnoreCase(registerRequest.email()).ifPresent(x -> {
            throw new IllegalArgumentException("Email already registered");
        });
        UserEntity u = new UserEntity();
        String normEmail = registerRequest.email().trim().toLowerCase();
        u.setEmail(normEmail);
        return null;
    }
}

package me.fi_calculator.fi_calculator.services;

import me.fi_calculator.fi_calculator.config.app.AppSettings;
import me.fi_calculator.fi_calculator.domain.entity.RoleEntity;
import me.fi_calculator.fi_calculator.domain.entity.UserEntity;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterRequest;
import me.fi_calculator.fi_calculator.domain.dtos.RegisterResponse;
import me.fi_calculator.fi_calculator.repository.RoleRepository;
import me.fi_calculator.fi_calculator.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final AppSettings appSettings;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles, AppSettings appSettings, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.appSettings = appSettings;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse createUser(RegisterRequest registerRequest) {
        final String normEmail = registerRequest.email().trim().toLowerCase(Locale.ROOT);
        users.findByEmailIgnoreCase(normEmail).ifPresent(x -> {
            throw new IllegalArgumentException("Email already registered");
        });
        UserEntity u = new UserEntity();
        u.setEmail(normEmail);
        u.setPassword(passwordEncoder.encode(registerRequest.password()));
        Set<RoleEntity> roleEntities = appSettings.getDefaultUserRoles().stream()
                .map(code -> roles.findByCode(code).orElseGet(() -> {
                    RoleEntity r = new RoleEntity();
                    r.setCode(code);
                    String friendly = code.substring(0, 1).toUpperCase(Locale.ROOT) +
                            code.substring(1).toLowerCase(Locale.ROOT).replace('_', ' ');
                    r.setName(friendly);
                    return roles.saveAndFlush(r);
                }))
                .collect(Collectors.toSet());
        u.getRoles().addAll(roleEntities);

        UserEntity saved = users.save(u);

        Set<String> roleCodes = saved.getRoles().stream()
                .map(RoleEntity::getCode)
                .collect(Collectors.toCollection(java.util.TreeSet::new));
        return new RegisterResponse(saved.getId(), saved.getEmail(), roleCodes);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return users.findByEmailWithRoles(email);
    }

    public boolean passwordMatches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
}

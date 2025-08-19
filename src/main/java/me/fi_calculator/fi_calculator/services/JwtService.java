package me.fi_calculator.fi_calculator.services;

import me.fi_calculator.fi_calculator.config.app.AppSettings;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {

    private final AppSettings settings;

    public JwtService(AppSettings settings) {
        this.settings = settings;
    }

    public String generateAccess(String subjectEmail, UUID userId, Set<String> roleCodes) {
        return null;
    }


}

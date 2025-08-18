package me.fi_calculator.fi_calculator.services;

import me.fi_calculator.fi_calculator.config.app.AppSettings;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final AppSettings settings;

    public JwtService(AppSettings settings) {
        this.settings = settings;
    }
}

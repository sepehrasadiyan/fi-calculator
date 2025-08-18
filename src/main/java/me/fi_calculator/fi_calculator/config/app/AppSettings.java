package me.fi_calculator.fi_calculator.config.app;

import java.util.Set;

public final class AppSettings {

    private final Set<String> defaultUserRoles;

    public AppSettings(AppProperties props) {
        this.defaultUserRoles = Set.copyOf(props.security().defaultUserRoles());
    }

    public Set<String> defaultUserRoles() {
        return defaultUserRoles;
    }
}

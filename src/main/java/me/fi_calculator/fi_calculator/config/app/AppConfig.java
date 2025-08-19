package me.fi_calculator.fi_calculator.config.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public AppSettings appSettings(AppProperties props) {
        var settings = new AppSettings(props);
        log.info("AppSettings initialized: defaultRoles={}",
                settings.getDefaultUserRoles());
        return settings;
    }
}

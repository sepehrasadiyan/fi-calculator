package me.fi_calculator.fi_calculator.config.app;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

    @Bean
    public OpenAPI fireApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIRE Calculator API")
                        .version("v1")
                        .description("User-scoped FIRE calculations with JWT (cookie) auth."))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

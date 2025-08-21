package me.fi_calculator.fi_calculator.config.app;


import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
@EnableCaching
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


    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cm = new CaffeineCacheManager(
                "usersByEmail"
        );
        cm.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1_000_000)
                .expireAfterAccess(60, TimeUnit.MINUTES));
        return cm;
    }
}

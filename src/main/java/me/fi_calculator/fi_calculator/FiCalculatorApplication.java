package me.fi_calculator.fi_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FiCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiCalculatorApplication.class, args);
    }

}

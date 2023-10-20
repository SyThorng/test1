package com.kshrd.soccer_date;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
        name = "auth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
@OpenAPIDefinition(info = @Info(title = "SoccerDate API", version = "1.0", description = "Welcome to SoccerDate API"))
public class SoccerDateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoccerDateApplication.class, args);
    }

}

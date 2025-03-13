package com.autiwarrior;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                version = "1.0",
                contact = @Contact(name = "Omar Ebid", email = "ebidomar79@gmail.com"),
                license = @License(name = "MU License"),
                description = "API for user management system"
        )
)
@SpringBootApplication
public class AutiwarriorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutiwarriorApplication.class, args);
    }
}
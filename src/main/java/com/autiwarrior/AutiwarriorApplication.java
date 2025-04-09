package com.autiwarrior;

import io.github.cdimascio.dotenv.Dotenv;
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
                description = "API for medical management system"
        )
)
@SpringBootApplication
public class AutiwarriorApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(AutiwarriorApplication.class, args);
        System.out.println("hello jo ");
    }
}
package com.autiwarrior.config;

import brevoApi.TransactionalEmailsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrevoConfig {
    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        return new TransactionalEmailsApi(); // Initialize with required parameters
    }
}
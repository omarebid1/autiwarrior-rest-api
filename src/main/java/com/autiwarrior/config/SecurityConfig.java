package com.autiwarrior.config;


import com.autiwarrior.filters.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    //private final OAuth2SuccessHandler oauth2SuccessHandler; // Add this

    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService/*, OAuth2SuccessHandler oauth2SuccessHandler*/) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        //this.oauth2SuccessHandler = oauth2SuccessHandler; // Initialize OAuth2SuccessHandler
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Disable sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/doctors/**", "/api/auth/**", "/swagger-ui/**", "/v3/**", "/api/public/**", "/api/messages/**", "/ws-chat/**").permitAll() // Allow access to log in/register
                        //.requestMatchers("/api/mothers/**").authenticated() // Protect doctor endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                /*.oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2SuccessHandler) // Use custom OAuth2 success handler
                )*/
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
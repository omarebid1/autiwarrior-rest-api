package com.autiwarrior.config;

import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.entities.User;
import com.autiwarrior.filters.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // Extract user details from OAuth2User
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String providerId = oauth2User.getAttribute("sub"); // Google's unique ID

        // Check if the user already exists
        User user = userRepository.findByEmailAndProvider(email, "google")
                .orElseGet(() -> {
                    // If the user doesn't exist, create a new user
                    User newUser = new User(firstName, lastName, email, User.Role.MOTHER, "google", providerId);
                    return userRepository.save(newUser);
                });

        // Generate a JWT token with custom claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("provider", user.getProvider());
        claims.put("providerId", user.getProviderId());

        String token = jwtUtil.generateToken(user.getEmail(), claims);

        // Add the token to the response
        response.addHeader("Authorization", "Bearer " + token);

        // Redirect to a success page or endpoint
        response.sendRedirect("/api/auth/success?token=" + token);
    }
}
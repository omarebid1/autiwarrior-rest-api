package com.autiwarrior.filters;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Method to generate a JWT token with custom claims
    public String generateToken(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setSubject(email)
                .addClaims(claims) // Add custom claims
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(60)) // Expires in 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Method to generate a reset token
    public String generateResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(15)) // Expires in 15 min
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract Claims from the token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract the username (email) from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract the email from the token
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    // Extract the role from the token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Extract the provider from the token
    public String extractProvider(String token) {
        return extractClaims(token).get("provider", String.class);
    }

    // Extract the provider ID from the token
    public String extractProviderId(String token) {
        return extractClaims(token).get("providerId", String.class);
    }

    // Method to validate the token
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Calculate token expiration time
    private Date getExpirationDate(long minutes) {
        return Date.from(LocalDateTime.now()
                .plusMinutes(minutes)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


}


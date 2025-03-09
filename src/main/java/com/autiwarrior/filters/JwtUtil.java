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

    // Method to generate a JWT token for login
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role))
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(60))   //  Expires in 1 hour
                .signWith(key, SignatureAlgorithm.HS256)    //  Sign the token
                .compact(); //  Compact it into a string
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

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract the email from the token
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    // Method to validate the token
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Calculate token expiration time
    private Date getExpirationDate(long minutes) {
        return Date.from(LocalDateTime.now()
                .plusMinutes(minutes)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}


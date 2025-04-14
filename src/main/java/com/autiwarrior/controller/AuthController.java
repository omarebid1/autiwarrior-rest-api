package com.autiwarrior.controller;

import com.autiwarrior.dto.*;
import com.autiwarrior.entities.*;
import com.autiwarrior.dao.*;
import com.autiwarrior.filters.JwtUtil;
import com.autiwarrior.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login endpoint reached");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found.");
        }

        User user = userOptional.get();

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password.");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));

        // Log successful login
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        System.out.printf("%s signed in successfully at %s%n", user.getEmail(), formattedTime);
        System.out.printf("Generated token: %s%n", token);

        // Return the token in the response
        return ResponseEntity.ok(token);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Register endpoint reached");
    }


    //todo security issue (user can't make himself admin)
    //admin only created by admin
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Validate doctorLicense if role is DOCTOR
        if (request.getRole() == User.Role.DOCTOR) {
            if (request.getDoctorLicense() == null || request.getDoctorLicense().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("In case of Doctors, the license is required!");
            }
        }

        // Create a new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // Set doctorLicense if role is DOCTOR
        if (request.getRole() == User.Role.DOCTOR) {
            user.setDoctorLicense(request.getDoctorLicense());
        }

        // Save the user
        userRepository.save(user);

        // Log successful registration
        System.out.printf("%s registered successfully!\n", user.getEmail());

        return ResponseEntity.ok(String.format("New user registered successfully!\n%s", user.getEmail()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User with this email does not exist.");
        }

        String token = jwtUtil.generateResetToken(email); // Generate JWT token

        emailService.sendPasswordResetEmail(email, token); // Send email with token

        return ResponseEntity.ok("Password reset email sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // Manually check if newPassword and confirmPassword match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        // Validate token
        String token = request.getToken();
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        // Extract email from token and find user
        String email = jwtUtil.extractEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // Update user's password
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful.");
    }
}

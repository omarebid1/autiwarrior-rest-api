package com.autiwarrior.controller;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.dao.MotherRepository;
import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.entities.Doctor;
import com.autiwarrior.entities.Mother;
import com.autiwarrior.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final MotherRepository motherRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. List Users (search + pagination)
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = search.isEmpty()
                ? userRepository.findAll(pageable)
                : userRepository.findByEmailContainingIgnoreCase(search, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", userPage.getContent());
        response.put("total", userPage.getTotalElements());
        response.put("page", userPage.getNumber());
        response.put("size", userPage.getSize());
        return ResponseEntity.ok(response);
    }

    // 2. Get single user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Create user (handles mother/doctor creation)
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider("local");

        User savedUser = userRepository.save(user);

        // Save to Mother or Doctor based on role
        switch (user.getRole()) {
            case MOTHER -> {
                Mother mother = new Mother();
                mother.setUser(savedUser);
                motherRepository.save(mother);
            }
            case DOCTOR -> {
                Doctor doctor = new Doctor();
                doctor.setUser(savedUser);
                doctor.setDoctorLicense(user.getDoctorLicense());
                doctorRepository.save(doctor);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // 4. Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            // Handle doctor license updates
            if (user.getRole() == User.Role.DOCTOR && userDetails.getDoctorLicense() != null) {
                user.setDoctorLicense(userDetails.getDoctorLicense());
                doctorRepository.findById(user.getUserId()).ifPresent(doctor -> {
                    doctor.setDoctorLicense(userDetails.getDoctorLicense());
                    doctorRepository.save(doctor);
                });
            }

            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            switch (user.getRole()) {
                case MOTHER -> motherRepository.deleteById(id);
                case DOCTOR -> doctorRepository.deleteById(id);
            }
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. System Status
    @GetMapping("/system/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalUsers", userRepository.count());
        status.put("mothers", motherRepository.count());
        status.put("doctors", doctorRepository.count());
        status.put("serverTime", Instant.now());
        return ResponseEntity.ok(status);
    }
}

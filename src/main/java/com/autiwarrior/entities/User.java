package com.autiwarrior.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column() // Make password nullable for Google users
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String doctorLicense; // Field for doctor's license (optional)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column() // Nullable for local users
    private String provider; // e.g., "local", "google"

    @Column() // Nullable for local users
    private String providerId; // Unique ID from Google

    @JsonManagedReference
    @OneToOne(mappedBy = "user")
    private Doctor doctor;

    public enum Role {
        DOCTOR, MOTHER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the user's role as a GrantedAuthority
        return List.of(() -> "ROLE_" + this.getRole().name());
    }

    @Override
    public String getPassword() {
        // Return null for OAuth2 users (Google users don't have a password)
        return "local".equals(this.provider) ? this.password : null;
        //return this.getProvider().equals("local") ? this.getPassword() : null;
    }

    @Override
    public String getUsername() {
        // Use email as the username for authentication
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Constructor for local (email/password) users
    public User(String firstName, String lastName, String email, String password, Role role, String doctorLicense) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.doctorLicense = doctorLicense;
        this.provider = "local";
    }

    // Constructor for Google OAuth2 users
    public User(String firstName, String lastName, String email, Role role, String provider, String providerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

}
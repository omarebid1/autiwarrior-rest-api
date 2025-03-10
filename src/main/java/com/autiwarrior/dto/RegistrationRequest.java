package com.autiwarrior.dto;

import com.autiwarrior.entities.User;
import com.autiwarrior.validation.DoctorValidationGroup;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private User.Role role;

    @NotBlank(message = "Doctor license is required", groups = DoctorValidationGroup.class)
    private String doctorLicense;
}

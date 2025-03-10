package com.autiwarrior.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotNull(message = "Token cannot be null")
    @NotEmpty(message = "Token cannot be empty")
    private String token;

    @NotNull(message = "New password cannot be null")
    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;

    @NotNull(message = "Confirm password cannot be null")
    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;
}

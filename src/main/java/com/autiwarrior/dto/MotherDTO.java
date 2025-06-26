package com.autiwarrior.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MotherDTO {
    private Long motherId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;

    private String profilePictureBase64; // For frontend preview
}

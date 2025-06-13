package com.autiwarrior.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorProfileResponseDTO {
    private Integer doctorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String doctorLicense;
    private LocalDate dateOfBirth;
    private String address;
    private String academicDegree;
    private Integer yearsOfExperience;
    private String certificates;
    private String profilePictureBase64; // base64 encoded string
}

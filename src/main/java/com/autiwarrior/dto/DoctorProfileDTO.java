package com.autiwarrior.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class DoctorProfileDTO {
    private String doctorLicense;
    private String phoneNumber;
    private String specialization;
    private LocalDate dateOfBirth;
    private String address;
    private String academicDegree;
    private Integer yearsOfExperience;
    private String certificates;

    private MultipartFile profilePicture;
}
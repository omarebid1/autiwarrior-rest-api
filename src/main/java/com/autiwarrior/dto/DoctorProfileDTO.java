package com.autiwarrior.dto;

import com.autiwarrior.entities.Certificate;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class DoctorProfileDTO {
    private String doctorLicense;
    private String phoneNumber;
    private String specialization;
    private LocalDate dateOfBirth;
    private String address;
    private String academicDegree;
    private Integer yearsOfExperience;
    private List<Certificate> certificates;

    private MultipartFile profilePicture;
}
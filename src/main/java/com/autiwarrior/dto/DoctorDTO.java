package com.autiwarrior.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DoctorDTO {

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

    private String profilePictureBase64;

    private List<CertificateDTO> certificates;
}

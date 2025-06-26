package com.autiwarrior.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class MotherProfileDTO {
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;

    private MultipartFile profilePicture;
}

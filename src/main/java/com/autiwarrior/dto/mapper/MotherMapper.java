package com.autiwarrior.dto.mapper;

import com.autiwarrior.dto.MotherDTO;
import com.autiwarrior.entities.Mother;

import java.util.Base64;

public class MotherMapper {

    public static MotherDTO toDTO(Mother mother) {
        MotherDTO dto = new MotherDTO();
        dto.setMotherId(mother.getMotherId());
        dto.setFirstName(mother.getFirstName());
        dto.setLastName(mother.getLastName());
        dto.setEmail(mother.getEmail());
        dto.setPhoneNumber(mother.getPhoneNumber());
        dto.setDateOfBirth(mother.getDateOfBirth());
        dto.setAddress(mother.getAddress());

        if (mother.getProfilePicture() != null) {
            dto.setProfilePictureBase64(Base64.getEncoder().encodeToString(mother.getProfilePicture()));
        }

        return dto;
    }
}

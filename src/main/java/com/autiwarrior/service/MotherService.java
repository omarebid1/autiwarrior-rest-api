package com.autiwarrior.service;


import com.autiwarrior.dao.MotherRepository;
import com.autiwarrior.dto.MotherProfileDTO;
import com.autiwarrior.entities.Mother;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MotherService {

    @Autowired
    private MotherRepository motherRepository;

    // Create a new mother
    public Mother createMother(Mother mother) {
        return motherRepository.save(mother);
    }

    // Get all mothers
    public List<Mother> getAllMothers() {
        return motherRepository.findAll();
    }

    // Update a mother
    public Mother updateMother(Mother mother) {
        Optional<Mother> existing = getMotherByEmail(mother.getEmail());
        return existing.map(value -> motherRepository.save(mother)).orElse(null);
    }

    // Delete a mother by ID
    public void deleteMother(String email) {
        Mother mother = motherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mother not found"));
        motherRepository.deleteById(mother.getMotherId());
    }

    public Optional<Mother> getMotherByEmail(String email) {
        return motherRepository.findByEmail(email);
    }

    public void saveMotherDataByEmail(String email, MotherProfileDTO dto) throws IOException {
        Mother mother = motherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mother not found with email: " + email));

        mother.setPhoneNumber(dto.getPhoneNumber());
        mother.setDateOfBirth(dto.getDateOfBirth());
        mother.setAddress(dto.getAddress());

        if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
            mother.setProfilePicture(dto.getProfilePicture().getBytes());
        }

        motherRepository.save(mother);
    }
}

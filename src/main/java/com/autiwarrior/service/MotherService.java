package com.autiwarrior.service;


import com.autiwarrior.dao.MotherRepository;
import com.autiwarrior.entities.Mother;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Find a mother by ID
    public Optional<Mother> getMotherById(Long motherId) {
        return motherRepository.findById(motherId);
    }

    // Get all mothers
    public List<Mother> getAllMothers() {
        return motherRepository.findAll();
    }

    // Update a mother
    public Mother updateMother(Mother mother) {
        return motherRepository.save(mother);
    }

    // Delete a mother by ID
    public void deleteMother(Long motherId) {
        motherRepository.deleteById(motherId);
    }
}

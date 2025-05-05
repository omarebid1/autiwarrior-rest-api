package com.autiwarrior.service;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.dto.DoctorProfileDTO;
import com.autiwarrior.entities.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // ------------------ Create Operations ------------------

    /**
     * Create and save a new doctor.
     */
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ------------------ Read Operations ------------------

    /**
     * Retrieve a doctor by ID.
     */
    public Optional<Doctor> getDoctorById(Integer doctorId) {
        return doctorRepository.findById(doctorId);
    }

    /**
     * Get a list of all doctors.
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ------------------ Update Operations ------------------

    /**
     * Update a doctor if they exist by ID.
     */
    public Doctor updateDoctor(Doctor doctor) {
        Optional<Doctor> existing = getDoctorById(doctor.getDoctorId());
        return existing.map(value -> doctorRepository.save(doctor)).orElse(null);
    }


    /**
     * Update profile data using doctor license instead of ID.
     */
    public void saveDoctorDataByLicense(DoctorProfileDTO doctorProfileDTO) {
        if (doctorProfileDTO.getDoctorLicense() == null) {
            throw new IllegalArgumentException("Doctor License is required to update profile data.");
        }

        // Find the existing doctor by license
        Doctor existingDoctor = doctorRepository.findByDoctorLicense(doctorProfileDTO.getDoctorLicense())
                .orElseThrow(() -> new RuntimeException("Doctor with license " + doctorProfileDTO.getDoctorLicense() + " not found"));

        // Map data from the DTO to the Doctor entity
        existingDoctor.setPhoneNumber(doctorProfileDTO.getPhoneNumber());
        existingDoctor.setSpecialization(doctorProfileDTO.getSpecialization());
        existingDoctor.setDateOfBirth(doctorProfileDTO.getDateOfBirth());
        existingDoctor.setAddress(doctorProfileDTO.getAddress());
        existingDoctor.setAcademicDegree(doctorProfileDTO.getAcademicDegree());
        existingDoctor.setYearsOfExperience(doctorProfileDTO.getYearsOfExperience());
        existingDoctor.setCertificates(doctorProfileDTO.getCertificates());

        // Save the updated doctor entity
        doctorRepository.save(existingDoctor);
    }


    // ------------------ Delete Operations ------------------

    /**
     * Delete a doctor by ID.
     */
    public void deleteDoctor(Integer doctorId) {
        doctorRepository.deleteById(doctorId);
    }
}


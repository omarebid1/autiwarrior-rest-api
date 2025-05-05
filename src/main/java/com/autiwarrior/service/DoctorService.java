package com.autiwarrior.service;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.dao.UserRepository;
import com.autiwarrior.dto.DoctorProfileDTO;
import com.autiwarrior.entities.Doctor;
import com.autiwarrior.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
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
     * Retrieve a doctor by license as Optional.
     */
    public Optional<Doctor> getDoctorByLicenseOptional(String doctorLicense) {
        return doctorRepository.findAllByDoctorLicense(doctorLicense);
    }

    /**
     * Retrieve a doctor by license, or return null if not found.
     */
    public Optional<Doctor> getDoctorByLicense(String license) {
        return doctorRepository.findByDoctorLicense(license);
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
     * Update specific profile data using doctor ID.
     */
    public void saveDoctorData(Doctor doctor) {
        if (doctor.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required to update profile data.");
        }

        Doctor existingDoctor = doctorRepository.findById(doctor.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existingDoctor.setAcademicDegree(doctor.getAcademicDegree());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setYearsOfExperience(doctor.getYearsOfExperience());
        existingDoctor.setCertificates(doctor.getCertificates());

        doctorRepository.save(existingDoctor);
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

    // ------------------ Utility Methods ------------------

    /**
     * Extract profile-related data from a saved doctor by ID.
     */
    public List<String> getDoctorData(Doctor doctor) {
        Doctor savedDoctor = doctorRepository.findById(doctor.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<String> doctorData = new ArrayList<>();
        doctorData.add(savedDoctor.getAcademicDegree());
        doctorData.add(savedDoctor.getSpecialization());
        doctorData.add(String.valueOf(savedDoctor.getYearsOfExperience()));
        doctorData.add(savedDoctor.getCertificates());

        return doctorData;
    }

    /**
     * Extract profile-related data from a given doctor object.
     */
    public List<String> extractDoctorData(Doctor doctor) {
        List<String> doctorData = new ArrayList<>();
        doctorData.add(doctor.getAcademicDegree());
        doctorData.add(doctor.getSpecialization());
        doctorData.add(String.valueOf(doctor.getYearsOfExperience()));
        doctorData.add(doctor.getCertificates());
        return doctorData;
    }

}


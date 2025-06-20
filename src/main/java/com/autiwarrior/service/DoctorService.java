package com.autiwarrior.service;

import com.autiwarrior.dao.CertificateRepository;
import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.dto.DoctorProfileDTO;
import com.autiwarrior.entities.Certificate;
import com.autiwarrior.entities.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CertificateRepository certificateRepo;

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


    public void uploadCertificates(Integer doctorId, List<MultipartFile> files) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        for (MultipartFile file : files) {
            try {
                Certificate cert = new Certificate();
                cert.setDoctor(doctor);
                cert.setName(file.getOriginalFilename());
                cert.setFileType(file.getContentType());
                cert.setFileData(file.getBytes()); // 🔁 store in DB

                certificateRepo.save(cert);
            } catch (IOException e) {
                throw new RuntimeException("Error saving certificate", e);
            }
        }
    }

    public List<Certificate> getCertificatesByDoctorId(Integer doctorId) {
        return certificateRepo.findByDoctorDoctorId(doctorId);
    }

    public void saveDoctorDataById(Integer doctorId, DoctorProfileDTO dto) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setDoctorLicense(dto.getDoctorLicense());
        doctor.setAcademicDegree(dto.getAcademicDegree());
        doctor.setAddress(dto.getAddress());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getProfilePicture() != null) {
            doctor.setProfilePicture(dto.getProfilePicture().getBytes());
        }

        doctorRepository.save(doctor);
    }


    // ------------------ Delete Operations ------------------

    /**
     * Delete a doctor by ID.
     */
    public void deleteDoctor(Integer doctorId) {
        doctorRepository.deleteById(doctorId);
    }
}


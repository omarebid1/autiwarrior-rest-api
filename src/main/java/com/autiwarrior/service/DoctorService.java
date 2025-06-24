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

    // ------------------ Create ------------------

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ------------------ Read ------------------

    public Optional<Doctor> getDoctorById(Integer doctorId) {
        return doctorRepository.findById(doctorId);
    }

    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ------------------ Update ------------------

    public Doctor updateDoctor(Doctor doctor) {
        Optional<Doctor> existing = getDoctorById(doctor.getDoctorId());
        return existing.map(value -> doctorRepository.save(doctor)).orElse(null);
    }

    // ------------------ Profile & Certificates ------------------

    public void uploadCertificates(String email, List<MultipartFile> files) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        for (MultipartFile file : files) {
            try {
                Certificate cert = new Certificate();
                cert.setDoctor(doctor);
                cert.setName(file.getOriginalFilename());
                cert.setFileType(file.getContentType());
                cert.setFileData(file.getBytes());

                certificateRepo.save(cert);
            } catch (IOException e) {
                throw new RuntimeException("Error saving certificate", e);
            }
        }
    }

    public List<Certificate> getCertificatesByEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return certificateRepo.findByDoctorDoctorId(doctor.getDoctorId());
    }

    public void saveDoctorDataByEmail(String email, DoctorProfileDTO dto) throws IOException {
        Doctor doctor = doctorRepository.findByEmail(email)
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

    // ------------------ Delete ------------------

    public void deleteDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctorRepository.deleteById(doctor.getDoctorId());
    }
}

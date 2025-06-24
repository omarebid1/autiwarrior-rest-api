package com.autiwarrior.controller;

import com.autiwarrior.dto.CertificateDTO;
import com.autiwarrior.dto.DoctorDTO;
import com.autiwarrior.dto.DoctorProfileDTO;
import com.autiwarrior.dto.mapper.DoctorMapper;
import com.autiwarrior.entities.Certificate;
import com.autiwarrior.entities.Doctor;
import com.autiwarrior.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ------------------ Create ------------------

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        List<Certificate> certificates = doctorService.getCertificatesByEmail(createdDoctor.getEmail());
        DoctorDTO dto = DoctorMapper.toDTO(createdDoctor, certificates);
        return ResponseEntity.ok(dto);
    }

    // ------------------ Read ------------------

    @GetMapping("/find")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@RequestParam String email) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Doctor doctor = doctorOpt.get();
        List<Certificate> certificates = doctorService.getCertificatesByEmail(email);
        DoctorDTO dto = DoctorMapper.toDTO(doctor, certificates);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> doctorDTOs = doctors.stream().map(doctor -> {
            List<Certificate> certificates = doctorService.getCertificatesByEmail(doctor.getEmail());
            return DoctorMapper.toDTO(doctor, certificates);
        }).toList();

        return ResponseEntity.ok(doctorDTOs);
    }

    // ------------------ Update ------------------

    @PutMapping("update")
    public ResponseEntity<Doctor> updateDoctor(@RequestParam String email, @RequestBody Doctor doctor) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        doctor.setDoctorId(doctorOpt.get().getDoctorId());
        Doctor updatedDoctor = doctorService.updateDoctor(doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    // ------------------ Delete ------------------

    @DeleteMapping
    public ResponseEntity<Void> deleteDoctor(@RequestParam String email) {
        doctorService.deleteDoctor(email);
        return ResponseEntity.noContent().build();
    }

    // ------------------ Profile ------------------

    @GetMapping("/profile")
    public ResponseEntity<DoctorDTO> getDoctorProfile(@RequestParam String email) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Doctor doctor = doctorOpt.get();
        List<Certificate> certificates = doctorService.getCertificatesByEmail(email);
        DoctorDTO dto = DoctorMapper.toDTO(doctor, certificates);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> completeDoctorData(
            @RequestParam String email,
            @ModelAttribute DoctorProfileDTO doctorProfileDTO) throws IOException {

        doctorService.saveDoctorDataByEmail(email, doctorProfileDTO);
        return ResponseEntity.ok("Doctor profile updated successfully");
    }

    // ------------------ Certificates ------------------

    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCertificates(
            @RequestParam String email,
            @RequestParam("files") List<MultipartFile> files) {

        doctorService.uploadCertificates(email, files);
        return ResponseEntity.ok("Certificates uploaded successfully.");
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<CertificateDTO>> getCertificates(@RequestParam String email) {
        List<Certificate> certificates = doctorService.getCertificatesByEmail(email);

        List<CertificateDTO> response = certificates.stream().map(cert -> {
            CertificateDTO dto = new CertificateDTO();
            dto.setName(cert.getName());
            dto.setFileType(cert.getFileType());
            dto.setBase64Data(Base64.getEncoder().encodeToString(cert.getFileData()));
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}

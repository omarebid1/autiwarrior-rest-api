package com.autiwarrior.controller;

import com.autiwarrior.dto.DoctorProfileDTO;
import com.autiwarrior.dto.DoctorProfileResponseDTO;
import com.autiwarrior.entities.Doctor;
import com.autiwarrior.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ------------------ Create Operations ------------------

    /**
     * Endpoint to create a new doctor.
     */
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(createdDoctor);
    }

    // ------------------ Read Operations ------------------

    /**
     * Endpoint to retrieve a doctor by ID.
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Integer doctorId) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        return doctor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to retrieve all doctors.
     */
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // ------------------ Update Operations ------------------

    /**
     * Endpoint to update a doctor by ID.
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Integer doctorId, @RequestBody Doctor doctor) {
        doctor.setDoctorId(doctorId);
        Doctor updatedDoctor = doctorService.updateDoctor(doctor);
        return updatedDoctor != null ? ResponseEntity.ok(updatedDoctor) : ResponseEntity.notFound().build();
    }

    // ------------------ Delete Operations ------------------

    /**
     * Endpoint to delete a doctor by ID.
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    // ------------------ Doctor Profile Data ------------------

    /**
     * Endpoint to retrieve doctor profile data by doctor ID.
     */
    @GetMapping("/profile")
    public ResponseEntity<DoctorProfileResponseDTO> getDoctorProfile(@RequestParam Integer id) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(id);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Doctor doctor = doctorOpt.get();

        DoctorProfileResponseDTO dto = new DoctorProfileResponseDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setDoctorLicense(doctor.getDoctorLicense());
        dto.setDateOfBirth(doctor.getDateOfBirth());
        dto.setAddress(doctor.getAddress());
        dto.setAcademicDegree(doctor.getAcademicDegree());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());
        dto.setCertificates(doctor.getCertificates());

        // Convert image to Base64
        if (doctor.getProfilePicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(doctor.getProfilePicture());
            dto.setProfilePictureBase64(base64Image);
        }

        return ResponseEntity.ok(dto);
    }

    /**
     * Endpoint to complete doctor profile data using doctor license and optional profile picture.
     */
    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> completeDoctorData(@ModelAttribute DoctorProfileDTO doctorProfileDTO) {
        if (doctorProfileDTO.getDoctorLicense() == null) {
            throw new IllegalArgumentException("Doctor License is required to update profile data.");
        }

        doctorService.saveDoctorDataByLicense(doctorProfileDTO);
        return ResponseEntity.ok("Doctor profile updated successfully");
    }


}
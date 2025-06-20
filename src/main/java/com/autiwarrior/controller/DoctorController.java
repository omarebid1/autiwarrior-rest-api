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

    // ------------------ Create Operations ------------------

    /**
     * Creates a new doctor in the system.
     *
     * @param doctor The doctor object sent in the request body.
     * @return The created doctor object with a generated ID.
     */
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        List<Certificate> certificates = doctorService.getCertificatesByDoctorId(createdDoctor.getDoctorId());
        DoctorDTO dto = DoctorMapper.toDTO(createdDoctor, certificates);
        return ResponseEntity.ok(dto);
    }

    // ------------------ Read Operations ------------------

    /**
     * Retrieves a doctor by their unique ID.
     *
     * @param doctorId The ID of the doctor to retrieve.
     * @return The doctor object if found, or 404 if not.
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Integer doctorId) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Doctor doctor = doctorOpt.get();
        List<Certificate> certificates = doctorService.getCertificatesByDoctorId(doctorId);
        DoctorDTO dto = DoctorMapper.toDTO(doctor, certificates);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves a list of all doctors.
     *
     * @return A list of all registered doctors.
     */
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> doctorDTOs = doctors.stream().map(doctor -> {
            List<Certificate> certificates = doctorService.getCertificatesByDoctorId(doctor.getDoctorId());
            return DoctorMapper.toDTO(doctor, certificates);
        }).toList();

        return ResponseEntity.ok(doctorDTOs);
    }

    // ------------------ Update Operations ------------------

    /**
     * Updates an existing doctor's information by ID.
     *
     * @param doctorId The ID of the doctor to update.
     * @param doctor   The updated doctor information.
     * @return The updated doctor object, or 404 if not found.
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Integer doctorId, @RequestBody Doctor doctor) {
        doctor.setDoctorId(doctorId);
        Doctor updatedDoctor = doctorService.updateDoctor(doctor);
        return updatedDoctor != null ? ResponseEntity.ok(updatedDoctor) : ResponseEntity.notFound().build();
    }

    // ------------------ Delete Operations ------------------

    /**
     * Deletes a doctor by their ID.
     *
     * @param doctorId The ID of the doctor to delete.
     * @return 204 No Content if deletion is successful.
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    // ------------------ Doctor Profile Data ------------------

    /**
     * Retrieves a detailed doctor profile by doctor ID, including personal details,
     * profile picture (as Base64), and uploaded certificates (as Base64).
     *
     * @param id The ID of the doctor.
     * @return The full profile data of the doctor.
     */
    @GetMapping("/profile")
    public ResponseEntity<DoctorDTO> getDoctorProfile(@RequestParam Integer id) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(id);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Doctor doctor = doctorOpt.get();
        List<Certificate> certificates = doctorService.getCertificatesByDoctorId(id);
        DoctorDTO dto = DoctorMapper.toDTO(doctor, certificates);

        return ResponseEntity.ok(dto);
    }

    /**
     * Completes a doctor's profile by updating additional fields such as license and profile picture.
     * Expects multipart/form-data including optional profile image.
     *
     * @param doctorProfileDTO DTO containing profile completion fields.
     * @return Success message after saving the data.
     */
    @PostMapping(value = "/{doctorId}/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> completeDoctorData(
            @PathVariable Integer doctorId,
            @ModelAttribute DoctorProfileDTO doctorProfileDTO) throws IOException {

        doctorService.saveDoctorDataById(doctorId, doctorProfileDTO);
        return ResponseEntity.ok("Doctor profile updated successfully");
    }

    /**
     * Uploads one or more certificates for a doctor. Files are stored in the database as binary data (BLOB).
     *
     * @param doctorId The ID of the doctor.
     * @param files    The list of files to upload.
     * @return Success message after saving the certificates.
     */
    @PostMapping(value = "/{doctorId}/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCertificates(
            @PathVariable Integer doctorId,
            @RequestParam("files") List<MultipartFile> files) {
        doctorService.uploadCertificates(doctorId, files);
        return ResponseEntity.ok("Certificates uploaded successfully.");
    }

    /**
     * Retrieves all certificates for a given doctor. The files are returned as Base64 strings
     * along with their metadata to allow direct preview in the frontend.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of certificate DTOs containing name, file type, and Base64 data.
     */
    @GetMapping("/{doctorId}/certificates")
    public ResponseEntity<List<CertificateDTO>> getCertificates(@PathVariable Integer doctorId) {
        List<Certificate> certificates = doctorService.getCertificatesByDoctorId(doctorId);

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
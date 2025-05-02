package com.autiwarrior.controller;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.entities.Doctor;
import com.autiwarrior.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private Doctor doctor;
    // Create a new doctor

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(createdDoctor);
    }

    // Get a doctor by ID
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long doctorId) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        return doctor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // Update a doctor
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody Doctor doctor) {
        doctor.setDoctorId(doctorId);
        Doctor updatedDoctor = doctorService.updateDoctor(doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    // Delete a doctor by ID
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getDoctorData")
    public List<String> getDoctorData() {
        System.out.println("getDoctorData");
        return doctorService.getDoctorData(doctor);
    }

    // ✅ Post endpoint to get doctor data as a list of strings
    @PostMapping("/complete-profile")
    public ResponseEntity<List<String>> completeDoctorData(@RequestBody Doctor doctor) {
        List<String> doctorData = doctorService.SaveDoctorData(doctor);
        return ResponseEntity.ok(doctorData);
    }

}
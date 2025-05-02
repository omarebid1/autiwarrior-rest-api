package com.autiwarrior.service;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.entities.Doctor;
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
    private  List<String> doctorData = new ArrayList<>();


    // Create a new doctor
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Find a doctor by ID
    public Optional<Doctor> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Update a doctor
    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Delete a doctor by ID
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }


    public List<String> SaveDoctorData(Doctor doctor) {
        doctorData.add(doctor.getDoctorLicense());
        doctorData.add(doctor.getAcademicDegree());
        doctorData.add(doctor.getSpecialization());
        doctorData.add(String.valueOf(doctor.getYearsOfExperience()));
        doctorData.add(doctor.getCertificates());
        DoctorRepository doctorRepository = this.doctorRepository;
        doctorRepository.save(doctor);
        return doctorData;
    }

    // what paulo ask
    public List<String> getDoctorData(Doctor doctor) {
        return doctorData;
    }

}

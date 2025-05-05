package com.autiwarrior.service;

import com.autiwarrior.dao.DoctorRepository;
import com.autiwarrior.dao.UserRepository;
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
    private  List<String> doctorData = new ArrayList<>();


    // Create a new doctor
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Find a doctor by ID
    public Optional<Doctor> getDoctorById(Integer doctorId) {
        return doctorRepository.findById(doctorId);
    }
    public Optional<Doctor> getDoctorByLicense(String doctorLicense) {

        return doctorRepository.findAllByDoctorLicense(doctorLicense);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }


    // Update a doctor
    public Doctor updateDoctor(Doctor doctor) {
        Optional<Doctor> doctor1 = getDoctorById(doctor.getDoctorId());
        if(doctor1.isPresent()){
            return doctorRepository.save(doctor);

        }
        return null;
    }

    // Delete a doctor by ID
    public void deleteDoctor(Integer doctorId) {
        doctorRepository.deleteById(doctorId);
    }


    public void SaveDoctorData(Doctor doctor) {
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

    // what paulo ask
    public List<String> getDoctorData(Doctor doctor) {
        Doctor savedDoctor = doctorRepository.findById(doctor.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<String> doctorData = new ArrayList<>();
       // doctorData.add(savedDoctor.getDoctorLicense());
        doctorData.add(savedDoctor.getAcademicDegree());
        doctorData.add(savedDoctor.getSpecialization());
        doctorData.add(String.valueOf(savedDoctor.getYearsOfExperience()));
        doctorData.add(savedDoctor.getCertificates());

        return doctorData;
    }

    public List<String> extractDoctorData(Doctor doctor) {
        List<String> doctorData = new ArrayList<>();
       // doctorData.add(doctor.getDoctorLicense());
        doctorData.add(doctor.getAcademicDegree());
        doctorData.add(doctor.getSpecialization());
        doctorData.add(String.valueOf(doctor.getYearsOfExperience()));
        doctorData.add(doctor.getCertificates());
        return doctorData;
    }

    public void saveDoctorDataByLicense(Doctor doctor) {
        if (doctor.getDoctorLicense() == null) {
            throw new IllegalArgumentException("Doctor License is required to update profile data.");
        }

        Doctor existingDoctor = doctorRepository.findAllByDoctorLicense(doctor.getDoctorLicense())
                .orElseThrow(() -> new RuntimeException("Doctor with license " + doctor.getDoctorLicense() + " not found"));

        existingDoctor.setAcademicDegree(doctor.getAcademicDegree());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setYearsOfExperience(doctor.getYearsOfExperience());
        existingDoctor.setCertificates(doctor.getCertificates());

        doctorRepository.save(existingDoctor);
    }

}

package com.autiwarrior.dao;


import com.autiwarrior.entities.Doctor;
import com.autiwarrior.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {


    /**
     * Find a doctor by their ID (inherited from JpaRepository).
     */
    @NotNull Optional<Doctor> findById(@NotNull Integer doctorId);

    /**
     * Find a doctor by their license (custom query).
     */
    Optional<Doctor> findByDoctorLicense(String doctorLicense);

    /**
     * Custom query to find a doctor by their license, if you prefer it to be a custom method.
     */
    Optional<Doctor> findAllByDoctorLicense(String doctorLicense);

    /**
     * Retrieve all doctors (inherited from JpaRepository).
     */
    @NotNull List<Doctor> findAll();

}

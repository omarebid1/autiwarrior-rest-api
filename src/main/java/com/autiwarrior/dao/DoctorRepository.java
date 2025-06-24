package com.autiwarrior.dao;


import com.autiwarrior.entities.Doctor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @NotNull Optional<Doctor> findById(@NotNull Integer doctorId);

    Optional<Doctor> findByEmail(String email);

    @NotNull List<Doctor> findAll();
}
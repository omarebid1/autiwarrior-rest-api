package com.autiwarrior.dao;


import com.autiwarrior.entities.Doctor;
import com.autiwarrior.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {


    Optional<Doctor> findAllByDoctorLicense(String doctorLicense);

}

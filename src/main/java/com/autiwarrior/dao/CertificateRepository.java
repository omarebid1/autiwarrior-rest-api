package com.autiwarrior.dao;

import com.autiwarrior.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByDoctorDoctorId(Integer doctorId);
}


package com.autiwarrior.dao;

import com.autiwarrior.entities.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotherRepository extends JpaRepository<Mother, Long> {
    Optional<Mother> findByEmail(String email);
}
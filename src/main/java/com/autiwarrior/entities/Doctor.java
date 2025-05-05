package com.autiwarrior.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@Component
@Getter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    @Column(unique = true, nullable = false)
    private String doctorLicense;
    private LocalDate dateOfBirth;
    private String address;
    private String academicDegree; // الدرجة العلمية (e.g., "دكتوراه في طب الأطفال والنوحد")
    private Integer yearsOfExperience; // سنوات الخبرة (e.g., 10)
    private String certificates; // List of certificates (e.g., ["شهادة أطفال", "شهادة توحد"])

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true,insertable = false ,updatable = false)
    private User user;
}

package com.autiwarrior.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@Component
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

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


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;
}

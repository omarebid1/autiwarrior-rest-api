package com.autiwarrior.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "doctors")
@Data
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;
}

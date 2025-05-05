package com.autiwarrior.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


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
    private String academicDegree;
    private Integer yearsOfExperience;
    private String certificates;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;
}

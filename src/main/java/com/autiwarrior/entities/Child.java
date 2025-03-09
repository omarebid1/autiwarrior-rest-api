package com.autiwarrior.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "children")
@Data
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;

    @ManyToOne
    @JoinColumn(name = "mother_id", nullable = false)
    private Mother mother;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
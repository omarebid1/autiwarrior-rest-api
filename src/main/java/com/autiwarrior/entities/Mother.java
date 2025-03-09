package com.autiwarrior.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "mothers")
@Data
public class Mother {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long motherId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String address;
}
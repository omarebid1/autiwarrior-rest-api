package com.autiwarrior.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "certificates")
@Data
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // original file name
    private String fileType; // e.g., application/pdf, image/png
    private LocalDate uploadDate = LocalDate.now();

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData; // 🔁 store actual file

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

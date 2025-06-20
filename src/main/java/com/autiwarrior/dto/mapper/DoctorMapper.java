package com.autiwarrior.dto.mapper;

import com.autiwarrior.dto.CertificateDTO;
import com.autiwarrior.dto.DoctorDTO;
import com.autiwarrior.entities.Certificate;
import com.autiwarrior.entities.Doctor;

import java.util.Base64;
import java.util.List;

public class DoctorMapper {
    public static DoctorDTO toDTO(Doctor doctor, List<Certificate> certificates) {
        DoctorDTO dto = new DoctorDTO();

        dto.setDoctorId(doctor.getDoctorId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setDoctorLicense(doctor.getDoctorLicense());
        dto.setDateOfBirth(doctor.getDateOfBirth());
        dto.setAddress(doctor.getAddress());
        dto.setAcademicDegree(doctor.getAcademicDegree());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());

        if (doctor.getProfilePicture() != null) {
            dto.setProfilePictureBase64(Base64.getEncoder().encodeToString(doctor.getProfilePicture()));
        }

        List<CertificateDTO> certDTOs = certificates.stream().map(cert -> {
            CertificateDTO certDTO = new CertificateDTO();
            certDTO.setName(cert.getName());
            certDTO.setFileType(cert.getFileType());
            certDTO.setBase64Data(Base64.getEncoder().encodeToString(cert.getFileData()));
            return certDTO;
        }).toList();

        dto.setCertificates(certDTOs);

        return dto;
    }
}


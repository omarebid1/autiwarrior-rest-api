package com.autiwarrior.dto;

import lombok.Data;

@Data
public class CertificateDTO {
    private String name;
    private String fileType;
    private String base64Data; // ⬅️ used to render in frontend
}

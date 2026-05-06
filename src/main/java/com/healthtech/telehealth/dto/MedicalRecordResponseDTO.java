package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponseDTO {
    private Long id;
    private String patientName;
    private String patientEmail;
    private String bloodGroup;
    private String allergies;
    private String pastDiseases;
    private Double height;
    private Double weight;
    private LocalDateTime updatedAt;
}

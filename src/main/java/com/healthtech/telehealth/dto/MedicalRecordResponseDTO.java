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
    // Faz 4: Genişletilmiş alanlar
    private String chronicDiseases;
    private String surgeryHistory;
    private String familyHistory;
    private String currentMedications;
    private String disabilities;
    private Boolean smoker;
    private Boolean alcoholUse;
    private LocalDateTime updatedAt;
}

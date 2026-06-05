package com.healthtech.telehealth.dto;

import lombok.Data;

@Data
public class MedicalRecordRequestDTO {
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
}

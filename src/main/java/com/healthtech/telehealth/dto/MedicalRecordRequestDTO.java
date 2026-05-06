package com.healthtech.telehealth.dto;

import lombok.Data;

@Data
public class MedicalRecordRequestDTO {
    private String bloodGroup;
    private String allergies;
    private String pastDiseases;
    private Double height;
    private Double weight;
}

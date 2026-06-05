package com.healthtech.telehealth.dto;

import lombok.Data;

/**
 * Doktor profil oluşturma/güncelleme DTO
 */
@Data
public class DoctorProfileDTO {
    private String specialization;
    private String title;
    private String hospital;
    private String department;
    private String biography;
    private String languages;
    private String education;
    private Integer experienceYears;
    private String consultationFee;
}

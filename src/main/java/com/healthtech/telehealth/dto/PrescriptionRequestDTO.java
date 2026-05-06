package com.healthtech.telehealth.dto;

import lombok.Data;

@Data
public class PrescriptionRequestDTO {
    private Long appointmentId; // Hangi randevu icin recete yaziliyor?
    private String medicines; // Ilac listesi
    private String instructions; // Kullanim talimatlari
}

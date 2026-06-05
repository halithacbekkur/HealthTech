package com.healthtech.telehealth.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionRequestDTO {
    private Long appointmentId;
    private String medicines;
    private String instructions;
    // Faz 5: Genişletilmiş alanlar
    private String dosages;
    private String frequencies;
    private Integer durationDays;
    private LocalDate startDate;
    private String warnings;
    private String diagnosis;
}

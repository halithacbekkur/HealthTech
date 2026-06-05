package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {
    private Long id;
    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private String medicines;
    private String instructions;
    // Faz 5: Genişletilmiş alanlar
    private String dosages;
    private String frequencies;
    private Integer durationDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String warnings;
    private String diagnosis;
    private LocalDateTime createdAt;
}

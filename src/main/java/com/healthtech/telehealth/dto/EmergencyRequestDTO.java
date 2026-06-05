package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRequestDTO {
    private Long id;
    private String patientName;
    private Long patientId;
    private String assignedDoctorName;
    private Long assignedDoctorId;
    private String level;
    private String description;
    private String symptoms;
    private String location;
    private String status;
    private String responseNotes;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
    private LocalDateTime resolvedAt;
}

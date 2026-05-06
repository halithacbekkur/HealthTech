package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDateTime createdAt;
}

package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
}

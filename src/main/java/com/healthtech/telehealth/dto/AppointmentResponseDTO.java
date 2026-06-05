package com.healthtech.telehealth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDate;

    private AppointmentStatus status;
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}

package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCallDTO {
    private Long id;
    private Long appointmentId;
    private String roomId;
    private String doctorName;
    private String patientName;
    private Long doctorId;
    private Long patientId;
    private String status;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;
    private String notes;
    private LocalDateTime createdAt;
}

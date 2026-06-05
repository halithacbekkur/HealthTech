package com.healthtech.telehealth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoCallRequestDTO {
    private Long appointmentId;   // Randevuya bağlı arama (opsiyonel)
    private Long patientId;       // Doğrudan hasta ID ile arama
    private LocalDateTime scheduledAt;
}

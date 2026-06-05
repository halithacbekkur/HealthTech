package com.healthtech.telehealth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {

    // Randevuyu kim alıyor (hangi doktor için)
    private Long doctorId;

    // Randevu tarihi ve saati — ISO 8601 formatında kabul eder: "2026-05-10T14:30:00"
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDate;

    // Opsiyonel: Ziyaret sebebi / not
    private String notes;
}

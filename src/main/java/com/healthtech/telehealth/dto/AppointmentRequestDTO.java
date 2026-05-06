package com.healthtech.telehealth.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    
    // Randevuyu kim alıyor (hangi doktor için)
    private Long doctorId;
    
    // Randevu tarihi ve saati
    private LocalDateTime appointmentDate;
}

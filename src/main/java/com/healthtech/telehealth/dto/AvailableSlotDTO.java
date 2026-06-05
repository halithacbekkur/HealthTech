package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Müsait randevu slotu — Frontend'e sunulacak boş saatler.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSlotDTO {
    private LocalDateTime dateTime;
    private int durationMinutes;
}

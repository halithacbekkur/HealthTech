package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorReviewDTO {
    private Long id;
    private Long doctorProfileId;
    private String patientName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}

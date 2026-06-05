package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultDTO {
    private Long id;
    private String testName;
    private String testCategory;
    private String results;
    private String resultValue;
    private String unit;
    private String referenceRange;
    private String status;
    private LocalDate testDate;
    private String laboratory;
    private String fileUrl;
    private String notes;
    private String doctorName;
    private LocalDateTime createdAt;
}

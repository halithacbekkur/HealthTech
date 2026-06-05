package com.healthtech.telehealth.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DoctorReviewRequestDTO {
    private Long doctorProfileId;

    @Min(value = 1, message = "Puan en az 1 olmalı")
    @Max(value = 5, message = "Puan en fazla 5 olmalı")
    private int rating;

    private String comment;
}

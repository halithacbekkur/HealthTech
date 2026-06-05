package com.healthtech.telehealth.dto;

import lombok.Data;

@Data
public class EmergencyCreateDTO {
    private String level;        // LOW, MEDIUM, HIGH, CRITICAL
    private String description;
    private String symptoms;
    private String location;
}

package com.healthtech.telehealth.dto;

import lombok.Data;

/**
 * Acil durum kişisi DTO
 */
@Data
public class EmergencyContactDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String relationship;
}

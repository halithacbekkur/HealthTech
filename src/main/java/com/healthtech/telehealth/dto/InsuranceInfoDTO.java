package com.healthtech.telehealth.dto;

import lombok.Data;

/**
 * Sigorta bilgisi DTO
 */
@Data
public class InsuranceInfoDTO {
    private String insuranceType;
    private String insuranceCompany;
    private String policyNumber;
    private String sgkNo;
    private String notes;
}

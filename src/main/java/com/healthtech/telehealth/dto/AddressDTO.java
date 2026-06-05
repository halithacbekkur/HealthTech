package com.healthtech.telehealth.dto;

import lombok.Data;

/**
 * Adres request DTO
 */
@Data
public class AddressDTO {
    private String city;
    private String district;
    private String neighborhood;
    private String fullAddress;
    private String postalCode;
}

package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
}
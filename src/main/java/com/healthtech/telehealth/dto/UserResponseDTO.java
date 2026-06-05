package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.AccountStatus;
import com.healthtech.telehealth.entity.Gender;
import com.healthtech.telehealth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Genişletilmiş kullanıcı response DTO — Faz 1 alanlarıyla birlikte.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;

    // Faz 1 — Yeni alanlar
    private String tcKimlik;
    private LocalDate birthDate;
    private Gender gender;
    private String profilePhotoUrl;
    private AccountStatus accountStatus;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String bloodGroup;
    private String chronicDiseases;
    private String disabilities;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;

    // Geriye uyumluluk — eski 5 parametreli constructor
    public UserResponseDTO(Long id, String fullName, String email, String phone, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
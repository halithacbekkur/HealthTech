package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.Gender;
import lombok.Data;

import java.time.LocalDate;

/**
 * Profil güncelleme request DTO — Kullanıcının kendi profilini düzenlemesi.
 */
@Data
public class ProfileUpdateDTO {
    private String fullName;
    private String phone;
    private String tcKimlik;
    private LocalDate birthDate;
    private Gender gender;
    private String bloodGroup;
    private String chronicDiseases;
    private String disabilities;
}

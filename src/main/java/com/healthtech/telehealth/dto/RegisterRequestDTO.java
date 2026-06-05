package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.Gender;
import com.healthtech.telehealth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * Gelişmiş kayıt formu DTO — Faz 1 alanlarıyla birlikte.
 */
@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Ad Soyad boş olamaz")
    private String fullName;

    @Email(message = "Geçerli bir e-posta giriniz")
    @NotBlank(message = "E-posta boş olamaz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, message = "Şifre en az 8 karakter olmalı")
    private String password;

    @NotNull(message = "Rol boş olamaz")
    private Role role;

    // Opsiyonel alanlar
    private String phone;
    private String tcKimlik;
    private LocalDate birthDate;
    private Gender gender;
}

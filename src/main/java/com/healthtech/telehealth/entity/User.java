package com.healthtech.telehealth.entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "İsim boş olamaz")
    private String fullName;

    @Email(message = "Geçerli bir email giriniz")
    @NotBlank(message = "Email boş olamaz")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    private String phone;

    @NotNull(message = "Rol boş olamaz")
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
}
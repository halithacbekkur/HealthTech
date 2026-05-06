package com.healthtech.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad alanı boş bırakılamaz")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Soyad alanı boş bırakılamaz")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "TC Kimlik Numarası boş bırakılamaz")
    @Column(nullable = false, unique = true, length = 11)
    private String identityNumber;

    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır")
    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank(message = "Telefon numarası boş bırakılamaz")
    private String phone;

    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

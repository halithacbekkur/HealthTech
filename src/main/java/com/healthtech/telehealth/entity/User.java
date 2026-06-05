package com.healthtech.telehealth.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Kullanıcı entity — Hasta, Doktor ve Admin rolleri için ortak yapı.
 * Faz 1: TC kimlik, doğum tarihi, cinsiyet, profil fotoğrafı, hesap durumu eklendi.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Temel Kimlik Bilgileri =====
    @Column(unique = true, length = 11)
    private String tcKimlik;    // TC Kimlik Numarası (11 haneli)

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

    // ===== Faz 1: Yeni Alanlar =====
    private LocalDate birthDate;        // Doğum tarihi

    @Enumerated(EnumType.STRING)
    private Gender gender;              // Cinsiyet

    private String profilePhotoUrl;     // Profil fotoğrafı dosya yolu

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    private boolean emailVerified = false;   // E-posta doğrulanmış mı?
    private boolean phoneVerified = false;   // Telefon doğrulanmış mı?

    // ===== Sağlık Bilgileri (Hızlı erişim) =====
    private String bloodGroup;              // Kan grubu (A+, B-, O+ vb.)

    @Column(columnDefinition = "TEXT")
    private String chronicDiseases;         // Kronik hastalıklar

    @Column(columnDefinition = "TEXT")
    private String disabilities;            // Engellilik / özel durum bilgisi

    // ===== Güvenlik =====
    private int failedLoginAttempts = 0;    // Başarısız giriş denemesi
    private LocalDateTime lockedUntil;      // Hesap kilit süresi (brute-force koruması)
    private LocalDateTime lastLoginAt;      // Son giriş zamanı

    // ===== Zaman Damgaları =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.accountStatus == null) {
            this.accountStatus = AccountStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Yardımcı Metodlar =====
    public boolean isAccountLocked() {
        return this.lockedUntil != null && LocalDateTime.now().isBefore(this.lockedUntil);
    }

    public boolean isAccountActive() {
        return this.accountStatus == AccountStatus.ACTIVE;
    }
}
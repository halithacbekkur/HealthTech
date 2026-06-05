package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Doktor profil detayları — Uzmanlık, biyografi, hastane, dil bilgisi vb.
 * User ile OneToOne ilişkili (sadece DOCTOR rolündeki kullanıcılar).
 */
@Entity
@Table(name = "doctor_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String specialization;       // Uzmanlık alanı (Kardiyoloji, Nöroloji vb.)
    private String title;                // Unvan (Prof. Dr., Doç. Dr., Uzm. Dr. vb.)
    private String hospital;             // Çalıştığı hastane / klinik
    private String department;           // Bölüm

    @Column(columnDefinition = "TEXT")
    private String biography;            // Özgeçmiş / biyografi

    private String languages;            // Konuştuğu diller (virgülle ayrılmış)
    private String education;            // Eğitim bilgisi
    private Integer experienceYears;     // Deneyim yılı
    private String consultationFee;      // Muayene ücreti

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorApprovalStatus approvalStatus = DoctorApprovalStatus.PENDING;

    private String rejectionReason;      // Reddedilme nedeni (admin tarafından)

    private double averageRating = 0.0;  // Ortalama puan (hesaplanır)
    private int totalReviews = 0;        // Toplam yorum sayısı

    private LocalDateTime approvedAt;    // Onay tarihi
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.approvalStatus == null) this.approvalStatus = DoctorApprovalStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Faz 4: Gelişmiş Tıbbi Kayıt — Kronik hastalıklar, ameliyat, aile geçmişi.
 */
@Entity
@Table(name = "medical_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id", unique = true, nullable = false)
    private User patient;

    // Temel bilgiler
    private String bloodGroup;
    private Double height;
    private Double weight;

    // Faz 4: Genişletilmiş sağlık geçmişi
    @Column(columnDefinition = "TEXT")
    private String allergies;           // Alerjiler (virgülle ayrılmış)

    @Column(columnDefinition = "TEXT")
    private String pastDiseases;        // Geçirilmiş hastalıklar

    @Column(columnDefinition = "TEXT")
    private String chronicDiseases;     // Kronik hastalıklar (diyabet, tansiyon vb.)

    @Column(columnDefinition = "TEXT")
    private String surgeryHistory;      // Ameliyat geçmişi

    @Column(columnDefinition = "TEXT")
    private String familyHistory;       // Aile sağlık geçmişi

    @Column(columnDefinition = "TEXT")
    private String currentMedications;  // Şu anda kullandığı ilaçlar

    @Column(columnDefinition = "TEXT")
    private String disabilities;        // Engellilik / özel durum bilgisi

    private Boolean smoker;             // Sigara kullanımı
    private Boolean alcoholUse;         // Alkol kullanımı

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

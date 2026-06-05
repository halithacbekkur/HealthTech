package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Faz 4: Laboratuvar Sonuçları — Test türü, değer, referans aralığı, dosya.
 */
@Entity
@Table(name = "lab_results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;               // Testi isteyen doktor

    private String testName;            // Hemogram, Biyokimya, TSH vb.
    private String testCategory;        // KAN, İDRAR, GÖRÜNTÜLEME, PATOLOJİ

    @Column(columnDefinition = "TEXT")
    private String results;             // Sonuçlar (JSON veya metin)

    private String resultValue;         // Sayısal değer (örn. "5.2")
    private String unit;                // Birim (mg/dL, IU/L vb.)
    private String referenceRange;      // Referans aralık (örn. "4.0-6.0")

    @Enumerated(EnumType.STRING)
    private LabResultStatus status = LabResultStatus.NORMAL;

    private LocalDate testDate;         // Test tarihi
    private String laboratory;          // Laboratuvar adı
    private String fileUrl;             // Dosya/rapor URL'si (opsiyonel)

    @Column(columnDefinition = "TEXT")
    private String notes;               // Doktor notu

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

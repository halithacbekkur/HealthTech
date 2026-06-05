package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Faz 5: Gelişmiş Reçete — İlaç detayları, dozaj, süre, durum takibi.
 */
@Entity
@Table(name = "prescriptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // Faz 5: Genişletilmiş ilaç bilgisi
    @Column(columnDefinition = "TEXT", nullable = false)
    private String medicines;               // İlaç adları (virgülle ayrılmış veya JSON)

    @Column(columnDefinition = "TEXT")
    private String dosages;                  // Dozajlar (500mg, 250mg vb.)

    @Column(columnDefinition = "TEXT")
    private String frequencies;              // Kullanım sıklığı (günde 2, haftada 1 vb.)

    @Column(columnDefinition = "TEXT")
    private String instructions;             // Doktor talimatları

    private Integer durationDays;            // Kullanım süresi (gün)

    private LocalDate startDate;             // Başlangıç tarihi
    private LocalDate endDate;               // Bitiş tarihi

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String warnings;                 // İlaç etkileşim uyarıları

    @Column(columnDefinition = "TEXT")
    private String diagnosis;                // Tanı / teşhis

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.startDate == null) this.startDate = LocalDate.now();
        if (this.durationDays != null && this.endDate == null)
            this.endDate = this.startDate.plusDays(this.durationDays);
    }
}

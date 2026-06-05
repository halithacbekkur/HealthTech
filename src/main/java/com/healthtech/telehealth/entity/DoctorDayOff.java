package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Doktor izin/tatil günü — Belirli bir tarihte doktorun çalışmayacağını belirtir.
 * Haftalık şablonun üzerine istisna olarak eklenir.
 */
@Entity
@Table(name = "doctor_day_offs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id", "off_date"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorDayOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "off_date", nullable = false)
    private LocalDate offDate;           // İzin tarihi

    @Column(name = "reason")
    private String reason;               // İzin nedeni (opsiyonel): "Yıllık izin", "Konferans" vb.

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

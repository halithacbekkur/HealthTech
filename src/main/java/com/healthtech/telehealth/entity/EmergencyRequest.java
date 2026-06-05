package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Faz 10: Acil Durum Talebi — SOS butonu ile oluşturulan acil çağrı.
 */
@Entity
@Table(name = "emergency_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmergencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "assigned_doctor_id")
    private User assignedDoctor;        // Atanan doktor (opsiyonel)

    @Enumerated(EnumType.STRING)
    private EmergencyLevel level = EmergencyLevel.MEDIUM;

    @Column(columnDefinition = "TEXT")
    private String description;          // Acil durum açıklaması

    private String symptoms;             // Belirtiler
    private String location;             // Konum bilgisi (adres)

    @Enumerated(EnumType.STRING)
    private EmergencyStatus status = EmergencyStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String responseNotes;        // Doktor yanıt notları

    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

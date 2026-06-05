package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Tüm işlem logları — KVKK uyumu için erişim kaydı.
 */
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;          // LOGIN, VIEW_RECORD, UPDATE_PROFILE, DELETE_USER vb.
    private String entityType;      // User, Appointment, MedicalRecord vb.
    private Long entityId;          // İlgili kaydın ID'si

    private String performedBy;     // İşlemi yapan kullanıcının e-postası
    private String ipAddress;       // İstek IP adresi
    private String userAgent;       // Tarayıcı bilgisi

    @Column(columnDefinition = "TEXT")
    private String details;         // Ek bilgi (JSON formatında)

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

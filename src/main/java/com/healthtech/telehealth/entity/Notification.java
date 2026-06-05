package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Uygulama içi bildirim — Randevu, onay, mesaj vb. bildirimler.
 */
@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;              // Bildirimin sahibi

    private String title;           // Başlık
    @Column(columnDefinition = "TEXT")
    private String message;         // Mesaj içeriği

    private String type;            // APPOINTMENT, DOCTOR_APPROVAL, SYSTEM, REVIEW, PROFILE
    private String actionUrl;       // Tıklandığında yönlendirilecek sayfa (opsiyonel)
    private String icon;            // FontAwesome ikon sınıfı

    @Column(name = "is_read")
    private boolean read = false;   // Okundu mu?
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Faz 9: Video Görüşme — Randevuya bağlı video arama odası.
 */
@Entity
@Table(name = "video_calls")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VideoCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(unique = true, nullable = false)
    private String roomId;              // Benzersiz oda ID'si

    @Enumerated(EnumType.STRING)
    private VideoCallStatus status = VideoCallStatus.WAITING;

    private LocalDateTime scheduledAt;  // Planlanan zaman
    private LocalDateTime startedAt;    // Başlangıç
    private LocalDateTime endedAt;      // Bitiş
    private Integer durationSeconds;    // Süre (saniye)

    @Column(columnDefinition = "TEXT")
    private String notes;               // Görüşme notları

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.roomId == null) {
            this.roomId = "vc-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9999);
        }
    }
}

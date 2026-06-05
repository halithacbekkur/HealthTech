package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Hasta yorumları ve puanlama — Doktor-hasta ilişkisi.
 */
@Entity
@Table(name = "doctor_reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_profile_id", "patient_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_profile_id", nullable = false)
    private DoctorProfile doctorProfile;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    private int rating;              // 1-5 yıldız

    @Column(columnDefinition = "TEXT")
    private String comment;          // Yorum metni

    private boolean approved = true; // Moderasyon (admin onayı)

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

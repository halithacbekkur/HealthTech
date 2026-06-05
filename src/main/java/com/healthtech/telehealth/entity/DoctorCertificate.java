package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Doktor sertifika belgeleri — DoctorProfile ile OneToMany.
 */
@Entity
@Table(name = "doctor_certificates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_profile_id", nullable = false)
    private DoctorProfile doctorProfile;

    private String name;           // Sertifika adı
    private String institution;    // Veren kurum
    private String year;           // Yıl
    private String fileUrl;        // Dosya yolu (opsiyonel)

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

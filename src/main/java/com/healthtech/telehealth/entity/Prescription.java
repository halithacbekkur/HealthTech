package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bir randevunun genellikle 1 adet recetesi olur.
    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true, nullable = false)
    private Appointment appointment;

    // Ilaclar (JSON formati veya virgulle ayrilmis metin olarak tutulabilir)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String medicines; 

    // Doktorun kullanim talimatlari (Gunde 2 defa tok karnina vs.)
    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Entity veritabanina kaydedilmeden hemen once calisir
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

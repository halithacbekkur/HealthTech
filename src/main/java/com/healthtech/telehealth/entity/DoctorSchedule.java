package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Doktor haftalık çalışma takvimi — Her gün için çalışma saatleri.
 */
@Entity
@Table(name = "doctor_schedules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id", "day_of_week"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;        // MONDAY, TUESDAY vb.

    @Column(name = "start_time")
    private LocalTime startTime;         // Başlangıç saati (09:00)

    @Column(name = "end_time")
    private LocalTime endTime;           // Bitiş saati (17:00)

    @Column(name = "slot_duration_minutes", nullable = false)
    private int slotDurationMinutes = 30; // Randevu süresi (dakika)

    @Column(name = "available", nullable = false)
    private boolean available = true;    // Bu gün aktif mi?
}

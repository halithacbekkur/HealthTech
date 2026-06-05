package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.Appointment;
import com.healthtech.telehealth.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Bir hastanın tüm randevularını getir
    List<Appointment> findByPatientId(Long patientId);

    // Bir doktorun tüm randevularını getir
    List<Appointment> findByDoctorId(Long doctorId);

    // Bir hastanın belirli durumdaki randevularını getir
    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    // Bir doktorun belirli durumdaki randevularını getir
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // Raporlama: duruma gore randevu sayisi
    long countByStatus(AppointmentStatus status);

    // Faz 3: Çakışma kontrolü — belirli tarih aralığında aktif randevu var mı?
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate BETWEEN :start AND :end " +
           "AND a.status IN ('PENDING', 'APPROVED')")
    List<Appointment> findConflictingAppointments(Long doctorId, LocalDateTime start, LocalDateTime end);

    // Faz 3: Belirli tarih aralığında doktorun randevuları (müsaitlik hesabı)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate >= :dayStart AND a.appointmentDate < :dayEnd " +
           "AND a.status IN ('PENDING', 'APPROVED')")
    List<Appointment> findDoctorAppointmentsForDay(Long doctorId, LocalDateTime dayStart, LocalDateTime dayEnd);
}

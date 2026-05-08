package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.Appointment;
import com.healthtech.telehealth.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

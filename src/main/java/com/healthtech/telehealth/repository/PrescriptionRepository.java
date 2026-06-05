package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.Prescription;
import com.healthtech.telehealth.entity.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointmentId(Long appointmentId);
    List<Prescription> findByAppointment_Patient_Id(Long patientId);
    List<Prescription> findByAppointment_Doctor_Id(Long doctorId);
    List<Prescription> findByAppointment_Patient_IdAndStatus(Long patientId, PrescriptionStatus status);
}

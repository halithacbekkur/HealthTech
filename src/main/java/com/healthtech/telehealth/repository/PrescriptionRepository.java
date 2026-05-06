package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    // Bir randevuya ait receteyi bulmak
    Optional<Prescription> findByAppointmentId(Long appointmentId);

    // Bir hastanin (hasta ID'si uzerinden) tum recetelerini liste seklinde getirmek
    // Spring Data JPA bu isimlendirmeyi gorunce "appointment tablosuna git, oradan patient tablosuna gec ve id'yi karsilastir" der.
    List<Prescription> findByAppointment_Patient_Id(Long patientId);
}

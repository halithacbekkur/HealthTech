package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.EmergencyRequest;
import com.healthtech.telehealth.entity.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Long> {

    List<EmergencyRequest> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<EmergencyRequest> findByAssignedDoctorIdOrderByCreatedAtDesc(Long doctorId);

    List<EmergencyRequest> findByStatusOrderByCreatedAtDesc(EmergencyStatus status);

    List<EmergencyRequest> findByStatusInOrderByCreatedAtDesc(List<EmergencyStatus> statuses);

    List<EmergencyRequest> findAllByOrderByCreatedAtDesc();

    long countByStatus(EmergencyStatus status);
}

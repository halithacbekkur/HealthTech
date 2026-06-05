package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.DoctorApprovalStatus;
import com.healthtech.telehealth.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUserId(Long userId);
    List<DoctorProfile> findByApprovalStatus(DoctorApprovalStatus status);
    List<DoctorProfile> findByApprovalStatusAndSpecializationContainingIgnoreCase(
            DoctorApprovalStatus status, String specialization);
    List<DoctorProfile> findByApprovalStatusOrderByAverageRatingDesc(DoctorApprovalStatus status);
}

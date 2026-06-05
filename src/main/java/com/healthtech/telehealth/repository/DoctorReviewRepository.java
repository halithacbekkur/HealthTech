package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.DoctorReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorReviewRepository extends JpaRepository<DoctorReview, Long> {
    List<DoctorReview> findByDoctorProfileIdAndApprovedTrueOrderByCreatedAtDesc(Long profileId);
    Optional<DoctorReview> findByDoctorProfileIdAndPatientId(Long profileId, Long patientId);

    @Query("SELECT AVG(r.rating) FROM DoctorReview r WHERE r.doctorProfile.id = :profileId AND r.approved = true")
    Double findAverageRatingByProfileId(Long profileId);

    @Query("SELECT COUNT(r) FROM DoctorReview r WHERE r.doctorProfile.id = :profileId AND r.approved = true")
    int countByProfileId(Long profileId);
}

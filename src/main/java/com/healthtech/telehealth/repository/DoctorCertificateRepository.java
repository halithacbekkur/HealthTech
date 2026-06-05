package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.DoctorCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorCertificateRepository extends JpaRepository<DoctorCertificate, Long> {
    List<DoctorCertificate> findByDoctorProfileId(Long profileId);
}

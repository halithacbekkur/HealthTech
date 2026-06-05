package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.InsuranceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceInfoRepository extends JpaRepository<InsuranceInfo, Long> {
    Optional<InsuranceInfo> findByUserId(Long userId);
}

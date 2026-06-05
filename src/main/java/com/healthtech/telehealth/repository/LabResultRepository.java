package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByPatientIdOrderByTestDateDesc(Long patientId);
    List<LabResult> findByPatientIdAndTestCategoryOrderByTestDateDesc(Long patientId, String category);
}

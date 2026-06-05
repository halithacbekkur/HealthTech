package com.healthtech.telehealth.dto;

import com.healthtech.telehealth.entity.DoctorApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Doktor profil response DTO — Arama sonuçları ve detay sayfası.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileResponseDTO {
    private Long profileId;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String profilePhotoUrl;

    // Doktor bilgileri
    private String specialization;
    private String title;
    private String hospital;
    private String department;
    private String biography;
    private String languages;
    private String education;
    private Integer experienceYears;
    private String consultationFee;

    // Onay durumu
    private DoctorApprovalStatus approvalStatus;
    private String rejectionReason;

    // Puan
    private double averageRating;
    private int totalReviews;

    // Sertifikalar
    private List<DoctorCertificateDTO> certificates;
}

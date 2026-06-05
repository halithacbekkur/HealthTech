package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorProfileService {

    private final DoctorProfileRepository profileRepo;
    private final DoctorCertificateRepository certRepo;
    private final DoctorReviewRepository reviewRepo;
    private final UserRepository userRepo;

    public DoctorProfileService(DoctorProfileRepository profileRepo,
                                DoctorCertificateRepository certRepo,
                                DoctorReviewRepository reviewRepo,
                                UserRepository userRepo) {
        this.profileRepo = profileRepo;
        this.certRepo = certRepo;
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    // ===== Doktor Profili Oluştur / Güncelle =====
    public DoctorProfileResponseDTO saveProfile(String email, DoctorProfileDTO dto) {
        User user = findUserByEmail(email);
        if (user.getRole() != Role.DOCTOR) throw new RuntimeException("Sadece doktorlar profil oluşturabilir");

        DoctorProfile profile = profileRepo.findByUserId(user.getId()).orElse(new DoctorProfile());
        profile.setUser(user);
        profile.setSpecialization(dto.getSpecialization());
        profile.setTitle(dto.getTitle());
        profile.setHospital(dto.getHospital());
        profile.setDepartment(dto.getDepartment());
        profile.setBiography(dto.getBiography());
        profile.setLanguages(dto.getLanguages());
        profile.setEducation(dto.getEducation());
        profile.setExperienceYears(dto.getExperienceYears());
        profile.setConsultationFee(dto.getConsultationFee());

        // İlk kez oluşturuluyorsa PENDING
        if (profile.getId() == null) {
            profile.setApprovalStatus(DoctorApprovalStatus.PENDING);
        }

        DoctorProfile saved = profileRepo.save(profile);
        return mapToResponseDTO(saved);
    }

    // ===== Kendi Profilimi Getir =====
    public DoctorProfileResponseDTO getMyProfile(String email) {
        User user = findUserByEmail(email);
        DoctorProfile profile = profileRepo.findByUserId(user.getId()).orElse(null);
        if (profile == null) return null;
        return mapToResponseDTO(profile);
    }

    // ===== Doktor Detay (Public) =====
    public DoctorProfileResponseDTO getDoctorDetail(Long profileId) {
        DoctorProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Doktor profili bulunamadı"));
        return mapToResponseDTO(profile);
    }

    // ===== Onaylanmış Doktorları Listele (Arama) =====
    public List<DoctorProfileResponseDTO> searchDoctors(String specialization) {
        List<DoctorProfile> profiles;
        if (specialization != null && !specialization.isBlank()) {
            profiles = profileRepo.findByApprovalStatusAndSpecializationContainingIgnoreCase(
                    DoctorApprovalStatus.APPROVED, specialization);
        } else {
            profiles = profileRepo.findByApprovalStatusOrderByAverageRatingDesc(DoctorApprovalStatus.APPROVED);
        }
        return profiles.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // ===== Admin: Bekleyen Doktorları Listele =====
    public List<DoctorProfileResponseDTO> getPendingDoctors() {
        return profileRepo.findByApprovalStatus(DoctorApprovalStatus.PENDING)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // ===== Admin: Doktor Onayla =====
    @Transactional
    public DoctorProfileResponseDTO approveDoctor(Long profileId) {
        DoctorProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Doktor profili bulunamadı"));
        profile.setApprovalStatus(DoctorApprovalStatus.APPROVED);
        profile.setApprovedAt(LocalDateTime.now());
        profile.setRejectionReason(null);
        return mapToResponseDTO(profileRepo.save(profile));
    }

    // ===== Admin: Doktor Reddet =====
    @Transactional
    public DoctorProfileResponseDTO rejectDoctor(Long profileId, String reason) {
        DoctorProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Doktor profili bulunamadı"));
        profile.setApprovalStatus(DoctorApprovalStatus.REJECTED);
        profile.setRejectionReason(reason);
        return mapToResponseDTO(profileRepo.save(profile));
    }

    // ===== Sertifika Ekle =====
    public DoctorCertificateDTO addCertificate(String email, DoctorCertificateDTO dto) {
        User user = findUserByEmail(email);
        DoctorProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Önce doktor profilinizi oluşturun"));
        DoctorCertificate cert = new DoctorCertificate();
        cert.setDoctorProfile(profile);
        cert.setName(dto.getName());
        cert.setInstitution(dto.getInstitution());
        cert.setYear(dto.getYear());
        DoctorCertificate saved = certRepo.save(cert);
        return new DoctorCertificateDTO(saved.getId(), saved.getName(), saved.getInstitution(), saved.getYear());
    }

    // ===== Sertifika Sil =====
    public void deleteCertificate(String email, Long certId) {
        User user = findUserByEmail(email);
        DoctorProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profil bulunamadı"));
        DoctorCertificate cert = certRepo.findById(certId)
                .orElseThrow(() -> new RuntimeException("Sertifika bulunamadı"));
        if (!cert.getDoctorProfile().getId().equals(profile.getId()))
            throw new RuntimeException("Bu sertifika size ait değil");
        certRepo.delete(cert);
    }

    // ===== Yorum Yaz =====
    @Transactional
    public DoctorReviewDTO addReview(String patientEmail, DoctorReviewRequestDTO dto) {
        User patient = findUserByEmail(patientEmail);
        if (patient.getRole() != Role.PATIENT) throw new RuntimeException("Sadece hastalar yorum yapabilir");

        DoctorProfile profile = profileRepo.findById(dto.getDoctorProfileId())
                .orElseThrow(() -> new RuntimeException("Doktor profili bulunamadı"));

        // Aynı doktora tekrar yorum engelle
        if (reviewRepo.findByDoctorProfileIdAndPatientId(profile.getId(), patient.getId()).isPresent()) {
            throw new RuntimeException("Bu doktora zaten yorum yapmışsınız");
        }

        DoctorReview review = new DoctorReview();
        review.setDoctorProfile(profile);
        review.setPatient(patient);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        DoctorReview saved = reviewRepo.save(review);

        // Ortalama puanı güncelle
        updateAverageRating(profile.getId());

        return mapReviewToDTO(saved);
    }

    // ===== Doktorun Yorumlarını Getir =====
    public List<DoctorReviewDTO> getReviews(Long profileId) {
        return reviewRepo.findByDoctorProfileIdAndApprovedTrueOrderByCreatedAtDesc(profileId)
                .stream().map(this::mapReviewToDTO).collect(Collectors.toList());
    }

    // ===== Yardımcı =====
    private void updateAverageRating(Long profileId) {
        Double avg = reviewRepo.findAverageRatingByProfileId(profileId);
        int count = reviewRepo.countByProfileId(profileId);
        DoctorProfile profile = profileRepo.findById(profileId).orElse(null);
        if (profile != null) {
            profile.setAverageRating(avg != null ? avg : 0.0);
            profile.setTotalReviews(count);
            profileRepo.save(profile);
        }
    }

    private User findUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));
    }

    private DoctorProfileResponseDTO mapToResponseDTO(DoctorProfile p) {
        DoctorProfileResponseDTO dto = new DoctorProfileResponseDTO();
        dto.setProfileId(p.getId());
        dto.setUserId(p.getUser().getId());
        dto.setFullName(p.getUser().getFullName());
        dto.setEmail(p.getUser().getEmail());
        dto.setPhone(p.getUser().getPhone());
        dto.setProfilePhotoUrl(p.getUser().getProfilePhotoUrl());
        dto.setSpecialization(p.getSpecialization());
        dto.setTitle(p.getTitle());
        dto.setHospital(p.getHospital());
        dto.setDepartment(p.getDepartment());
        dto.setBiography(p.getBiography());
        dto.setLanguages(p.getLanguages());
        dto.setEducation(p.getEducation());
        dto.setExperienceYears(p.getExperienceYears());
        dto.setConsultationFee(p.getConsultationFee());
        dto.setApprovalStatus(p.getApprovalStatus());
        dto.setRejectionReason(p.getRejectionReason());
        dto.setAverageRating(p.getAverageRating());
        dto.setTotalReviews(p.getTotalReviews());
        // Sertifikalar
        List<DoctorCertificateDTO> certs = certRepo.findByDoctorProfileId(p.getId())
                .stream().map(c -> new DoctorCertificateDTO(c.getId(), c.getName(), c.getInstitution(), c.getYear()))
                .collect(Collectors.toList());
        dto.setCertificates(certs);
        return dto;
    }

    private DoctorReviewDTO mapReviewToDTO(DoctorReview r) {
        return new DoctorReviewDTO(r.getId(), r.getDoctorProfile().getId(),
                r.getPatient().getFullName(), r.getRating(), r.getComment(), r.getCreatedAt());
    }
}

package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.service.AuditLogService;
import com.healthtech.telehealth.service.DoctorProfileService;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor-profiles")
@Tag(name = "Doctor Profiles", description = "Doktor profil yönetimi, arama, onay ve puanlama")
public class DoctorProfileController {

    private final DoctorProfileService service;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public DoctorProfileController(DoctorProfileService service, JwtService jwtService,
                                   AuditLogService auditLogService) {
        this.service = service;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
    }

    // ===== DOKTOR KENDİ PROFİLİ =====
    @Operation(summary = "Kendi doktor profilimi getir")
    @GetMapping("/me")
    public ResponseEntity<DoctorProfileResponseDTO> getMyProfile(HttpServletRequest request) {
        return ResponseEntity.ok(service.getMyProfile(extractEmail(request)));
    }

    @Operation(summary = "Doktor profilimi oluştur/güncelle")
    @PostMapping("/me")
    public ResponseEntity<DoctorProfileResponseDTO> saveMyProfile(@RequestBody DoctorProfileDTO dto,
                                                                   HttpServletRequest request) {
        String email = extractEmail(request);
        auditLogService.log("DOCTOR_PROFILE_UPDATE", "DoctorProfile", null, email);
        return ResponseEntity.ok(service.saveProfile(email, dto));
    }

    // ===== DOKTOR ARAMA (PUBLIC — herkes görebilir) =====
    @Operation(summary = "Onaylı doktorları ara/listele")
    @GetMapping("/search")
    public ResponseEntity<List<DoctorProfileResponseDTO>> searchDoctors(
            @RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(service.searchDoctors(specialization));
    }

    @Operation(summary = "Doktor detayını getir")
    @GetMapping("/{profileId}")
    public ResponseEntity<DoctorProfileResponseDTO> getDoctorDetail(@PathVariable Long profileId) {
        return ResponseEntity.ok(service.getDoctorDetail(profileId));
    }

    // ===== ADMIN: ONAY SÜRECİ =====
    @Operation(summary = "Admin: Bekleyen doktor başvurularını listele")
    @GetMapping("/admin/pending")
    public ResponseEntity<List<DoctorProfileResponseDTO>> getPendingDoctors() {
        return ResponseEntity.ok(service.getPendingDoctors());
    }

    @Operation(summary = "Admin: Doktoru onayla")
    @PutMapping("/admin/{profileId}/approve")
    public ResponseEntity<DoctorProfileResponseDTO> approveDoctor(@PathVariable Long profileId,
                                                                   HttpServletRequest request) {
        auditLogService.log("DOCTOR_APPROVE", "DoctorProfile", profileId, extractEmail(request));
        return ResponseEntity.ok(service.approveDoctor(profileId));
    }

    @Operation(summary = "Admin: Doktoru reddet")
    @PutMapping("/admin/{profileId}/reject")
    public ResponseEntity<DoctorProfileResponseDTO> rejectDoctor(@PathVariable Long profileId,
                                                                  @RequestBody Map<String, String> body,
                                                                  HttpServletRequest request) {
        String reason = body.getOrDefault("reason", "Belirtilmedi");
        auditLogService.log("DOCTOR_REJECT", "DoctorProfile", profileId, extractEmail(request));
        return ResponseEntity.ok(service.rejectDoctor(profileId, reason));
    }

    // ===== SERTİFİKALAR =====
    @Operation(summary = "Sertifika ekle")
    @PostMapping("/certificates")
    public ResponseEntity<DoctorCertificateDTO> addCertificate(@RequestBody DoctorCertificateDTO dto,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(service.addCertificate(extractEmail(request), dto));
    }

    @Operation(summary = "Sertifika sil")
    @DeleteMapping("/certificates/{certId}")
    public ResponseEntity<Map<String, String>> deleteCertificate(@PathVariable Long certId,
                                                                  HttpServletRequest request) {
        service.deleteCertificate(extractEmail(request), certId);
        return ResponseEntity.ok(Map.of("message", "Sertifika silindi"));
    }

    // ===== YORUMLAR =====
    @Operation(summary = "Doktora yorum yaz")
    @PostMapping("/reviews")
    public ResponseEntity<DoctorReviewDTO> addReview(@Valid @RequestBody DoctorReviewRequestDTO dto,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(service.addReview(extractEmail(request), dto));
    }

    @Operation(summary = "Doktorun yorumlarını getir")
    @GetMapping("/{profileId}/reviews")
    public ResponseEntity<List<DoctorReviewDTO>> getReviews(@PathVariable Long profileId) {
        return ResponseEntity.ok(service.getReviews(profileId));
    }

    private String extractEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtService.extractEmail(authHeader.substring(7));
    }
}

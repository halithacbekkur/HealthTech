package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@Tag(name = "Medical Records", description = "Tıbbi kayıt ve laboratuvar sonuçları yönetimi")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final JwtService jwtService;

    public MedicalRecordController(MedicalRecordService medicalRecordService, JwtService jwtService) {
        this.medicalRecordService = medicalRecordService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Kendi tıbbi kaydımı görüntüle (Hasta)")
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> getMyRecord(HttpServletRequest request) {
        return ResponseEntity.ok(medicalRecordService.getRecordByPatientEmail(extractEmail(request)));
    }

    @Operation(summary = "Tıbbi kaydımı oluştur/güncelle (Hasta)")
    @PostMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> updateMyRecord(
            @RequestBody MedicalRecordRequestDTO requestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(medicalRecordService.createOrUpdateRecord(extractEmail(request), requestDTO));
    }

    @Operation(summary = "Hasta tıbbi kaydını görüntüle (Doktor)")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponseDTO> getPatientRecord(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordByPatientId(patientId));
    }

    // ===== FAZ 4: LABORATUVAR SONUÇLARI =====
    @Operation(summary = "Hasta için lab sonucu ekle (Doktor)")
    @PostMapping("/lab-results/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<LabResultDTO> addLabResult(
            @PathVariable Long patientId, @RequestBody LabResultDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(medicalRecordService.addLabResult(extractEmail(request), patientId, dto));
    }

    @Operation(summary = "Kendi lab sonuçlarımı getir (Hasta)")
    @GetMapping("/lab-results/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<LabResultDTO>> getMyLabResults(HttpServletRequest request) {
        return ResponseEntity.ok(medicalRecordService.getMyLabResults(extractEmail(request)));
    }

    @Operation(summary = "Hasta lab sonuçlarını getir (Doktor)")
    @GetMapping("/lab-results/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<LabResultDTO>> getPatientLabResults(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientLabResults(patientId));
    }

    private String extractEmail(HttpServletRequest request) {
        return jwtService.extractEmail(request.getHeader("Authorization").substring(7));
    }
}

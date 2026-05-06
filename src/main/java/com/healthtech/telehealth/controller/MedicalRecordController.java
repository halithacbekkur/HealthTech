package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.MedicalRecordRequestDTO;
import com.healthtech.telehealth.dto.MedicalRecordResponseDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.MedicalRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final JwtService jwtService;

    public MedicalRecordController(MedicalRecordService medicalRecordService, JwtService jwtService) {
        this.medicalRecordService = medicalRecordService;
        this.jwtService = jwtService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> getMyRecord(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(medicalRecordService.getRecordByPatientEmail(email));
    }

    @PostMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> updateMyRecord(
            @RequestBody MedicalRecordRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(medicalRecordService.createOrUpdateRecord(email, requestDTO));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponseDTO> getPatientRecord(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordByPatientId(patientId));
    }

    private String extractEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractEmail(token);
    }
}

package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.PrescriptionRequestDTO;
import com.healthtech.telehealth.dto.PrescriptionResponseDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescription Management", description = "Reçete oluşturma, listeleme ve durum yönetimi")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final JwtService jwtService;

    public PrescriptionController(PrescriptionService prescriptionService, JwtService jwtService) {
        this.prescriptionService = prescriptionService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Reçete oluştur (Doktor)")
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @RequestBody PrescriptionRequestDTO requestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(prescriptionService.createPrescription(extractEmail(request), requestDTO));
    }

    @Operation(summary = "Reçetelerimi görüntüle (Hasta)")
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getMyPrescriptions(HttpServletRequest request) {
        return ResponseEntity.ok(prescriptionService.getMyPrescriptions(extractEmail(request)));
    }

    @Operation(summary = "Yazdığım reçeteler (Doktor)")
    @GetMapping("/doctor/my")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getDoctorPrescriptions(HttpServletRequest request) {
        return ResponseEntity.ok(prescriptionService.getDoctorPrescriptions(extractEmail(request)));
    }

    @Operation(summary = "Reçete durumunu güncelle")
    @PutMapping("/{id}/status")
    public ResponseEntity<PrescriptionResponseDTO> updateStatus(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(prescriptionService.updateStatus(id, body.get("status")));
    }

    private String extractEmail(HttpServletRequest request) {
        return jwtService.extractEmail(request.getHeader("Authorization").substring(7));
    }
}

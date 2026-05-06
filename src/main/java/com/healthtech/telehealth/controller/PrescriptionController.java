package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.PrescriptionRequestDTO;
import com.healthtech.telehealth.dto.PrescriptionResponseDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.PrescriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final JwtService jwtService;

    public PrescriptionController(PrescriptionService prescriptionService, JwtService jwtService) {
        this.prescriptionService = prescriptionService;
        this.jwtService = jwtService;
    }

    // Sadece Doktorlar recete olusturabilir
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @RequestBody PrescriptionRequestDTO requestDTO,
            HttpServletRequest request) {
        String doctorEmail = extractEmailFromToken(request);
        return ResponseEntity.ok(prescriptionService.createPrescription(doctorEmail, requestDTO));
    }

    // Sadece Hastalar kendi recetelerini gorebilir
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getMyPrescriptions(HttpServletRequest request) {
        String patientEmail = extractEmailFromToken(request);
        return ResponseEntity.ok(prescriptionService.getMyPrescriptions(patientEmail));
    }

    // JWT token'dan email cikaran yardimci metod
    private String extractEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractEmail(token);
    }
}

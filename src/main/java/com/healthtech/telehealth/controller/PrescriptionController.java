package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.PrescriptionRequestDTO;
import com.healthtech.telehealth.dto.PrescriptionResponseDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescription Management", description = "Reçete oluşturma ve görüntüleme (rol bazlı erişim)")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final JwtService jwtService;

    public PrescriptionController(PrescriptionService prescriptionService, JwtService jwtService) {
        this.prescriptionService = prescriptionService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Reçete oluştur (Doktor)", description = "Doktor, bir randevuya bağlı reçete oluşturur. İlaç adı, dozaj ve kullanım talimatı girer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reçete başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz reçete bilgisi"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim – sadece DOCTOR rolü"),
            @ApiResponse(responseCode = "404", description = "Randevu bulunamadı")
    })
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @RequestBody PrescriptionRequestDTO requestDTO,
            HttpServletRequest request) {
        String doctorEmail = extractEmailFromToken(request);
        return ResponseEntity.ok(prescriptionService.createPrescription(doctorEmail, requestDTO));
    }

    @Operation(summary = "Reçetelerimi görüntüle (Hasta)", description = "Giriş yapan hastanın tüm reçetelerini listeler")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reçete listesi başarıyla döndü"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim – sadece PATIENT rolü")
    })
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

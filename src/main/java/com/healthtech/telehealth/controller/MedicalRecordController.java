package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.MedicalRecordRequestDTO;
import com.healthtech.telehealth.dto.MedicalRecordResponseDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@Tag(name = "Medical Records", description = "Tıbbi kayıt görüntüleme ve yönetimi (rol bazlı erişim)")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final JwtService jwtService;

    public MedicalRecordController(MedicalRecordService medicalRecordService, JwtService jwtService) {
        this.medicalRecordService = medicalRecordService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Kendi tıbbi kaydımı görüntüle (Hasta)", description = "Giriş yapan hastanın kendi tıbbi kaydını getirir")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tıbbi kayıt başarıyla döndü"),
            @ApiResponse(responseCode = "404", description = "Tıbbi kayıt bulunamadı"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim – sadece PATIENT rolü")
    })
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> getMyRecord(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(medicalRecordService.getRecordByPatientEmail(email));
    }

    @Operation(summary = "Tıbbi kaydımı oluştur/güncelle (Hasta)", description = "Hastanın kendi tıbbi kaydını oluşturur veya günceller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tıbbi kayıt başarıyla kaydedildi"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim – sadece PATIENT rolü")
    })
    @PostMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordResponseDTO> updateMyRecord(
            @RequestBody MedicalRecordRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(medicalRecordService.createOrUpdateRecord(email, requestDTO));
    }

    @Operation(summary = "Hasta tıbbi kaydını görüntüle (Doktor)", description = "Doktor, belirtilen hastanın tıbbi kaydını görüntüler")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hasta tıbbi kaydı başarıyla döndü"),
            @ApiResponse(responseCode = "404", description = "Hasta veya tıbbi kayıt bulunamadı"),
            @ApiResponse(responseCode = "403", description = "Yetkisiz erişim – sadece DOCTOR rolü")
    })
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

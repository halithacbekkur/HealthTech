package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.EmergencyCreateDTO;
import com.healthtech.telehealth.dto.EmergencyRequestDTO;
import com.healthtech.telehealth.service.EmergencyService;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency")
@Tag(name = "Emergency", description = "Acil durum yönetimi")
public class EmergencyController {

    private final EmergencyService emergencyService;
    private final JwtService jwtService;

    public EmergencyController(EmergencyService emergencyService, JwtService jwtService) {
        this.emergencyService = emergencyService;
        this.jwtService = jwtService;
    }

    @PostMapping
    @Operation(summary = "Acil durum talebi oluştur (Hasta — SOS)")
    public ResponseEntity<EmergencyRequestDTO> createEmergency(
            @RequestHeader("Authorization") String token,
            @RequestBody EmergencyCreateDTO dto) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.createEmergency(email, dto));
    }

    @PutMapping("/{id}/respond")
    @Operation(summary = "Acil talebi üstlen (Doktor)")
    public ResponseEntity<EmergencyRequestDTO> respondToEmergency(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.respondToEmergency(id, email));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Acil durumu çöz (Doktor)")
    public ResponseEntity<EmergencyRequestDTO> resolveEmergency(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.resolveEmergency(id, body.get("notes"), email));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Acil talebi iptal et (Hasta)")
    public ResponseEntity<EmergencyRequestDTO> cancelEmergency(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.cancelEmergency(id, email));
    }

    @GetMapping("/my")
    @Operation(summary = "Benim acil taleplerim (Hasta)")
    public ResponseEntity<List<EmergencyRequestDTO>> getMyEmergencies(
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.getMyEmergencies(email));
    }

    @GetMapping("/pending")
    @Operation(summary = "Bekleyen acil talepler (Doktor/Admin)")
    public ResponseEntity<List<EmergencyRequestDTO>> getPendingEmergencies() {
        return ResponseEntity.ok(emergencyService.getPendingEmergencies());
    }

    @GetMapping("/all")
    @Operation(summary = "Tüm acil talepler (Admin)")
    public ResponseEntity<List<EmergencyRequestDTO>> getAllEmergencies() {
        return ResponseEntity.ok(emergencyService.getAllEmergencies());
    }

    @GetMapping("/assigned")
    @Operation(summary = "Üstlendiğim acil talepler (Doktor)")
    public ResponseEntity<List<EmergencyRequestDTO>> getMyAssignedEmergencies(
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(emergencyService.getMyAssignedEmergencies(email));
    }

    @GetMapping("/pending-count")
    @Operation(summary = "Bekleyen acil talep sayısı")
    public ResponseEntity<Map<String, Long>> getPendingCount() {
        return ResponseEntity.ok(Map.of("count", emergencyService.getPendingCount()));
    }
}

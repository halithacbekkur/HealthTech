package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.AppointmentRequestDTO;
import com.healthtech.telehealth.dto.AppointmentResponseDTO;
import com.healthtech.telehealth.service.AppointmentService;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Management", description = "Randevu oluşturma, listeleme, onaylama ve iptal işlemleri")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtService jwtService;

    public AppointmentController(AppointmentService appointmentService, JwtService jwtService) {
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Yeni randevu oluştur", description = "Giriş yapan hasta, belirtilen doktora randevu oluşturur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Randevu başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz randevu bilgisi"),
            @ApiResponse(responseCode = "404", description = "Doktor bulunamadı")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO requestDTO,
            HttpServletRequest request) {

        // JWT token'dan giriş yapan kullanıcının email'ini çıkar
        String email = extractEmailFromToken(request);

        AppointmentResponseDTO response = appointmentService.createAppointment(email, requestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Hastanın randevularını listele", description = "Belirtilen hasta ID'sine ait tüm randevuları getirir")
    @ApiResponse(responseCode = "200", description = "Randevu listesi başarıyla döndü")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(
            @RequestParam Long patientId) {

        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Doktorun randevularını listele", description = "Belirtilen doktor ID'sine ait tüm randevuları getirir")
    @ApiResponse(responseCode = "200", description = "Doktor randevu listesi başarıyla döndü")
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments(
            @RequestParam Long doctorId) {

        List<AppointmentResponseDTO> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Randevu iptal et", description = "Belirtilen ID'ye sahip randevuyu iptal eder (durum: CANCELLED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Randevu başarıyla iptal edildi"),
            @ApiResponse(responseCode = "404", description = "Randevu bulunamadı")
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {

        AppointmentResponseDTO response = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Randevu onayla (Doktor)", description = "Doktor, bekleyen randevuyu onaylar (durum: APPROVED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Randevu başarıyla onaylandı"),
            @ApiResponse(responseCode = "404", description = "Randevu bulunamadı")
    })
    @PutMapping("/{id}/approve")
    public ResponseEntity<AppointmentResponseDTO> approveAppointment(@PathVariable Long id) {

        AppointmentResponseDTO response = appointmentService.approveAppointment(id);
        return ResponseEntity.ok(response);
    }

    // JWT token'dan email çıkaran yardımcı metod
    private String extractEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // "Bearer " kısmını atla (7 karakter)
        return jwtService.extractEmail(token);
    }
}

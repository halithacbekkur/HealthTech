package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.AppointmentRequestDTO;
import com.healthtech.telehealth.dto.AppointmentResponseDTO;
import com.healthtech.telehealth.service.AppointmentService;
import com.healthtech.telehealth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtService jwtService;

    public AppointmentController(AppointmentService appointmentService, JwtService jwtService) {
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    // POST /api/appointments → Yeni randevu oluştur
    // Giriş yapan hasta, bir doktora randevu alır
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO requestDTO,
            HttpServletRequest request) {

        // JWT token'dan giriş yapan kullanıcının email'ini çıkar
        String email = extractEmailFromToken(request);

        AppointmentResponseDTO response = appointmentService.createAppointment(email, requestDTO);
        return ResponseEntity.ok(response);
    }

    // GET /api/appointments/my → Giriş yapan hastanın kendi randevularını getir
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(
            @RequestParam Long patientId) {

        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    // GET /api/appointments/doctor → Doktorun randevularını getir
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments(
            @RequestParam Long doctorId) {

        List<AppointmentResponseDTO> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // PUT /api/appointments/{id}/cancel → Randevuyu iptal et
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {

        AppointmentResponseDTO response = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(response);
    }

    // PUT /api/appointments/{id}/approve → Doktor randevuyu onaylar
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

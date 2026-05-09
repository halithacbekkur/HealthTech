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

    @Operation(summary = "Yeni randevu oluştur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Randevu oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz bilgi"),
            @ApiResponse(responseCode = "404", description = "Doktor bulunamadı")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = extractEmail(request);
        return ResponseEntity.ok(appointmentService.createAppointment(email, requestDTO));
    }

    @Operation(summary = "Kendi randevularımı getir (hasta — JWT'den güvenli)")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(HttpServletRequest request) {
        String email = extractEmail(request);
        return ResponseEntity.ok(appointmentService.getPatientAppointmentsByEmail(email));
    }

    @Operation(summary = "Doktorun kendi randevuları (JWT'den güvenli)")
    @GetMapping("/doctor/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyDoctorAppointments(HttpServletRequest request) {
        String email = extractEmail(request);
        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByEmail(email));
    }

    @Operation(summary = "ID ile hasta randevuları (admin)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    @Operation(summary = "ID ile doktor randevuları (admin)")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    @Operation(summary = "Randevu iptal et")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @Operation(summary = "Randevu onayla (Doktor)")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AppointmentResponseDTO> approveAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.approveAppointment(id));
    }

    @Operation(summary = "Randevu tamamla (Doktor)")
    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponseDTO> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }

    private String extractEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtService.extractEmail(authHeader.substring(7));
    }
}

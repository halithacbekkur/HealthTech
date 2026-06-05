package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.service.AppointmentService;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Management", description = "Randevu oluşturma, takvim, müsaitlik, onay ve erteleme")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtService jwtService;

    public AppointmentController(AppointmentService appointmentService, JwtService jwtService) {
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    // ===== RANDEVU CRUD =====
    @Operation(summary = "Yeni randevu oluştur (çakışma kontrolü ile)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Randevu oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Çakışma veya geçersiz bilgi"),
            @ApiResponse(responseCode = "404", description = "Doktor bulunamadı")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO requestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(extractEmail(request), requestDTO));
    }

    @Operation(summary = "Kendi randevularımı getir (hasta)")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.getPatientAppointmentsByEmail(extractEmail(request)));
    }

    @Operation(summary = "Doktorun kendi randevuları")
    @GetMapping("/doctor/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyDoctorAppointments(HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByEmail(extractEmail(request)));
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

    // ===== FAZ 3: ERTELEME =====
    @Operation(summary = "Randevu ertele (tarih güncelle)")
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        LocalDateTime newDate = LocalDateTime.parse(body.get("newDate"));
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, newDate));
    }

    // ===== FAZ 3: DOKTOR TAKVİMİ =====
    @Operation(summary = "Doktorun haftalık çalışma takvimini getir")
    @GetMapping("/schedule/{doctorId}")
    public ResponseEntity<List<DoctorScheduleDTO>> getDoctorSchedule(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorSchedule(doctorId));
    }

    @Operation(summary = "Doktor takvim ayarla/güncelle")
    @PostMapping("/schedule")
    public ResponseEntity<DoctorScheduleDTO> saveSchedule(
            @RequestBody DoctorScheduleDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.saveSchedule(extractEmail(request), dto));
    }

    // ===== FAZ 3: MÜSAİTLİK =====
    @Operation(summary = "Doktorun belirli gündeki müsait saatlerini getir")
    @GetMapping("/available-slots/{doctorId}")
    public ResponseEntity<List<AvailableSlotDTO>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailableSlots(doctorId, date));
    }

    @Operation(summary = "En yakın müsait randevu önerisi (ilk boş gün ve saat)")
    @GetMapping("/nearest-slot/{doctorId}")
    public ResponseEntity<Map<String, Object>> getNearestSlot(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getNearestAvailableSlot(doctorId));
    }

    @Operation(summary = "Doktorun haftalık doluluk özeti (takvim görünümü için)")
    @GetMapping("/week-availability/{doctorId}")
    public ResponseEntity<List<Map<String, Object>>> getWeekAvailability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(appointmentService.getWeekAvailability(doctorId, startDate));
    }

    // ===== FAZ 3+: İZİN GÜNÜ YÖNETİMİ =====
    @Operation(summary = "Doktor izin günü ekle")
    @PostMapping("/day-off")
    public ResponseEntity<Map<String, Object>> addDayOff(
            HttpServletRequest request,
            @RequestBody Map<String, String> body) {
        LocalDate offDate = LocalDate.parse(body.get("date"));
        String reason = body.getOrDefault("reason", "");
        return ResponseEntity.ok(appointmentService.addDayOff(extractEmail(request), offDate, reason));
    }

    @Operation(summary = "Doktor izin günü kaldır")
    @DeleteMapping("/day-off")
    public ResponseEntity<Map<String, Object>> removeDayOff(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.removeDayOff(extractEmail(request), date));
    }

    @Operation(summary = "Doktorun gelecek izin günlerini listele")
    @GetMapping("/day-offs")
    public ResponseEntity<List<Map<String, Object>>> getDayOffs(HttpServletRequest request) {
        return ResponseEntity.ok(appointmentService.getDayOffs(extractEmail(request)));
    }

    private String extractEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtService.extractEmail(authHeader.substring(7));
    }
}

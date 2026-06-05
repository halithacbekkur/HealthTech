package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.AuditLogDTO;
import com.healthtech.telehealth.dto.ReportDTO;
import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Faz 11: Gelişmiş Admin Paneli — Raporlama, Kullanıcı Yönetimi, Audit Log
 * Sadece ADMIN rolü erişebilir (SecurityConfig'de kontrol).
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Panel", description = "Gelişmiş yönetim ve raporlama endpoint'leri (Sadece Admin)")
public class AdminController {

    private final ReportService reportService;
    private final UserService userService;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AdminController(ReportService reportService,
                           UserService userService,
                           AuditLogService auditLogService,
                           UserRepository userRepository,
                           NotificationService notificationService) {
        this.reportService = reportService;
        this.userService = userService;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // ===== RAPORLAMA =====
    @Operation(summary = "Sistem raporu", description = "Tüm sistem istatistiklerini döner")
    @GetMapping("/report")
    public ResponseEntity<ReportDTO> getReport() {
        return ResponseEntity.ok(reportService.getSystemReport());
    }

    // ===== KULLANICI YÖNETİMİ =====
    @Operation(summary = "Tüm kullanıcılar", description = "Sistemdeki tüm kullanıcıları listeler")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Rol bazlı kullanıcılar", description = "Belirli bir roldeki kullanıcıları listeler")
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role) {
        try {
            Role r = Role.valueOf(role.toUpperCase());
            List<UserResponseDTO> users = userRepository.findByRole(r).stream()
                    .map(this::toUserDTO).collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Kullanıcı durumu değiştir", description = "Kullanıcıyı aktif/pasif/askıda yapabilir")
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<Map<String, String>> changeUserStatus(
            @PathVariable Long userId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        try {
            AccountStatus newStatus = AccountStatus.valueOf(status.toUpperCase());
            user.setAccountStatus(newStatus);
            userRepository.save(user);

            // Bildirim gönder
            String msg = newStatus == AccountStatus.ACTIVE
                    ? "Hesabınız aktif edildi."
                    : newStatus == AccountStatus.FROZEN
                    ? "Hesabınız donduruldu."
                    : "Hesabınız devre dışı bırakıldı.";
            notificationService.sendSystemNotification(userId, msg);

            return ResponseEntity.ok(Map.of("message", "Kullanıcı durumu güncellendi: " + newStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Geçersiz durum: " + status));
        }
    }

    @Operation(summary = "Kullanıcı rolü değiştir", description = "Kullanıcının rolünü değiştirir")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Map<String, String>> changeUserRole(
            @PathVariable Long userId, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        try {
            Role newRole = Role.valueOf(role.toUpperCase());
            user.setRole(newRole);
            userRepository.save(user);

            notificationService.sendSystemNotification(userId,
                    "Rolünüz " + newRole + " olarak güncellendi.");

            return ResponseEntity.ok(Map.of("message", "Kullanıcı rolü güncellendi: " + newRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Geçersiz rol: " + role));
        }
    }

    @Operation(summary = "Kullanıcı sil", description = "Kullanıcıyı sistemden siler")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "Kullanıcı silindi"));
    }

    // ===== AUDİT LOG =====
    @Operation(summary = "Son işlem logları", description = "Son 50 işlem logunu döner")
    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs() {
        List<AuditLogDTO> logs = auditLogService.getRecentLogs().stream()
                .map(this::toAuditDTO).collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Kullanıcıya göre loglar", description = "Belirli kullanıcının işlem loglarını döner")
    @GetMapping("/audit-logs/user/{email}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUser(@PathVariable String email) {
        List<AuditLogDTO> logs = auditLogService.getLogsByUser(email).stream()
                .map(this::toAuditDTO).collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ===== SİSTEM BİLDİRİMİ =====
    @Operation(summary = "Toplu bildirim gönder", description = "Tüm kullanıcılara veya belirli role bildirim gönderir")
    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, String>> broadcastNotification(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String targetRole = body.getOrDefault("role", "ALL");

        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mesaj boş olamaz"));
        }

        List<User> targets;
        if ("ALL".equalsIgnoreCase(targetRole)) {
            targets = userRepository.findAll();
        } else {
            try {
                targets = userRepository.findByRole(Role.valueOf(targetRole.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Geçersiz rol: " + targetRole));
            }
        }

        for (User u : targets) {
            notificationService.send(u.getId(), "📢 Sistem Duyurusu", message,
                    "BROADCAST", "fa-bullhorn", null);
        }

        return ResponseEntity.ok(Map.of("message", targets.size() + " kullanıcıya bildirim gönderildi"));
    }

    // ===== DTO Dönüştürücüler =====
    private UserResponseDTO toUserDTO(User u) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(u.getId());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setRole(u.getRole());
        dto.setAccountStatus(u.getAccountStatus());
        dto.setCreatedAt(u.getCreatedAt());
        dto.setLastLoginAt(u.getLastLoginAt());
        dto.setEmailVerified(u.isEmailVerified());
        return dto;
    }

    private AuditLogDTO toAuditDTO(AuditLog log) {
        return new AuditLogDTO(
                log.getId(), log.getAction(), log.getEntityType(), log.getEntityId(),
                log.getPerformedBy(), log.getIpAddress(), log.getUserAgent(),
                log.getDetails(), log.getCreatedAt()
        );
    }
}

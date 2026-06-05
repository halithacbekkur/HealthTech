package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.NotificationDTO;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Bildirim listeleme, okundu işaretleme")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final UserRepository userRepo;

    public NotificationController(NotificationService notificationService,
                                  JwtService jwtService, UserRepository userRepo) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Operation(summary = "Son 20 bildirimimi getir")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.getRecentNotifications(getUserId(request)));
    }

    @Operation(summary = "Okunmamış bildirim sayısı")
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(getUserId(request))));
    }

    @Operation(summary = "Bildirimi okundu işaretle")
    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long id, HttpServletRequest request) {
        notificationService.markAsRead(id, getUserId(request));
        return ResponseEntity.ok(Map.of("message", "Okundu"));
    }

    @Operation(summary = "Tüm bildirimleri okundu işaretle")
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(HttpServletRequest request) {
        notificationService.markAllAsRead(getUserId(request));
        return ResponseEntity.ok(Map.of("message", "Tümü okundu"));
    }

    private Long getUserId(HttpServletRequest request) {
        String email = jwtService.extractEmail(request.getHeader("Authorization").substring(7));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));
        return user.getId();
    }
}

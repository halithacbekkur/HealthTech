package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.NotificationDTO;
import com.healthtech.telehealth.entity.Notification;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.repository.NotificationRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bildirim servisi — Uygulama içi bildirim oluşturma ve yönetimi.
 * Randevu, doktor onay, sistem bildirimleri burada oluşturulur.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;

    public NotificationService(NotificationRepository notificationRepo, UserRepository userRepo) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
    }

    // ===== BİLDİRİM OLUŞTURMA =====
    public void send(Long userId, String title, String message, String type, String icon, String actionUrl) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return;

        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setIcon(icon != null ? icon : "fa-bell");
        n.setActionUrl(actionUrl);
        notificationRepo.save(n);
    }

    // Kısa yardımcılar
    public void sendAppointmentNotification(Long userId, String message) {
        send(userId, "Randevu Bildirimi", message, "APPOINTMENT", "fa-calendar-check", "dashboard");
    }

    public void sendDoctorApprovalNotification(Long userId, String message) {
        send(userId, "Doktor Onay", message, "DOCTOR_APPROVAL", "fa-user-check", "doctor-profile");
    }

    public void sendSystemNotification(Long userId, String message) {
        send(userId, "Sistem Bildirimi", message, "SYSTEM", "fa-circle-info", null);
    }

    public void sendReviewNotification(Long userId, String message) {
        send(userId, "Yeni Yorum", message, "REVIEW", "fa-star", "doctor-profile");
    }

    // ===== BİLDİRİM LİSTELEME =====
    public List<NotificationDTO> getRecentNotifications(Long userId) {
        return notificationRepo.findTop20ByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getAllNotifications(Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        return notificationRepo.countByUserIdAndReadFalse(userId);
    }

    // ===== OKUNDU İŞARETLE =====
    public void markAsRead(Long notificationId, Long userId) {
        Notification n = notificationRepo.findById(notificationId).orElse(null);
        if (n != null && n.getUser().getId().equals(userId)) {
            n.setRead(true);
            notificationRepo.save(n);
        }
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().filter(n -> !n.isRead()).collect(Collectors.toList());
        unread.forEach(n -> n.setRead(true));
        notificationRepo.saveAll(unread);
    }

    private NotificationDTO toDTO(Notification n) {
        return new NotificationDTO(n.getId(), n.getTitle(), n.getMessage(),
                n.getType(), n.getActionUrl(), n.getIcon(), n.isRead(), n.getCreatedAt());
    }
}

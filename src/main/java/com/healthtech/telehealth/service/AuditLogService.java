package com.healthtech.telehealth.service;

import com.healthtech.telehealth.entity.AuditLog;
import com.healthtech.telehealth.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * İşlem log servisi — Tüm kritik işlemleri kaydeder (KVKK uyumu).
 */
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entityType, Long entityId,
                    String performedBy, String ipAddress, String userAgent, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setPerformedBy(performedBy);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    public void log(String action, String entityType, Long entityId, String performedBy) {
        log(action, entityType, entityId, performedBy, null, null, null);
    }

    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public List<AuditLog> getLogsByUser(String email) {
        return auditLogRepository.findByPerformedByOrderByCreatedAtDesc(email);
    }
}

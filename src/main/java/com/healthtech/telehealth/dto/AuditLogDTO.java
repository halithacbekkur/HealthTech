package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDTO {
    private Long id;
    private String action;
    private String entityType;
    private Long entityId;
    private String performedBy;
    private String ipAddress;
    private String userAgent;
    private String details;
    private LocalDateTime createdAt;
}

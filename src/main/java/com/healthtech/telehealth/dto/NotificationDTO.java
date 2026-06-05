package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String type;
    private String actionUrl;
    private String icon;
    private boolean read;
    private LocalDateTime createdAt;
}

package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Konuşma listesi — Her konuşma partneri için özet.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {
    private Long partnerId;
    private String partnerName;
    private String partnerRole;
    private String lastMessage;
    private String lastMessageTime;
    private long unreadCount;
}

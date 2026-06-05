package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.ConversationDTO;
import com.healthtech.telehealth.dto.MessageDTO;
import com.healthtech.telehealth.entity.Message;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.MessageRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public MessageService(MessageRepository messageRepo, UserRepository userRepo,
                          NotificationService notificationService) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    // Mesaj gönder
    public MessageDTO sendMessage(String senderEmail, Long receiverId, String content) {
        User sender = findByEmail(senderEmail);
        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Alıcı bulunamadı"));

        if (content == null || content.isBlank())
            throw new RuntimeException("Mesaj boş olamaz");

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content.trim());
        Message saved = messageRepo.save(msg);

        // Bildirim gönder
        notificationService.send(receiverId, "Yeni Mesaj",
                sender.getFullName() + ": " + (content.length() > 50 ? content.substring(0, 50) + "..." : content),
                "MESSAGE", "fa-comment-dots", "messages");

        return toDTO(saved, sender.getId());
    }

    // İki kişi arasındaki konuşmayı getir + okundu işaretle
    @Transactional
    public List<MessageDTO> getConversation(String myEmail, Long partnerId) {
        User me = findByEmail(myEmail);
        List<Message> messages = messageRepo.findConversation(me.getId(), partnerId);

        // Karşı taraftan gelenleri okundu işaretle
        messages.stream()
                .filter(m -> m.getReceiver().getId().equals(me.getId()) && !m.isRead())
                .forEach(m -> { m.setRead(true); messageRepo.save(m); });

        return messages.stream().map(m -> toDTO(m, me.getId())).collect(Collectors.toList());
    }

    // Konuşma listesi
    public List<ConversationDTO> getConversations(String myEmail) {
        User me = findByEmail(myEmail);
        List<Long> partnerIds = messageRepo.findConversationPartnerIds(me.getId());

        List<ConversationDTO> conversations = new ArrayList<>();
        for (Long partnerId : partnerIds) {
            User partner = userRepo.findById(partnerId).orElse(null);
            if (partner == null) continue;

            List<Message> msgs = messageRepo.findConversation(me.getId(), partnerId);
            Message last = msgs.isEmpty() ? null : msgs.get(msgs.size() - 1);
            long unread = messageRepo.countBySenderIdAndReceiverIdAndReadFalse(partnerId, me.getId());

            ConversationDTO dto = new ConversationDTO();
            dto.setPartnerId(partnerId);
            dto.setPartnerName(partner.getFullName());
            dto.setPartnerRole(partner.getRole().name());
            dto.setLastMessage(last != null ? (last.getContent().length() > 60 ?
                    last.getContent().substring(0, 60) + "..." : last.getContent()) : "");
            dto.setLastMessageTime(last != null ?
                    last.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM HH:mm")) : "");
            dto.setUnreadCount(unread);
            conversations.add(dto);
        }

        // Son mesaja göre sırala (en yeni en üstte)
        conversations.sort((a, b) -> b.getLastMessageTime().compareTo(a.getLastMessageTime()));
        return conversations;
    }

    // Toplam okunmamış mesaj sayısı
    public long getUnreadCount(String email) {
        User me = findByEmail(email);
        return messageRepo.countByReceiverIdAndReadFalse(me.getId());
    }

    private User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));
    }

    private MessageDTO toDTO(Message m, Long myId) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setSenderId(m.getSender().getId());
        dto.setSenderName(m.getSender().getFullName());
        dto.setReceiverId(m.getReceiver().getId());
        dto.setReceiverName(m.getReceiver().getFullName());
        dto.setContent(m.getContent());
        dto.setRead(m.isRead());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setMine(m.getSender().getId().equals(myId));
        return dto;
    }
}

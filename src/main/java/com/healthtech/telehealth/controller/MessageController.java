package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.ConversationDTO;
import com.healthtech.telehealth.dto.MessageDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messaging", description = "Doktor-hasta mesajlaşma sistemi")
public class MessageController {

    private final MessageService messageService;
    private final JwtService jwtService;

    public MessageController(MessageService messageService, JwtService jwtService) {
        this.messageService = messageService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Mesaj gönder")
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody Map<String, Object> body,
                                                   HttpServletRequest request) {
        Long receiverId = Long.valueOf(body.get("receiverId").toString());
        String content = body.get("content").toString();
        return ResponseEntity.ok(messageService.sendMessage(extractEmail(request), receiverId, content));
    }

    @Operation(summary = "Konuşma geçmişini getir (partner ile)")
    @GetMapping("/conversation/{partnerId}")
    public ResponseEntity<List<MessageDTO>> getConversation(@PathVariable Long partnerId,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(messageService.getConversation(extractEmail(request), partnerId));
    }

    @Operation(summary = "Tüm konuşmalarımı listele")
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getConversations(HttpServletRequest request) {
        return ResponseEntity.ok(messageService.getConversations(extractEmail(request)));
    }

    @Operation(summary = "Okunmamış mesaj sayısı")
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("count", messageService.getUnreadCount(extractEmail(request))));
    }

    private String extractEmail(HttpServletRequest request) {
        return jwtService.extractEmail(request.getHeader("Authorization").substring(7));
    }
}

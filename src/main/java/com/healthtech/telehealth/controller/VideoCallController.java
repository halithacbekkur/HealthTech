package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.VideoCallDTO;
import com.healthtech.telehealth.dto.VideoCallRequestDTO;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.VideoCallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/video-calls")
@Tag(name = "Video Calls", description = "Video görüşme yönetimi")
public class VideoCallController {

    private final VideoCallService videoCallService;
    private final JwtService jwtService;

    public VideoCallController(VideoCallService videoCallService, JwtService jwtService) {
        this.videoCallService = videoCallService;
        this.jwtService = jwtService;
    }

    @PostMapping
    @Operation(summary = "Video görüşme odası oluştur (Doktor)")
    public ResponseEntity<VideoCallDTO> createCall(
            @RequestHeader("Authorization") String token,
            @RequestBody VideoCallRequestDTO request) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.createVideoCall(email, request));
    }

    @PostMapping("/join/{roomId}")
    @Operation(summary = "Video görüşmeye katıl")
    public ResponseEntity<VideoCallDTO> joinCall(
            @PathVariable String roomId,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.joinCall(roomId, email));
    }

    @PostMapping("/end/{roomId}")
    @Operation(summary = "Video görüşmeyi sonlandır")
    public ResponseEntity<VideoCallDTO> endCall(
            @PathVariable String roomId,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.endCall(roomId, email));
    }

    @PutMapping("/{id}/notes")
    @Operation(summary = "Görüşme notlarını kaydet (Doktor)")
    public ResponseEntity<VideoCallDTO> saveNotes(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.saveNotes(id, body.get("notes"), email));
    }

    @GetMapping("/my")
    @Operation(summary = "Tüm görüşmelerimi getir")
    public ResponseEntity<List<VideoCallDTO>> getMyCalls(
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.getMyCalls(email));
    }

    @GetMapping("/active")
    @Operation(summary = "Aktif görüşmelerimi getir")
    public ResponseEntity<List<VideoCallDTO>> getActiveCalls(
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(videoCallService.getActiveCalls(email));
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "Oda bilgilerini getir")
    public ResponseEntity<VideoCallDTO> getCallByRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(videoCallService.getCallByRoom(roomId));
    }
}

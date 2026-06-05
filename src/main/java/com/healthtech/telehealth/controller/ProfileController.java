package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.service.AuditLogService;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Profil Yönetim Controller — Adres, acil durum kişisi, sigorta, hesap yönetimi.
 */
@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Management", description = "Profil güncelleme, adres, acil durum kişisi, sigorta ve hesap yönetimi")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public ProfileController(ProfileService profileService, JwtService jwtService,
                             AuditLogService auditLogService) {
        this.profileService = profileService;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
    }

    // ===== PROFİL =====
    @Operation(summary = "Detaylı profil bilgileri")
    @ApiResponse(responseCode = "200", description = "Profil bilgileri döndü")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(HttpServletRequest request) {
        String email = extractEmail(request);
        return ResponseEntity.ok(profileService.getFullProfile(email));
    }

    @Operation(summary = "Profil güncelle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil güncellendi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody ProfileUpdateDTO dto,
                                                          HttpServletRequest request) {
        String email = extractEmail(request);
        auditLogService.log("PROFILE_UPDATE", "User", null, email);
        return ResponseEntity.ok(profileService.updateProfile(email, dto));
    }

    // ===== ADRES =====
    @Operation(summary = "Adres bilgilerini getir")
    @GetMapping("/address")
    public ResponseEntity<AddressDTO> getAddress(HttpServletRequest request) {
        String email = extractEmail(request);
        AddressDTO dto = profileService.getAddress(email);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Adres bilgilerini kaydet / güncelle")
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> saveAddress(@RequestBody AddressDTO dto,
                                                   HttpServletRequest request) {
        String email = extractEmail(request);
        auditLogService.log("ADDRESS_UPDATE", "Address", null, email);
        return ResponseEntity.ok(profileService.saveAddress(email, dto));
    }

    // ===== ACİL DURUM KİŞİLERİ =====
    @Operation(summary = "Acil durum kişilerini listele")
    @GetMapping("/emergency-contacts")
    public ResponseEntity<List<EmergencyContactDTO>> getEmergencyContacts(HttpServletRequest request) {
        String email = extractEmail(request);
        return ResponseEntity.ok(profileService.getEmergencyContacts(email));
    }

    @Operation(summary = "Yeni acil durum kişisi ekle")
    @PostMapping("/emergency-contacts")
    public ResponseEntity<EmergencyContactDTO> addEmergencyContact(@RequestBody EmergencyContactDTO dto,
                                                                     HttpServletRequest request) {
        String email = extractEmail(request);
        auditLogService.log("EMERGENCY_CONTACT_ADD", "EmergencyContact", null, email);
        return ResponseEntity.ok(profileService.addEmergencyContact(email, dto));
    }

    @Operation(summary = "Acil durum kişisini sil")
    @DeleteMapping("/emergency-contacts/{id}")
    public ResponseEntity<Map<String, String>> deleteEmergencyContact(@PathVariable Long id,
                                                                       HttpServletRequest request) {
        String email = extractEmail(request);
        profileService.deleteEmergencyContact(email, id);
        auditLogService.log("EMERGENCY_CONTACT_DELETE", "EmergencyContact", id, email);
        return ResponseEntity.ok(Map.of("message", "Acil durum kişisi silindi"));
    }

    // ===== SİGORTA BİLGİSİ =====
    @Operation(summary = "Sigorta bilgilerini getir")
    @GetMapping("/insurance")
    public ResponseEntity<InsuranceInfoDTO> getInsurance(HttpServletRequest request) {
        String email = extractEmail(request);
        InsuranceInfoDTO dto = profileService.getInsurance(email);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Sigorta bilgilerini kaydet / güncelle")
    @PostMapping("/insurance")
    public ResponseEntity<InsuranceInfoDTO> saveInsurance(@RequestBody InsuranceInfoDTO dto,
                                                           HttpServletRequest request) {
        String email = extractEmail(request);
        auditLogService.log("INSURANCE_UPDATE", "InsuranceInfo", null, email);
        return ResponseEntity.ok(profileService.saveInsurance(email, dto));
    }

    // ===== HESAP YÖNETİMİ =====
    @Operation(summary = "Hesabı dondur")
    @PutMapping("/freeze")
    public ResponseEntity<Map<String, String>> freezeAccount(HttpServletRequest request) {
        String email = extractEmail(request);
        profileService.freezeAccount(email);
        auditLogService.log("ACCOUNT_FREEZE", "User", null, email);
        return ResponseEntity.ok(Map.of("message", "Hesabınız donduruldu"));
    }

    @Operation(summary = "Hesabı sil (soft delete)")
    @DeleteMapping("/delete-account")
    public ResponseEntity<Map<String, String>> deleteAccount(HttpServletRequest request) {
        String email = extractEmail(request);
        profileService.deleteAccount(email);
        auditLogService.log("ACCOUNT_DELETE", "User", null, email);
        return ResponseEntity.ok(Map.of("message", "Hesabınız silindi"));
    }

    // ===== YARDIMCI =====
    private String extractEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtService.extractEmail(authHeader.substring(7));
    }
}

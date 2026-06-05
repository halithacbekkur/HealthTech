package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.entity.AccountStatus;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.EmailAlreadyExistsException;
import com.healthtech.telehealth.exception.InvalidCredentialsException;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.service.AuditLogService;
import com.healthtech.telehealth.service.JwtService;
import com.healthtech.telehealth.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Kullanıcı kayıt, giriş, şifre sıfırlama ve hesap yönetimi")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;
    private final ProfileService profileService;

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtService jwtService, AuditLogService auditLogService,
                          ProfileService profileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
        this.profileService = profileService;
    }

    @Operation(summary = "Kullanıcı girişi", description = "E-posta ve şifre ile giriş yaparak JWT token + kullanıcı bilgisi alır")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Giriş başarılı"),
            @ApiResponse(responseCode = "401", description = "Geçersiz e-posta veya şifre"),
            @ApiResponse(responseCode = "423", description = "Hesap kilitli")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
                                                   HttpServletRequest httpRequest) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Geçersiz e-posta veya şifre"));

        // Hesap durumu kontrolü
        if (user.getAccountStatus() == AccountStatus.DELETED) {
            throw new InvalidCredentialsException("Bu hesap silinmiş");
        }
        if (user.getAccountStatus() == AccountStatus.FROZEN) {
            throw new InvalidCredentialsException("Bu hesap dondurulmuş. Destek ile iletişime geçin.");
        }

        // Brute-force koruması — hesap kilitli mi?
        if (user.isAccountLocked()) {
            throw new InvalidCredentialsException("Çok fazla başarısız deneme. Hesap " + LOCK_DURATION_MINUTES + " dakika kilitli.");
        }

        // Şifre kontrolü
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Başarısız giriş sayısını artır
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            }
            userRepository.save(user);

            auditLogService.log("LOGIN_FAILED", "User", user.getId(), request.getEmail(),
                    httpRequest.getRemoteAddr(), httpRequest.getHeader("User-Agent"), null);

            throw new InvalidCredentialsException("Geçersiz e-posta veya şifre");
        }

        // Başarılı giriş — sayaçları sıfırla
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        UserResponseDTO userDTO = profileService.mapToFullDTO(user);

        auditLogService.log("LOGIN_SUCCESS", "User", user.getId(), user.getEmail(),
                httpRequest.getRemoteAddr(), httpRequest.getHeader("User-Agent"), null);

        return ResponseEntity.ok(new LoginResponseDTO(token, userDTO));
    }

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Hasta veya doktor olarak yeni kullanıcı oluşturur")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kayıt başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz bilgi"),
            @ApiResponse(responseCode = "409", description = "Bu e-posta zaten kayıtlı")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request,
                                                     HttpServletRequest httpRequest) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Bu e-posta adresi zaten kayıtlı: " + request.getEmail());
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setTcKimlik(request.getTcKimlik());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());
        user.setAccountStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        auditLogService.log("REGISTER", "User", savedUser.getId(), savedUser.getEmail(),
                httpRequest.getRemoteAddr(), httpRequest.getHeader("User-Agent"), null);

        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.mapToFullDTO(savedUser));
    }

    @Operation(summary = "Şifre sıfırlama talebi", description = "Belirtilen e-postaya şifre sıfırlama linki gönderir")
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        // Not: E-posta gönderim servisi (SMTP) yapılandırıldığında aktif edilecek
        // Güvenlik: E-posta bulunamasa bile aynı mesajı döndür (email enumeration koruması)
        return ResponseEntity.ok(Map.of("message", "Şifre sıfırlama talimatları e-posta adresinize gönderildi"));
    }

    @Operation(summary = "Şifre değiştir", description = "Giriş yapmış kullanıcının şifresini değiştirir")
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> body,
                                                               HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Mevcut şifre yanlış");
        }

        if (newPassword == null || newPassword.length() < 8) {
            throw new InvalidCredentialsException("Yeni şifre en az 8 karakter olmalı");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        auditLogService.log("PASSWORD_CHANGE", "User", user.getId(), email);

        return ResponseEntity.ok(Map.of("message", "Şifre başarıyla değiştirildi"));
    }
}
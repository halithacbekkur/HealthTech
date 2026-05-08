package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.EmailAlreadyExistsException;
import com.healthtech.telehealth.exception.InvalidCredentialsException;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Kullanıcı kayıt ve giriş işlemleri")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Kullanıcı girişi", description = "E-posta ve şifre ile giriş yaparak JWT token alır")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Giriş başarılı, JWT token döner"),
            @ApiResponse(responseCode = "401", description = "Geçersiz e-posta veya şifre")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User request) {

        // BUG-005 FIX: Kullanici bulunamazsa veya sifre yanlissa 401 donuyor (500 degil)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Geçersiz e-posta veya şifre"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Geçersiz e-posta veya şifre");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Hasta, doktor veya admin rolünde yeni kullanıcı oluşturur")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kayıt başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz bilgi veya eksik alan"),
            @ApiResponse(responseCode = "409", description = "Bu e-posta zaten kayıtlı")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody User request) {

        // BUG-009 FIX: Ayni email ile cift kayit kontrolu (500 yerine 409 donuyor)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Bu e-posta adresi zaten kayıtlı: " + request.getEmail());
        }

        // Sifreyi hashle
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(request);

        // BUG-004 FIX: Sifre hash'i donmemeli — UserResponseDTO kullaniliyor
        UserResponseDTO responseDTO = new UserResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
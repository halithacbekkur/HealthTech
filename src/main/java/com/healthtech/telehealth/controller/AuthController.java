package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, String> login(@RequestBody User request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadi"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sifre yanlis");
        }

        // Artik token icine rol bilgisi de ekleniyor
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return Map.of("token", token);
    }

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Hasta, doktor veya admin rolünde yeni kullanıcı oluşturur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kayıt başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz bilgi veya eksik alan")
    })
    @PostMapping("/register")
    public User register(@Valid @RequestBody User request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(request);
    }
}
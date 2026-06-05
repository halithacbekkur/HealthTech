package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.healthtech.telehealth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Kullanıcı CRUD işlemleri ve profil yönetimi")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Tüm kullanıcıları listele", description = "Sistemdeki tüm kullanıcıları döner (Admin yetkisi önerilir)")
    @ApiResponse(responseCode = "200", description = "Kullanıcı listesi başarıyla döndü")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "ID ile kullanıcı getir", description = "Belirtilen ID'ye sahip kullanıcıyı döner")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kullanıcı bulundu"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Yeni kullanıcı oluştur", description = "Sisteme yeni bir kullanıcı ekler")
    @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla oluşturuldu")
    @PostMapping
    public UserResponseDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Kullanıcı sil", description = "Belirtilen ID'ye sahip kullanıcıyı siler")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla silindi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Kullanıcı başarıyla silindi";
    }

    @Operation(summary = "Kullanıcı güncelle", description = "Belirtilen ID'ye sahip kullanıcının bilgilerini günceller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla güncellendi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Giriş yapan kullanıcının bilgileri", description = "JWT token'dan kullanıcıyı tanır ve profil bilgilerini döner")
    @ApiResponse(responseCode = "200", description = "Kullanıcı bilgileri başarıyla döndü")
    @GetMapping("/me")
    public UserResponseDTO getMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        return userService.getCurrentUser(email);
    }

    @Operation(summary = "Doktor listesi", description = "Sistemdeki tüm doktorları listeler (randevu alma için)")
    @ApiResponse(responseCode = "200", description = "Doktor listesi başarıyla döndü")
    @GetMapping("/doctors")
    public List<UserResponseDTO> getDoctors() {
        return userService.getDoctors();
    }
}
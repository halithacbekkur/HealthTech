package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.healthtech.telehealth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Kullanıcı başarıyla silindi";
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @GetMapping("/me")
    public UserResponseDTO getMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        return userService.getCurrentUser(email);
    }
}
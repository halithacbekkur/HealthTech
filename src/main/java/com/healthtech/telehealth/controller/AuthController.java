package com.healthtech.telehealth.controller;

import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;import com.healthtech.telehealth.service.JwtService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Şifre yanlış");
        }

        String token = jwtService.generateToken(user.getEmail());

        return Map.of("token", token);
    }
    @PostMapping("/register")
    public User register(@Valid @RequestBody User request){

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(request);
    }
}
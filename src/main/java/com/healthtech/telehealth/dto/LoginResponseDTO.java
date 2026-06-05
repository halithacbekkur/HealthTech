package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Giriş yanıt DTO — token + kullanıcı bilgileri.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private UserResponseDTO user;
}

package com.healthtech.telehealth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtService için birim testleri.
 * Token üretme, doğrulama ve email çıkarma işlemlerini test eder.
 *
 * @author Ahmet Akif Yılmaz
 * @sprint Hafta 6 - Final Test
 */
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("Geçerli email ile token üretilmeli")
    void generateToken_shouldCreateValidToken() {
        // Given
        String email = "test@example.com";

        // When
        String token = jwtService.generateToken(email);

        // Then
        assertNotNull(token, "Token null olmamalı");
        assertFalse(token.isEmpty(), "Token boş olmamalı");
        assertTrue(token.contains("."), "Token JWT formatında olmalı (3 nokta ayrımlı parça)");
    }

    @Test
    @DisplayName("Token içerisinden email başarıyla çıkarılmalı")
    void extractEmail_shouldReturnCorrectEmail() {
        // Given
        String originalEmail = "akif@telehealth.com";
        String token = jwtService.generateToken(originalEmail);

        // When
        String extractedEmail = jwtService.extractEmail(token);

        // Then
        assertEquals(originalEmail, extractedEmail,
                "Çıkarılan email orijinal ile aynı olmalı");
    }

    @Test
    @DisplayName("Geçersiz token exception fırlatmalı")
    void extractEmail_shouldThrowExceptionForInvalidToken() {
        // Given
        String invalidToken = "bu.gecersiz.token";

        // When & Then
        assertThrows(Exception.class,
                () -> jwtService.extractEmail(invalidToken),
                "Geçersiz token için exception bekleniyordu");
    }

    @Test
    @DisplayName("Boş token exception fırlatmalı")
    void extractEmail_shouldThrowExceptionForEmptyToken() {
        assertThrows(Exception.class,
                () -> jwtService.extractEmail(""),
                "Boş token için exception bekleniyordu");
    }

    @Test
    @DisplayName("Aynı email için üretilen tokenlar farklı zaman damgaları içermeli")
    void generateToken_shouldProduceDifferentTokensOverTime() throws InterruptedException {
        // Given
        String email = "test@example.com";

        // When
        String token1 = jwtService.generateToken(email);
        Thread.sleep(1000); // 1 saniye bekle (issuedAt değişsin)
        String token2 = jwtService.generateToken(email);

        // Then
        assertNotEquals(token1, token2,
                "Farklı zamanlarda üretilen tokenlar aynı olmamalı");
    }
}

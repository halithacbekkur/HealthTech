package com.healthtech.telehealth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Guvvenlik icin en az 256-bit (32+ karakter) secret key kullanilmalidir
    private final String SECRET = "TeleSaglikPlatformuGizliAnahtar2026GucluSifre!";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Token uretirken rol bilgisini de ekliyoruz
    // Token suresi: 24 saat (SEC-05 gereksinimi)
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role) // Token icine rol bilgisi ekleniyor
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 saat
                .signWith(getSignKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Token icinden rol bilgisini cikar
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Token'in suresi dolmus mu kontrol et
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // Token gecerli mi kontrol et (email eslesmesi + sure kontrolu)
    public boolean isTokenValid(String token, String email) {
        String tokenEmail = extractEmail(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // Token icindeki tum bilgileri cikar (Claims = Token icindeki veriler)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
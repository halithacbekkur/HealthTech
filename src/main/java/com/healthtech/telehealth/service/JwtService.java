package com.healthtech.telehealth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Token uretirken artik rol bilgisini de ekliyoruz
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role) // Token icine rol bilgisi ekleniyor
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 saat
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

    // Token icindeki tum bilgileri cikar (Claims = Token icindeki veriler)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
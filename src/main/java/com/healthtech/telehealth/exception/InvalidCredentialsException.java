package com.healthtech.telehealth.exception;

/**
 * Geçersiz kimlik bilgileri (yanlış e-posta veya şifre) durumunda fırlatılır.
 * HTTP 401 Unauthorized döner.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

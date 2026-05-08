package com.healthtech.telehealth.exception;

/**
 * Geçersiz durum geçişlerinde fırlatılır.
 * Örn: CANCELLED durumundaki randevuyu onaylamaya çalışmak.
 */
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}

package com.healthtech.telehealth.exception;

/**
 * Yetkisiz erişim denemelerinde fırlatılır.
 * Örn: Hasta başka hastanın verisine erişmeye çalışırsa.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

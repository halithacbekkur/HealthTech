package com.healthtech.telehealth.exception;

/**
 * Aynı e-posta ile tekrar kayıt denemesinde fırlatılır.
 * HTTP 409 Conflict döner.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}

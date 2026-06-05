package com.healthtech.telehealth.exception;

/**
 * Randevu bulunamadığında fırlatılır.
 */
public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}

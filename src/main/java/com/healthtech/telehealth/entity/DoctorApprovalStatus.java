package com.healthtech.telehealth.entity;

/**
 * Doktor onay durumu — Admin tarafından yönetilir.
 */
public enum DoctorApprovalStatus {
    PENDING,    // Onay bekliyor
    APPROVED,   // Onaylandı — randevu alınabilir
    REJECTED    // Reddedildi
}

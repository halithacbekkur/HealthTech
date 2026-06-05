package com.healthtech.telehealth.entity;

public enum EmergencyStatus {
    PENDING,      // Beklemede
    RESPONDING,   // Doktor yanıt veriyor
    RESOLVED,     // Çözüldü
    CANCELLED     // İptal
}

package com.healthtech.telehealth.entity;

public enum VideoCallStatus {
    WAITING,     // Oda oluşturuldu, bekleniyor
    ACTIVE,      // Görüşme devam ediyor
    COMPLETED,   // Tamamlandı
    MISSED,      // Cevapsız
    CANCELLED    // İptal
}

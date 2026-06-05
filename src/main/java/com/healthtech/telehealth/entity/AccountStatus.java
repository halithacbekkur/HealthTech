package com.healthtech.telehealth.entity;

public enum AccountStatus {
    ACTIVE,         // Aktif hesap
    FROZEN,         // Dondurulmuş hesap
    DELETED,        // Silinmiş hesap
    PENDING_VERIFY  // E-posta doğrulama bekliyor
}

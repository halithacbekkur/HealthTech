# 🔧 Hafta 2 – Backend Geliştirme ve İyileştirme Raporu

**Proje:** Tele-Sağlık / HealthTech Platformu  
**Hafta:** 2 (Backend Enhancement)  
**Tarih:** 2026-05-08  

---

## Görev Durumu

| Görev | Sorumlu | Durum |
|-------|---------|-------|
| G6: RBAC güçlendirme ve güvenlik iyileştirmeleri | Ahmet Akif Yılmaz | ✅ Tamamlandı |
| G7: Randevu modülü iş kuralları ve durum geçişleri | Zelal Ergin | ✅ Tamamlandı |
| G8: Tıbbi kayıt ve reçete modülü iyileştirme | Halit Hacbekkur | ✅ Tamamlandı |
| G9: Global hata yönetimi ve validasyon güçlendirme | Nedim İsa | ✅ Tamamlandı |
| G10: README ve proje dokümantasyonu | Cena İsmail | ✅ Tamamlandı |

---

## G6: RBAC Güçlendirme ve Güvenlik İyileştirmeleri

### Yapılan İşlemler:
- JWT token süresi **1 saatten 24 saate** çıkarıldı (SEC-05 gereksinimi)
- Secret key güçlendirildi (256-bit, 46 karakter)
- `isTokenExpired()` metodu eklendi — token süre kontrolü
- `isTokenValid()` metodu eklendi — email eşleşmesi + süre kontrolü

### Güncellenen Dosya:
- `JwtService.java`

---

## G7: Randevu Modülü İş Kuralları

### Yeni İş Kuralları:
1. **Doktor rol kontrolü:** Sadece DOCTOR rolündeki kullanıcıya randevu alınabilir
2. **Geçmiş tarih kontrolü:** Geçmiş bir tarihe randevu oluşturulamaz
3. **Durum geçiş kuralları:**
   - PENDING → APPROVED (doktor onaylar)
   - APPROVED → COMPLETED (görüşme sonrası)
   - PENDING/APPROVED → CANCELLED (iptal)
   - Tamamlanmış randevu iptal edilemez
   - İptal edilmiş randevu tekrar onaylanamaz

### Yeni Endpoint:
- `PUT /api/appointments/{id}/complete` — Randevu tamamlama

### Güncellenen Dosyalar:
- `AppointmentService.java`
- `AppointmentController.java`

---

## G8: Tıbbi Kayıt ve Reçete İyileştirme

### Mevcut Durum:
- Tıbbi kayıt CRUD (hasta/doktor rol bazlı) ✅ Çalışıyor
- Reçete oluşturma (doktor) ve görüntüleme (hasta) ✅ Çalışıyor
- Rol bazlı @PreAuthorize kontrolleri ✅ Aktif

---

## G9: Global Hata Yönetimi Güçlendirme

### Yeni Exception Sınıfları:
| Sınıf | HTTP Kodu | Kullanım |
|-------|-----------|----------|
| `UserNotFoundException` | 404 | Kullanıcı bulunamadığında |
| `AppointmentNotFoundException` | 404 | Randevu bulunamadığında |
| `InvalidStatusTransitionException` | 400 | Geçersiz durum geçişlerinde |
| `UnauthorizedAccessException` | 403 | Yetkisiz erişim denemelerinde |

### Standart Hata Yanıt Formatı:
```json
{
    "timestamp": "2026-05-08T10:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Randevu bulunamadı: ID 99"
}
```

### Güncellenen Dosyalar:
- `GlobalExceptionHandler.java` (güçlendirildi)
- `AppointmentNotFoundException.java` (yeni)
- `InvalidStatusTransitionException.java` (yeni)
- `UnauthorizedAccessException.java` (yeni)

---

## G10: README ve Proje Dokümantasyonu

### Oluşturulan Dosya:
- `README.md` — Profesyonel proje tanıtım dokümanı

### İçerik:
- Teknoloji yığını
- Proje yapısı (klasör ağacı)
- Kurulum ve çalıştırma adımları
- API endpoint tablosu
- Randevu durum akış diyagramı
- Ekip bilgileri

---

*Hazırlayan: Proje Ekibi | Onaylayan: Halit Hacbekkur (Scrum Master)*

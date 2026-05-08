# 🏥 HealthTech – Proje Akışı ve Haftalık İlerleme

Bu doküman, **HealthTech (Tele-Sağlık Platformu)** projesinin 6 haftalık ilerleme durumunu, 30 görevi ve ekip katkılarını içerir.

---

## 👥 Proje Ekibi

| İsim | Rol |
|------|-----|
| Halid Hacbekkur | Scrum Master & Proje Yönetimi |
| Cena İsmail | Frontend Geliştirme / Mobil Analiz |
| Zelal Ergin | Backend Geliştirme / Altyapı |
| Nedim İsa | Gereksinim Toplama ve Belgeleme |
| Ahmet Akif Yılmaz | Veritabanı Tasarımı & Güvenlik |

---

## 📊 Genel İlerleme Tablosu

| Hafta | Görevler | Durum |
|-------|----------|-------|
| Hafta 1 | G1-G5: Analiz ve Planlama | ✅ Tamamlandı |
| Hafta 2 | G6-G10: Belgeleme ve Strateji | 🔄 Devam Ediyor |
| Hafta 3 | G11-G15: Tasarım ve Risk Yönetimi | ⏳ Bekliyor |
| Hafta 4 | G16-G20: Geliştirme ve Test | ⏳ Bekliyor |
| Hafta 5 | G21-G25: Entegrasyon ve Güvenlik | ⏳ Bekliyor |
| Hafta 6 | G26-G30: Sunum ve Final | ⏳ Bekliyor |

---

# 📅 HAFTA 1 – Analiz ve Planlama
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G1 | Mobil Uygulama Gereksinim Analizi | Cena İsmail | ✅ |
| G2 | Spring Boot Altyapısı ve API Tasarımı | Zelal Ergin | ✅ |
| G3 | Veritabanı Şema Tasarımı ve Modelleme | Halit Hacbekkur | ✅ |
| G4 | Paydaş Analizi ve İletişim Planı | Nedim İsa | ✅ |
| G5 | Video Konferans Modülü Araştırması | Ahmet Akif Yılmaz | ✅ |

### G1: Mobil Uygulama Gereksinim Analizi
**Sorumlu:** CENA İSMAİL

Hasta ve doktor gereksinimleri ayrı ayrı analiz edildi. Randevu alma, video görüşme, tıbbi kayıt görüntüleme ve kullanıcı arayüzü ihtiyaçları belirlendi.

**Hasta Gereksinimleri:**

| ID | Gereksinim | Öncelik |
|----|-----------|---------|
| H-01 | E-posta ve şifre ile kayıt | Yüksek |
| H-02 | JWT ile güvenli giriş | Yüksek |
| H-03 | Profil görüntüleme | Yüksek |
| H-04 | Randevu alma ve takip | Yüksek |
| H-05 | Tıbbi kayıt görüntüleme | Orta |
| H-06 | Video görüşme | Orta |

**Doktor Gereksinimleri:**

| ID | Gereksinim | Öncelik |
|----|-----------|---------|
| D-01 | Randevu onaylama/reddetme | Yüksek |
| D-02 | Hasta tıbbi kayıtlarına erişim | Yüksek |
| D-03 | Reçete yazma | Yüksek |
| D-04 | Video görüşme başlatma | Orta |

---

### G2: Spring Boot Altyapısı ve API Tasarımı
**Sorumlu:** ZELAL ERGİN

Spring Boot projesi oluşturuldu. MySQL bağlantısı, JWT güvenlik ve REST API bağımlılıkları eklendi. Swagger/OpenAPI dokümantasyonu entegre edildi.

**Teknoloji Yığını:** Java 17, Spring Boot 4.0.6, MySQL 8.x, JWT (jjwt 0.12.6), springdoc-openapi 2.8.8

**API Endpoint Yapısı:**
* `POST /api/auth/register` — Kayıt
* `POST /api/auth/login` — Giriş (JWT)
* `GET/POST/PUT/DELETE /api/users` — Kullanıcı CRUD
* `POST/GET /api/appointments` — Randevu yönetimi
* `GET/POST /api/medical-records` — Tıbbi kayıtlar
* `POST/GET /api/prescriptions` — Reçete yönetimi

---

### G3: Veritabanı Şema Tasarımı
**Sorumlu:** HALİT HACBEKKUR

MySQL veritabanı şeması tasarlandı. Entity ilişkileri belirlendi.

**Tablolar:** `users`, `appointments`, `medical_records`, `prescriptions`

**Enum Değerleri:**
* **Role:** `PATIENT`, `DOCTOR`, `ADMIN`
* **AppointmentStatus:** `PENDING`, `APPROVED`, `CANCELLED`, `COMPLETED`

---

### G4: Paydaş Analizi ve İletişim Planı
**Sorumlu:** NEDİM İSA

**Paydaşlar:** Hastalar, Doktorlar, Sağlık Personeli, Yöneticiler, Teknik Ekip

**İletişim Kanalları:** Haftalık sprint toplantıları, GitHub Issues, WhatsApp grubu

---

### G5: Video Konferans Araştırması
**Sorumlu:** AHMET AKİF YILMAZ

| Çözüm | Maliyet | Entegrasyon | Seçim |
|-------|---------|-------------|-------|
| Zoom API | Ücretli | Orta | ❌ |
| Twilio Video | Ücretli | Kolay | ❌ |
| **Jitsi Meet** | **Ücretsiz** | **Kolay (iframe)** | **✅ Seçildi** |

**Karar:** Jitsi Meet — açık kaynak, ücretsiz, iframe entegrasyonu

---

# 📅 HAFTA 2 – Belgeleme ve Strateji
**Durum:** 🔄 Devam Ediyor

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G6 | Gereksinim Analizi ve Belgeleme | Cena İsmail | ⏳ |
| G7 | Proje Takvimi ve Zaman Çizelgesi | Ahmet Akif Yılmaz | ⏳ |
| G8 | Veri Toplama ve Ön İşleme Stratejileri | Nedim İsa | ⏳ |
| G9 | Proje Yönetimi Araçları İnceleme | Zelal Ergin | ⏳ |
| G10 | Paydaşlarla İletişim ve Geri Bildirim | Halit Hacbekkur | ⏳ |

---

# 📅 HAFTA 3 – Tasarım ve Risk Yönetimi

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G11 | Proje İletişim Planı Oluşturma | Halit Hacbekkur | ⏳ |
| G12 | Veritabanı Tasarımı ve Modelleme | Cena İsmail | ⏳ |
| G13 | Risk Analizi ve Yönetim Planı | Zelal Ergin | ⏳ |
| G14 | Temel Kullanıcı Arayüzü Tasarımı | Ahmet Akif Yılmaz | ⏳ |
| G15 | Veri Ön İşleme ve Temizleme Algoritmaları | Nedim İsa | ⏳ |

---

# 📅 HAFTA 4 – Geliştirme ve Test

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G16 | Hasta Takip Uygulaması Arayüz Geliştirme | Nedim İsa | ⏳ |
| G17 | API Entegrasyonu ve Belgelendirme | Cena İsmail | ⏳ |
| G18 | Raporlama ve Analiz Modülü Tasarımı | Zelal Ergin | ⏳ |
| G19 | Veritabanı Entegrasyonu ve Test | Halit Hacbekkur | ⏳ |
| G20 | Güvenlik Testleri ve Optimizasyon | Ahmet Akif Yılmaz | ⏳ |

---

# 📅 HAFTA 5 – Entegrasyon ve Güvenlik

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G21 | Kullanıcı Arayüzü Test Senaryoları | Halit Hacbekkur | ⏳ |
| G22 | Veri Entegrasyonu Geliştirme | Nedim İsa | ⏳ |
| G23 | Mobil Uygulama Entegrasyon Testleri | Zelal Ergin | ⏳ |
| G24 | Veritabanı Performans İyileştirmesi | Cena İsmail | ⏳ |
| G25 | Güvenlik Açığı Analizi | Ahmet Akif Yılmaz | ⏳ |

---

# 📅 HAFTA 6 – Sunum ve Final

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G26 | Proje Sunum Materyalleri Hazırlama | Halit Hacbekkur | ⏳ |
| G27 | Proje Dokümantasyonu Tamamlama | Zelal Ergin | ⏳ |
| G28 | Final Testleri ve Hata Giderme | Ahmet Akif Yılmaz | ⏳ |
| G29 | Final Sunumuna Hazırlık ve Prova | Cena İsmail | ⏳ |
| G30 | Proje Sonuçları Değerlendirme ve Analiz | Nedim İsa | ⏳ |

---

# 🔧 Ek Geliştirmeler (Kod İyileştirmeleri)

Hafta 1 sonrasında yapılan kod iyileştirmeleri:

### Güvenlik İyileştirmeleri
* JWT token süresi 24 saate çıkarıldı
* Secret key ve DB şifresi environment variable'a taşındı
* `isTokenExpired()`, `isTokenValid()` metotları eklendi
* JWT'ye userId claim eklendi

### Randevu İş Kuralları
* Doktor rol kontrolü — sadece DOCTOR'a randevu alınabilir
* Geçmiş tarih kontrolü
* Durum geçiş kuralları: PENDING → APPROVED → COMPLETED
* `PUT /api/appointments/{id}/complete` endpoint'i eklendi

### Hata Yönetimi
* 5 özel exception sınıfı oluşturuldu
* Standart hata yanıt formatı (timestamp, status, error, message)
* `GlobalExceptionHandler` güçlendirildi

### Bug Fix'ler
* BUG-004: Register şifre hash dönmüyor (UserResponseDTO)
* BUG-005: Login 500 → 401 (InvalidCredentialsException)
* BUG-007: createUser şifre hash'leme eklendi
* BUG-008: Update'te rol korunuyor
* BUG-009: Duplicate email 409 (EmailAlreadyExistsException)
* BUG-010: @PrePersist ile createdAt otomatik

---

## 📌 Genel Sonuç

Hafta 1'de 5 analiz/planlama görevi tamamlanmıştır. Ek olarak backend kod iyileştirmeleri (güvenlik, hata yönetimi, bug fix) gerçekleştirilmiştir. Hafta 2'den itibaren resmi görev listesine göre ilerleme devam etmektedir.

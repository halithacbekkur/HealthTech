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
| Hafta 2 | G6-G10: Belgeleme ve Strateji | ✅ Tamamlandı |
| Hafta 3 | G11-G15: Tasarım ve Risk Yönetimi | ✅ Tamamlandı |
| Hafta 4 | G16-G20: Geliştirme ve Test | 🔄 Devam Ediyor |
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

---

### G1: Mobil Uygulama Gereksinim Analizi
**Sorumlu:** CENA İSMAİL

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

---

# 📅 HAFTA 2 – Belgeleme ve Strateji
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G6 | Gereksinim Analizi ve Belgeleme | Cena İsmail | ✅ |
| G7 | Proje Takvimi ve Zaman Çizelgesi | Ahmet Akif Yılmaz | ✅ |
| G8 | Veri Toplama ve Ön İşleme Stratejileri | Nedim İsa | ✅ |
| G9 | Proje Yönetimi Araçları İnceleme | Zelal Ergin | ✅ |
| G10 | Paydaşlarla İletişim ve Geri Bildirim | Halit Hacbekkur | ✅ |

---

### G6: Gereksinim Analizi ve Belgeleme
**Sorumlu:** CENA İSMAİL

**Fonksiyonel Gereksinimler:**

| ID | Gereksinim | Modül | Öncelik |
|----|-----------|-------|---------|
| FR-01 | Kullanıcı e-posta/şifre ile kayıt olabilmeli | Auth | Yüksek |
| FR-02 | Kullanıcı JWT ile güvenli giriş yapabilmeli | Auth | Yüksek |
| FR-03 | Hasta randevu oluşturabilmeli | Appointment | Yüksek |
| FR-04 | Doktor randevu onaylayabilmeli/reddedebilmeli | Appointment | Yüksek |
| FR-05 | Doktor randevuyu tamamlandı olarak işaretleyebilmeli | Appointment | Yüksek |
| FR-06 | Hasta kendi tıbbi kaydını görüntüleyebilmeli | MedicalRecord | Yüksek |
| FR-07 | Doktor hastanın tıbbi kaydına erişebilmeli | MedicalRecord | Yüksek |
| FR-08 | Doktor reçete yazabilmeli | Prescription | Yüksek |
| FR-09 | Hasta reçetelerini görüntüleyebilmeli | Prescription | Orta |
| FR-10 | Admin tüm kullanıcıları yönetebilmeli | User | Orta |
| FR-11 | Video görüşme başlatılabilmeli (Jitsi Meet) | Video | Orta |
| FR-12 | Kullanıcı profil bilgilerini güncelleyebilmeli | User | Orta |

**Kullanıcı Hikayeleri:**
* **US-01:** Hasta olarak, sisteme kayıt olmak istiyorum ki randevu alabileyim.
* **US-02:** Hasta olarak, doktorumla randevu almak istiyorum ki muayene olabileyim.
* **US-03:** Doktor olarak, bekleyen randevuları onaylamak istiyorum ki takvimimi yöneteyim.
* **US-04:** Doktor olarak, hastama reçete yazmak istiyorum ki tedavisini başlatayım.
* **US-05:** Hasta olarak, tıbbi kayıtlarımı görmek istiyorum ki sağlık geçmişimi takip edeyim.
* **US-06:** Admin olarak, tüm kullanıcıları listelemek istiyorum ki sistemi yöneteyim.

**Kullanım Senaryoları:**

**UC-01: Randevu Alma** → Hasta giriş → Doktor seç → Tarih belirle → PENDING → Doktor onaylar → COMPLETED
**UC-02: Reçete Yazma** → Doktor giriş → Hasta seç → İlaç gir → Reçete oluştur
**UC-03: Video Görüşme** → Doktor Jitsi başlat → Hasta katıl → Görüşme → COMPLETED

---

### G7: Proje Takvimi ve Zaman Çizelgesi
**Sorumlu:** AHMET AKİF YILMAZ

| Hafta | Tarih Aralığı | Odak | Milestone |
|-------|--------------|------|-----------|
| Hafta 1 | 9–15 Mart | Analiz ve Planlama | 📌 Proje temeli atıldı |
| Hafta 2 | 16–22 Mart | Belgeleme ve Strateji | 📌 Gereksinimler belgelendi |
| Hafta 3 | 23–29 Mart | Tasarım ve Risk Yönetimi | 📌 UI prototipi ve risk planı |
| Hafta 4 | 30 Mart–5 Nisan | Geliştirme ve Test | 📌 Çalışan MVP teslim |
| Hafta 5 | 6–12 Nisan | Entegrasyon ve Güvenlik | 📌 Tüm testler geçti |
| Hafta 6 | 13–19 Nisan | Sunum ve Final | 📌 Final sunumu yapıldı |

**Ekip İş Yükü Dağılımı:**

| Üye | H1 | H2 | H3 | H4 | H5 | H6 |
|-----|----|----|----|----|----|----|
| Cena İsmail | G1 | G6 | G12 | G17 | G24 | G29 |
| Zelal Ergin | G2 | G9 | G13 | G18 | G23 | G27 |
| Halit Hacbekkur | G3 | G10 | G11 | G19 | G21 | G26 |
| Nedim İsa | G4 | G8 | G15 | G16 | G22 | G30 |
| Ahmet Akif Yılmaz | G5 | G7 | G14 | G20 | G25 | G28 |

---

### G8: Veri Toplama ve Ön İşleme Stratejileri
**Sorumlu:** NEDİM İSA

**Veri Kaynakları:**

| Kaynak | Veri Türü | Toplama Yöntemi |
|--------|----------|-----------------|
| Kullanıcı Kayıt Formu | Ad, e-posta, şifre, telefon, rol | POST /api/auth/register |
| Randevu Formu | Doktor ID, tarih/saat | POST /api/appointments |
| Tıbbi Kayıt | Teşhis, tedavi, notlar | POST /api/medical-records |
| Reçete | İlaç adı, doz, süre | POST /api/prescriptions |
| Sistem Logları | Giriş/çıkış, hata kayıtları | Spring Boot Logger |

**Veri Temizleme:** `@Email`, `@NotBlank`, `@NotNull`, BCrypt hash, Enum doğrulama, unique constraint

---

### G9: Proje Yönetimi Araçları İnceleme ve Seçme
**Sorumlu:** ZELAL ERGİN

| Özellik | Trello | Jira | GitHub Projects |
|---------|--------|------|-----------------|
| Maliyet | Ücretsiz | Ücretsiz (10 kişi) | **Ücretsiz** |
| Git Entegrasyonu | ❌ | Kısmen | **✅ Doğrudan** |
| Issue Tracking | Kısmen | ✅ | **✅** |

**Seçim: ✅ GitHub Projects** — Kod zaten GitHub'da, aynı platformda yönetim

**Commit Mesaj Kuralları:** `feat:`, `fix:`, `docs:` ön ekleri

---

### G10: Paydaşlarla İletişim ve Geri Bildirim
**Sorumlu:** HALİT HACBEKKUR

**Toplanan Geri Bildirimler:**

* Hastalar: "Randevu alma basit olmalı" → ✅ Tek endpoint
* Doktorlar: "Bekleyen randevuları hızlıca görmeliyim" → ✅ GET /appointments/doctor
* Teknik Ekip: "API hata mesajları tutarlı olmalı" → ✅ GlobalExceptionHandler
* Danışman: "Güvenlik testleri yapılmalı" → 📅 Hafta 4 (G20)

---

# 📅 HAFTA 3 – Tasarım ve Risk Yönetimi
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G11 | Proje İletişim Planı Oluşturma | Halit Hacbekkur | ✅ |
| G12 | Veritabanı Tasarımı ve Modelleme | Cena İsmail | ✅ |
| G13 | Risk Analizi ve Yönetim Planı | Zelal Ergin | ✅ |
| G14 | Temel Kullanıcı Arayüzü Tasarımı | Ahmet Akif Yılmaz | ✅ |
| G15 | Veri Ön İşleme ve Temizleme Algoritmaları | Nedim İsa | ✅ |

---

### G11: Proje İletişim Planı Oluşturma
**Sorumlu:** HALİT HACBEKKUR

**İletişim Kanalları:**

| Kanal | Amaç | Sıklık |
|-------|------|--------|
| WhatsApp Grubu | Anlık iletişim | Sürekli |
| GitHub Issues | Bug raporlama, görev takibi | Her görevde |
| Sprint Toplantısı | Haftalık ilerleme | Haftada 1 |
| Code Review | Kod kalitesi | Her PR'da |
| E-posta | Resmi bilgilendirme | Gerektiğinde |

**Toplantı Takvimi:** Pazartesi (Sprint Planlama), Çarşamba (Teknik), Cuma (Review)

**RACI Matrisi:**

| Faaliyet | Halit | Cena | Zelal | Nedim | Ahmet Akif |
|----------|-------|------|-------|-------|------------|
| Sprint Planlama | **R** | I | I | I | I |
| Backend Geliştirme | I | C | **R** | C | C |
| DB Tasarımı | **R** | C | C | I | C |
| Güvenlik | I | I | C | I | **R** |
| Test | A | C | C | C | **R** |

---

### G12: Veritabanı Tasarımı ve Modelleme
**Sorumlu:** CENA İSMAİL

**ER Diyagramı:**

```
┌──────────┐       ┌──────────────┐       ┌──────────────┐
│  users   │──1:N──│ appointments │──1:1──│ prescriptions│
│          │       └──────────────┘       └──────────────┘
│          │──1:1──┌──────────────────┐
└──────────┘       │ medical_records  │
                   └──────────────────┘
```

**Tablolar:** users (7 kolon), appointments (6 kolon), medical_records (8 kolon), prescriptions (5 kolon)

**İlişkiler:** users→appointments (1:N), users→medical_records (1:1), appointments→prescriptions (1:1)

---

### G13: Risk Analizi ve Yönetim Planı
**Sorumlu:** ZELAL ERGİN

| ID | Risk | Seviye | Önleyici Tedbir |
|----|------|--------|-----------------|
| R-01 | DB şifresi GitHub'da açık | 🔴 Kritik | Environment variable ✅ |
| R-02 | JWT secret hardcoded | 🔴 Kritik | @Value externalize ✅ |
| R-03 | SQL Injection | 🟡 Orta | JPA Parameterized Queries |
| R-04 | Ekip üyesi ayrılması | 🟡 Orta | Kod dokümantasyonu |
| R-05 | Takvim kayması | 🟠 Yüksek | Haftalık sprint review |
| R-06 | Video entegrasyon zorluğu | 🟡 Orta | Jitsi Meet (iframe) |
| R-07 | DB performans sorunu | 🟢 Düşük | Index, sorgu optimizasyonu |
| R-08 | KVKK uyumsuzluğu | 🟠 Yüksek | Şifreleme, erişim logları |

**Acil Durum Planları:** Veri sızıntısı → şifre değiştir, Sunucu çökmesi → yedekten dön, Takvim kayması → kapsam daralt

---

### G14: Temel Kullanıcı Arayüzü Tasarımı
**Sorumlu:** AHMET AKİF YILMAZ

**Sayfa Haritası:**

```
🏠 Ana Sayfa
├── 🔐 Login / Register
├── 👤 HASTA: Dashboard, Randevularım, Tıbbi Kayıtlarım, Reçetelerim
├── 🩺 DOKTOR: Dashboard, Bekleyen Randevular, Reçete Yaz, Video Görüşme
└── ⚙️ ADMİN: Kullanıcı Yönetimi, Raporlar
```

**Tasarım Sistemi:** Primary `#2563EB`, Success `#16A34A`, Danger `#DC2626`, Font: Inter/Roboto, Teknoloji: Angular 17+

---

### G15: Veri Ön İşleme ve Temizleme Algoritmaları
**Sorumlu:** NEDİM İSA

**Veri Dönüşüm Pipeline:**

```
Kullanıcı Girdisi → [1] Jakarta Validation → [2] İş kuralı kontrolü → [3] Şifre hash/tarih kontrolü → [4] JPA kayıt → [5] Entity→DTO dönüşümü
```

**Aykırı Değer Tespiti:** Geçmiş tarihte randevu → hata, Doktor olmayan kullanıcıya randevu → hata, Geçersiz durum geçişi → exception, Duplicate email → 409

---

# 📅 HAFTA 4 – Geliştirme ve Test
**Durum:** 🔄 Devam Ediyor

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G16 | Hasta Takip Uygulaması Arayüz Geliştirme | Nedim İsa | ✅ |
| G17 | API Entegrasyonu ve Belgelendirme | Cena İsmail | 🔄 |
| G18 | Raporlama ve Analiz Modülü Tasarımı | Zelal Ergin | ⏳ |
| G19 | Veritabanı Entegrasyonu ve Test | Halit Hacbekkur | ⏳ |
| G20 | Güvenlik Testleri ve Optimizasyon | Ahmet Akif Yılmaz | ⏳ |

---

### G16: Hasta Takip Uygulaması Arayüz Geliştirme
**Sorumlu:** NEDİM İSA

**Angular Component Yapısı:** auth/ (login, register), patient/ (dashboard, appointments, medical-record, prescriptions), doctor/ (dashboard, appointments, prescriptions), admin/ (users), shared/ (navbar, sidebar, guards), services/ (auth, user, appointment, prescription)

**Ekranlar:** Hasta Listeleme (tablo + arama), Hasta Detay (profil + tıbbi kayıt + randevu geçmişi), Yeni Hasta Ekleme (form)

---

### G17: API Entegrasyonu ve Belgelendirme
**Sorumlu:** CENA İSMAİL
**Durum:** 🔄 Devam Ediyor

**Yapılan Kod Değişiklikleri:**
* `GET /api/users/doctors` endpoint eklendi (doktor listeleme)
* `UserRepository.findByRole()` metodu eklendi
* `UserService.getDoctors()` metodu eklendi
* `CorsConfig.java` oluşturuldu (Angular bağlantısı için)

---

### G18: Raporlama ve Analiz Modülü Tasarımı
**Sorumlu:** ZELAL ERGİN
**Durum:** ⏳ Bekliyor

---

### G19: Veritabanı Entegrasyonu ve Test
**Sorumlu:** HALİT HACBEKKUR
**Durum:** ⏳ Bekliyor

---

### G20: Güvenlik Testleri ve Optimizasyon
**Sorumlu:** AHMET AKİF YILMAZ
**Durum:** ⏳ Bekliyor

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

### Güvenlik İyileştirmeleri
* JWT token süresi 24 saate çıkarıldı, secret key externalize edildi
* `isTokenExpired()`, `isTokenValid()`, `extractUserId()` metotları
* JWT'ye userId claim eklendi

### Randevu İş Kuralları
* Doktor rol kontrolü, geçmiş tarih kontrolü
* Durum geçişleri: PENDING → APPROVED → COMPLETED
* `PUT /api/appointments/{id}/complete` endpoint'i

### Hata Yönetimi
* 6 özel exception sınıfı + GlobalExceptionHandler
* Standart hata formatı (timestamp, status, error, message)

### Bug Fix'ler
* BUG-004: Register → UserResponseDTO (şifre gizleme)
* BUG-005: Login 500 → 401 (InvalidCredentialsException)
* BUG-007: createUser şifre hash'leme
* BUG-008: Update'te rol korunuyor
* BUG-009: Duplicate email → 409 (EmailAlreadyExistsException)
* BUG-010: @PrePersist ile createdAt otomatik

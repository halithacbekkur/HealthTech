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
| Hafta 4 | G16-G20: Geliştirme ve Test | ✅ Tamamlandı |
| Hafta 5 | G21-G25: Entegrasyon ve Güvenlik | ✅ Tamamlandı |
| Hafta 6 | G26-G30: Sunum ve Final | ✅ Tamamlandı |

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
| Tıbbi Kayıt | Teşhis, tedavi, notlar | POST /api/medical-records/my |
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
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G16 | Hasta Takip Uygulaması Arayüz Geliştirme | Nedim İsa | ✅ |
| G17 | API Entegrasyonu ve Belgelendirme | Cena İsmail | ✅ |
| G18 | Raporlama ve Analiz Modülü Tasarımı | Zelal Ergin | ✅ |
| G19 | Veritabanı Entegrasyonu ve Test | Halit Hacbekkur | ✅ |
| G20 | Güvenlik Testleri ve Optimizasyon | Ahmet Akif Yılmaz | ✅ |

---

### G16: Hasta Takip Uygulaması Arayüz Geliştirme
**Sorumlu:** NEDİM İSA

Hasta takip uygulaması için Angular 17+ tabanlı kullanıcı arayüzü tasarlandı ve component yapısı oluşturuldu.

**Angular Component Yapısı:**

```
src/app/
├── auth/
│   ├── login/           → Giriş ekranı (email + şifre)
│   └── register/        → Kayıt ekranı (ad, email, şifre, telefon, rol)
├── patient/
│   ├── dashboard/       → Hasta özet sayfası (yaklaşan randevular)
│   ├── appointments/    → Randevu listesi ve yeni randevu alma
│   ├── medical-record/  → Tıbbi kayıt görüntüleme (kan grubu, alerji vb.)
│   └── prescriptions/   → Reçete listesi
├── doctor/
│   ├── dashboard/       → Doktor özet sayfası (bekleyen randevular)
│   ├── appointments/    → Randevu onaylama/reddetme/tamamlama
│   └── prescriptions/   → Reçete yazma formu
├── admin/
│   └── users/           → Kullanıcı listeleme, silme, düzenleme
├── shared/
│   ├── navbar/          → Üst menü (rol bazlı menü öğeleri)
│   ├── sidebar/         → Yan menü (navigasyon)
│   └── guards/          → AuthGuard (JWT kontrolü), RoleGuard
└── services/
    ├── auth.service.ts       → Login/Register API çağrıları
    ├── user.service.ts       → Kullanıcı CRUD
    ├── appointment.service.ts → Randevu CRUD
    └── prescription.service.ts → Reçete CRUD
```

**Ekran Detayları:**

| Ekran | Bileşenler | API Bağlantısı |
|-------|-----------|----------------|
| Hasta Listeleme | Tablo, arama kutusu, detay butonu | GET /api/users |
| Hasta Detay | Profil kartı, tıbbi kayıt, randevu geçmişi | GET /api/users/{id}, GET /api/medical-records/patient/{id} |
| Yeni Hasta Ekleme | Form (ad, email, şifre, telefon) | POST /api/auth/register |
| Randevu Al | Doktor seçimi, tarih picker, onay | POST /api/appointments |
| Reçete Yaz | Hasta seçimi, ilaç girişi, talimat | POST /api/prescriptions |

---

### G17: API Entegrasyonu ve Belgelendirme
**Sorumlu:** CENA İSMAİL

Tüm backend API endpoint'leri Swagger/OpenAPI ile dokümante edildi. Frontend-backend entegrasyonu için CORS ve yeni endpoint'ler eklendi.

**Yapılan Kod Değişiklikleri:**
* `GET /api/users/doctors` — doktor listeleme endpoint'i eklendi
* `UserRepository.findByRole()` — rol bazlı kullanıcı sorgulama
* `UserRepository.existsByEmail()` — email varlık kontrolü
* `UserService.getDoctors()` — doktor listesi servis metodu
* `CorsConfig.java` — Angular localhost:4200 için CORS izni

**Tüm API Endpoint Listesi (19 adet):**

| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/auth/register | Yeni kullanıcı kaydı | Herkese açık |
| POST | /api/auth/login | JWT ile giriş | Herkese açık |
| GET | /api/users | Tüm kullanıcıları listele | JWT gerekli |
| GET | /api/users/{id} | Kullanıcı detay | JWT gerekli |
| GET | /api/users/me | Giriş yapan kullanıcı bilgisi | JWT gerekli |
| GET | /api/users/doctors | Doktor listesi | JWT gerekli |
| PUT | /api/users/{id} | Kullanıcı güncelle | JWT gerekli |
| DELETE | /api/users/{id} | Kullanıcı sil | JWT gerekli |
| POST | /api/appointments | Randevu oluştur | JWT (PATIENT) |
| GET | /api/appointments/my | Hasta randevuları | JWT gerekli |
| GET | /api/appointments/doctor | Doktor randevuları | JWT gerekli |
| PUT | /api/appointments/{id}/approve | Randevu onayla | JWT (DOCTOR) |
| PUT | /api/appointments/{id}/cancel | Randevu iptal | JWT gerekli |
| PUT | /api/appointments/{id}/complete | Randevu tamamla | JWT (DOCTOR) |
| POST | /api/prescriptions | Reçete yaz | JWT (DOCTOR) |
| GET | /api/prescriptions/my | Reçetelerim | JWT (PATIENT) |
| POST | /api/medical-records/my | Tıbbi kayıt oluştur | JWT gerekli |
| GET | /api/medical-records/my | Tıbbi kaydım | JWT gerekli |
| GET | /api/medical-records/patient/{id} | Hasta tıbbi kaydı | JWT (DOCTOR) |

**Swagger UI Erişimi:** `http://localhost:8080/swagger-ui.html`

---

### G18: Raporlama ve Analiz Modülü Tasarımı
**Sorumlu:** ZELAL ERGİN

Sistem genelinde istatistiksel verileri toplayan raporlama modülü tasarlandı.

**Raporlama Metrikleri:**

| Metrik | Veri Kaynağı | Açıklama |
|--------|-------------|----------|
| Toplam kullanıcı sayısı | UserRepository.count() | Hasta/Doktor/Admin ayrımı |
| Randevu istatistikleri | AppointmentRepository | PENDING/APPROVED/COMPLETED/CANCELLED sayıları |
| Reçete sayısı | PrescriptionRepository.count() | Toplam yazılmış reçete |
| Doktor yoğunluk analizi | AppointmentRepository | Doktor başına düşen randevu sayısı |
| Aylık randevu trendi | AppointmentRepository | Son 6 ayın randevu grafiği verileri |
| Ortalama randevu süresi | AppointmentRepository | Oluşturma-tamamlama arası süre |

---

### G19: Veritabanı Entegrasyonu ve Test
**Sorumlu:** HALİT HACBEKKUR

Tüm entity-repository-service katmanları entegre edildi ve API endpoint'leri uçtan uca test edildi.

**Test Senaryoları ve Sonuçları:**

| ID | Test Senaryosu | Beklenen | Gerçekleşen | Durum |
|----|---------------|----------|-------------|-------|
| T-01 | Yeni kullanıcı kayıt | 201 Created + UserResponseDTO | 201 + DTO (şifresiz) | ✅ |
| T-02 | Mevcut email ile kayıt | 409 Conflict | 409 + hata mesajı | ✅ |
| T-03 | Doğru bilgiyle giriş | 200 + JWT token | Token döndü | ✅ |
| T-04 | Yanlış şifre ile giriş | 401 Unauthorized | 401 + hata | ✅ |
| T-05 | Geçerli randevu oluşturma | 200 + PENDING | Randevu oluştu | ✅ |
| T-06 | Geçmiş tarihte randevu | 400 Bad Request | Hata fırlatıldı | ✅ |
| T-07 | Randevu onaylama (DOCTOR) | PENDING → APPROVED | Durum güncellendi | ✅ |
| T-08 | Randevu tamamlama | APPROVED → COMPLETED | Durum güncellendi | ✅ |
| T-09 | Reçete yazma (DOCTOR) | 200 + PrescriptionDTO | Reçete oluştu | ✅ |
| T-10 | Reçete yazma (PATIENT) | 403 Forbidden | Yetkisiz erişim | ✅ |

**Veritabanı Konfigürasyonu:** MySQL 8.x, `ddl-auto=update`, UTF-8, connection pool: HikariCP

---

### G20: Güvenlik Testleri ve Optimizasyon
**Sorumlu:** AHMET AKİF YILMAZ

Proje genelinde güvenlik açıkları tarandı ve optimizasyon çalışmaları yapıldı.

**Güvenlik Kontrol Listesi:**

| # | Kontrol | Açıklama | Durum |
|---|---------|----------|-------|
| 1 | Şifre hash'leme | BCrypt ile hash, plaintext saklanmıyor | ✅ |
| 2 | JWT token doğrulama | Her istekte JwtAuthFilter kontrol eder | ✅ |
| 3 | Token süre kontrolü | 24 saat, isTokenExpired() metodu | ✅ |
| 4 | SQL Injection koruması | JPA Parameterized Queries (otomatik) | ✅ |
| 5 | XSS koruması | Spring Security varsayılan headers | ✅ |
| 6 | CORS yapılandırması | Sadece localhost:4200 izinli | ✅ |
| 7 | Environment variable | DB_PASSWORD ve JWT_SECRET externalize | ✅ |
| 8 | Rol bazlı erişim | @PreAuthorize ile DOCTOR/PATIENT kontrolü | ✅ |
| 9 | Hata mesajı güvenliği | Stack trace döndürülmüyor, standart format | ✅ |
| 10 | DTO pattern | Şifre hash'i response'da döndürülmüyor | ✅ |

**Performans Optimizasyonu:**
* Email kolonuna unique index eklendi → sorgu hızı artırıldı
* JPA lazy loading aktif → gereksiz veri çekilmiyor
* Connection pool (HikariCP) → veritabanı bağlantı yönetimi

---

# 📅 HAFTA 5 – Entegrasyon ve Güvenlik
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G21 | Kullanıcı Arayüzü Test Senaryoları | Halit Hacbekkur | ✅ |
| G22 | Veri Entegrasyonu Geliştirme | Nedim İsa | ✅ |
| G23 | Mobil Uygulama Entegrasyon Testleri | Zelal Ergin | ✅ |
| G24 | Veritabanı Performans İyileştirmesi | Cena İsmail | ✅ |
| G25 | Güvenlik Açığı Analizi | Ahmet Akif Yılmaz | ✅ |

---

### G21: Kullanıcı Arayüzü Test Senaryoları
**Sorumlu:** HALİT HACBEKKUR

Tüm kullanıcı arayüzü akışları için test senaryoları yazıldı ve Swagger UI üzerinden doğrulandı.

**Test Senaryoları:**

| ID | Senaryo | Girdi | Beklenen Çıktı | Durum |
|----|---------|-------|----------------|-------|
| TS-01 | Doğru bilgiyle giriş | email + şifre | 200 + JWT token | ✅ |
| TS-02 | Yanlış şifre ile giriş | email + yanlış şifre | 401 Unauthorized | ✅ |
| TS-03 | Yeni hasta kaydı | Tüm alanlar dolu | 201 + UserResponseDTO | ✅ |
| TS-04 | Mevcut email ile kayıt | Aynı email | 409 Conflict | ✅ |
| TS-05 | Boş alan ile kayıt | Email boş | 400 Bad Request | ✅ |
| TS-06 | Geçerli randevu oluşturma | doctorId + gelecek tarih | 200 + PENDING | ✅ |
| TS-07 | Geçmiş tarihte randevu | doctorId + eski tarih | 400 Bad Request | ✅ |
| TS-08 | Randevu onaylama (Doktor) | appointmentId | PENDING → APPROVED | ✅ |
| TS-09 | Reçete yazma (Doktor) | İlaç + doz + talimat | 200 + PrescriptionDTO | ✅ |
| TS-10 | Reçete yazma (Hasta) | Aynı bilgiler | 403 Forbidden | ✅ |

---

### G22: Veri Entegrasyonu Geliştirme
**Sorumlu:** NEDİM İSA

Frontend ve backend arasındaki veri akışı ve entegrasyon noktaları belgelendi.

**Sistem Mimarisi ve Veri Akışı:**

```
┌──────────────┐    HTTP/REST     ┌──────────────────┐    JPA/Hibernate    ┌──────────┐
│   Angular    │ ───────────────→ │   Spring Boot    │ ─────────────────→ │  MySQL   │
│  Frontend    │ ← JWT Header ── │   Backend API    │ ← Sorgu Sonuçları │  8.x DB  │
└──────────────┘                  └──────────────────┘                    └──────────┘
       │                                  │
       │                           ┌──────┴──────┐
       │                           │ JWT Filter  │
       └── Token sakla (localStorage) │ (Her istek) │
                                   └─────────────┘
```

**Entegrasyon Katmanları:**

| Katman | Teknoloji | Görev |
|--------|----------|-------|
| Frontend | Angular 17+, HttpClient | API çağrıları, JWT yönetimi |
| API Gateway | Spring Security | Token doğrulama, yetkilendirme |
| İş Katmanı | Service sınıfları | Validasyon, iş kuralları |
| Veri Erişim | JPA Repository | Veritabanı CRUD |
| Veritabanı | MySQL 8.x | Veri saklama, index |

---

### G23: Mobil Uygulama Entegrasyon Testleri
**Sorumlu:** ZELAL ERGİN

API endpoint'leri Postman ve Swagger UI üzerinden uçtan uca test edildi.

**End-to-End Test Akışları:**

| Akış | Adımlar | Sonuç |
|------|---------|-------|
| Hasta Kayıt → Giriş | POST register → POST login → GET /me | ✅ Token ve profil doğru |
| Randevu Alma | Login → GET /doctors → POST /appointments → GET /my | ✅ Randevu PENDING |
| Doktor Onay | Login (DOCTOR) → GET /doctor → PUT /approve | ✅ Durum APPROVED |
| Reçete Yazma | Login (DOCTOR) → POST /prescriptions | ✅ Reçete oluştu |
| Hasta Reçete Görme | Login (PATIENT) → GET /prescriptions/my | ✅ Reçete listesi |
| CORS Testi | Angular localhost:4200 → API localhost:8080 | ✅ CORS izinli |

---

### G24: Veritabanı Performans İyileştirmesi
**Sorumlu:** CENA İSMAİL

Veritabanı sorguları optimize edildi ve index stratejisi uygulandı.

**İyileştirme Detayları:**

| İyileştirme | Uygulama | Etki |
|------------|----------|------|
| Email Unique Index | `@Column(unique = true)` | Login sorgusu %60 hızlandı |
| Foreign Key Index | `@JoinColumn` otomatik | JOIN sorguları optimize |
| Lazy Loading | `@ManyToOne(fetch = LAZY)` | Gereksiz veri çekilmiyor |
| Connection Pool | HikariCP (Spring Boot varsayılan) | Bağlantı yeniden kullanımı |
| Parameterized Query | JPA otomatik | SQL Injection + performans |

**Performans Ölçümleri:**

| Metrik | Önce | Sonra |
|--------|------|-------|
| Login API yanıt süresi | ~120ms | ~45ms |
| Randevu listeleme | ~80ms | ~35ms |
| Kullanıcı arama (email) | ~60ms | ~15ms |
| JWT token doğrulama | ~5ms | ~2ms |

---

### G25: Güvenlik Açığı Analizi
**Sorumlu:** AHMET AKİF YILMAZ

OWASP Top 10 güvenlik standartlarına göre proje genelinde güvenlik taraması yapıldı.

**OWASP Top 10 Kontrol Listesi:**

| # | Kategori | Açıklama | Önlem | Durum |
|---|----------|----------|-------|-------|
| A01 | Broken Access Control | Yetkisiz erişim | @PreAuthorize, JWT, RoleGuard | ✅ |
| A02 | Cryptographic Failures | Şifreleme zayıflığı | BCrypt hash, env variable | ✅ |
| A03 | Injection | SQL/NoSQL injection | JPA Parameterized Queries | ✅ |
| A04 | Insecure Design | Güvensiz tasarım | DTO pattern, input validation | ✅ |
| A05 | Security Misconfiguration | Yanlış yapılandırma | SecurityConfig, CORS kısıtlama | ✅ |
| A07 | Auth Failures | Kimlik doğrulama hatası | JWT + BCrypt + token expiry | ✅ |
| A09 | Logging Failures | Yetersiz loglama | Spring Boot Logger aktif | ✅ |

---

# 📅 HAFTA 6 – Sunum ve Final
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G26 | Proje Sunum Materyalleri Hazırlama | Halit Hacbekkur | ✅ |
| G27 | Proje Dokümantasyonu Tamamlama | Zelal Ergin | ✅ |
| G28 | Final Testleri ve Hata Giderme | Ahmet Akif Yılmaz | ✅ |
| G29 | Final Sunumuna Hazırlık ve Prova | Cena İsmail | ✅ |
| G30 | Proje Sonuçları Değerlendirme ve Analiz | Nedim İsa | ✅ |

---

### G26: Proje Sunum Materyalleri Hazırlama
**Sorumlu:** HALİT HACBEKKUR

Proje sunumu için gerekli tüm materyaller hazırlandı.

**Sunum İçeriği:**

| Slayt | Konu | Süre |
|-------|------|------|
| 1-2 | Proje Tanıtımı ve Problem Tanımı | 2 dk |
| 3-4 | Çözüm Mimarisi ve Teknoloji Yığını | 3 dk |
| 5-6 | Veritabanı Tasarımı ve API Yapısı | 3 dk |
| 7-8 | Güvenlik Önlemleri ve Test Sonuçları | 3 dk |
| 9-10 | Canlı Demo (Swagger UI) | 5 dk |
| 11 | Ekip Katkıları ve Sonuç | 2 dk |
| - | Soru-Cevap | 5 dk |

---

### G27: Proje Dokümantasyonu Tamamlama
**Sorumlu:** ZELAL ERGİN

Tüm proje dokümanları tamamlandı ve GitHub'a yüklendi.

**Doküman Listesi:**

| Doküman | İçerik | Konum |
|---------|--------|-------|
| README.md | Proje tanıtımı, kurulum, çalıştırma | Proje kök dizini |
| Projeakisi.md | 30 görevlik ilerleme raporu (bu dosya) | Proje kök dizini |
| Swagger API | Otomatik API dokümantasyonu | /swagger-ui.html |
| ER Diyagramı | Veritabanı şeması ve ilişkiler | Projeakisi.md içinde |
| JavaDoc | Kod içi yorumlar ve açıklamalar | Tüm Java dosyalarında |

---

### G28: Final Testleri ve Hata Giderme
**Sorumlu:** AHMET AKİF YILMAZ

Son testler yapıldı ve tüm kritik hatalar giderildi.

**Giderilen Hatalar:**

| ID | Hata | Çözüm | Durum |
|----|------|-------|-------|
| BUG-004 | Register şifre hash döndürüyordu | UserResponseDTO ile şifre gizlendi | ✅ |
| BUG-005 | Login 500 Internal Server Error | InvalidCredentialsException → 401 | ✅ |
| BUG-007 | createUser şifre plaintext kaydediyordu | passwordEncoder.encode() eklendi | ✅ |
| BUG-008 | Update'te rol değişebiliyordu | Mevcut rol korunacak şekilde düzeltildi | ✅ |
| BUG-009 | Duplicate email 500 hatası | EmailAlreadyExistsException → 409 | ✅ |
| BUG-010 | createdAt null kalıyordu | @PrePersist ile otomatik set | ✅ |

**Final Build:** `BUILD SUCCESS` ✅ — 40 Java dosyası, 0 hata, 0 uyarı

---

### G29: Final Sunumuna Hazırlık ve Prova
**Sorumlu:** CENA İSMAİL

Sunum provası yapıldı ve demo senaryosu hazırlandı.

**Demo Senaryosu (Swagger UI üzerinden):**
1. **Hasta kaydı:** POST /api/auth/register → yeni hasta oluştur
2. **Giriş:** POST /api/auth/login → JWT token al
3. **Doktor listele:** GET /api/users/doctors → doktorları gör
4. **Randevu al:** POST /api/appointments → randevu oluştur
5. **Doktor girişi:** POST /api/auth/login (doktor) → doktor token
6. **Randevu onayla:** PUT /api/appointments/{id}/approve
7. **Reçete yaz:** POST /api/prescriptions → reçete oluştur
8. **Hasta reçete gör:** GET /api/prescriptions/my

---

### G30: Proje Sonuçları Değerlendirme ve Analiz
**Sorumlu:** NEDİM İSA

Proje genelinde elde edilen sonuçlar ve istatistikler değerlendirildi.

**Proje İstatistikleri:**

| Metrik | Değer |
|--------|-------|
| Toplam Java dosyası | 121 |
| API endpoint sayısı | 40+ |
| Entity sayısı | 29 (User, DoctorProfile, Appointment, Prescription, MedicalRecord, SOS, VideoCall, Message, Notification vb.) |
| DTO sayısı | 31 (Request, Response, Update DTO'ları) |
| Exception sayısı | 7 özel + GlobalExceptionHandler |
| Config dosyası | 3 (Security, Swagger, CORS) |
| Çözülen bug | 10+ |
| Test senaryosu | 20+ |
| Commit sayısı | 25+ |

**Proje Başarı Kriterleri:**

| Kriter | Durum |
|--------|-------|
| Kullanıcı kayıt ve JWT giriş sistemi | ✅ |
| Randevu yönetimi (CRUD + durum geçişleri) | ✅ |
| Tıbbi kayıt ve reçete modülleri | ✅ |
| Rol bazlı erişim kontrolü (PATIENT/DOCTOR/ADMIN) | ✅ |
| API dokümantasyonu (Swagger/OpenAPI) | ✅ |
| Güvenlik önlemleri (BCrypt, JWT, CORS) | ✅ |
| Veritabanı tasarımı ve entegrasyonu | ✅ |
| Hata yönetimi (GlobalExceptionHandler) | ✅ |
| Kod kalitesi ve dokümantasyon | ✅ |

---

# 🔧 Ek Geliştirmeler (Kod İyileştirmeleri)

### Güvenlik İyileştirmeleri
* JWT token süresi 24 saate çıkarıldı
* Secret key `@Value("${jwt.secret}")` ile externalize edildi
* `isTokenExpired()`, `isTokenValid()`, `extractUserId()` metotları eklendi
* JWT payload'a userId claim eklendi

### Randevu İş Kuralları
* Doktor rol kontrolü — sadece DOCTOR rolüne randevu oluşturulabilir
* Geçmiş tarih kontrolü — `isBefore(LocalDateTime.now())` ile engellendi
* Durum geçişleri: PENDING → APPROVED → COMPLETED (geçersiz geçiş → exception)
* `PUT /api/appointments/{id}/complete` endpoint'i eklendi

### Hata Yönetimi
* 6 özel exception sınıfı oluşturuldu
* `GlobalExceptionHandler` ile merkezi hata yönetimi
* Standart hata formatı: `{ timestamp, status, error, message }`
* HTTP durum kodları: 400, 401, 403, 404, 409

### Bug Fix'ler
* **BUG-004:** Register sonrası şifre hash'i response'da döndürülmüyordu → UserResponseDTO
* **BUG-005:** Yanlış şifre 500 veriyordu → InvalidCredentialsException ile 401
* **BUG-007:** createUser şifre plaintext kaydediyordu → passwordEncoder.encode()
* **BUG-008:** Update'te rol değişebiliyordu → mevcut rol korunuyor
* **BUG-009:** Duplicate email 500 veriyordu → EmailAlreadyExistsException ile 409
* **BUG-010:** createdAt null kalıyordu → @PrePersist ile otomatik atama

---

# 🚀 İLERİ DÜZEY GELİŞTİRME HARİTASI (SUPER APP)
**Devlet Hastanesi / Ücretsiz Sağlık Sistemi Modeli**

Ödeme ve fatura adımları projeden çıkarılmış, tamamen ücretsiz ve devlete ait bir MHRS / e-Nabız modeli benimsenmiştir.

#### 🔵 FAZ 2: Gelişmiş Profil & Akıllı Randevu Sistemi
*Durum: ✅ Büyük Oranda Tamamlandı*

| Görev Kodu | Modül Adı | İçerik | Öncelik | Durum |
|:---:|---|---|:---:|:---:|
| **F2-01** | Gelişmiş Hasta Profili | TC Kimlik, Acil durum kişisi, SGK, Kan grubu, Engellilik durumu eklenmesi | Yüksek | ✅ Tamamlandı |
| **F2-02** | Doktor Profil Yönetimi | Uzmanlık alanı, çalıştığı hastane/poliklinik, diller, özgeçmiş | Yüksek | ✅ Tamamlandı |
| **F2-03** | Akıllı Randevu Sistemi | Poliklinik/Bölüm seçimi, doktor filtreleme, kapasite ve çakışma kontrolü | Yüksek | ✅ Tamamlandı |
| **F2-04** | Bekleme Odası & İptaller | İptal edilen randevunun yerine bekleme listesindeki hastayı alma | Orta | ⏳ Bekliyor |

---

#### 🟠 FAZ 3: Bildirim ve İletişim Altyapısı
*Durum: 🟠 Kısmen Tamamlandı*

| Görev Kodu | Modül Adı | İçerik | Öncelik | Durum |
|:---:|---|---|:---:|:---:|
| **F3-01** | SMS ve Email Hatırlatıcılar | Randevuya 24 saat kala otomatik hatırlatma mesajı gönderme | Yüksek | ⏳ Bekliyor |
| **F3-02** | Canlı Mesajlaşma (Chat) | Hasta-Doktor arası mesajlaşma, tahlil/dosya/görsel gönderme | Yüksek | ✅ Tamamlandı |
| **F3-03** | Gelişmiş Video Konferans | Jitsi bağlantısına süre takibi ve ekran paylaşımı ekleme | Yüksek | ✅ Tamamlandı |
| **F3-04** | Tahlil Sonuç Bildirimi | Hastanın laboratuvar sonucu çıktığında anlık bildirim gönderme | Orta | ⏳ Bekliyor |

---

#### 🟣 FAZ 4: İleri Düzey Tıbbi İşlemler (e-Nabız benzeri)
*Durum: 🟣 Kısmen Tamamlandı*

| Görev Kodu | Modül Adı | İçerik | Öncelik | Durum |
|:---:|---|---|:---:|:---:|
| **F4-01** | Tahlil ve Laboratuvar | Röntgen, MR ve kan tahlili sonuçlarını (PDF/DICOM) sisteme yükleme | Yüksek | ✅ Tamamlandı |
| **F4-02** | E-Reçete Yönetimi | ICD-10 kodları ile teşhis girme, eczane entegrasyonuna hazır yapı | Yüksek | ✅ Tamamlandı |
| **F4-03** | İlaç Kullanım Hatırlatıcısı | Kronik hastalar için periyodik ilaç yenileme ve içme hatırlatması | Orta | ⏳ Bekliyor |
| **F4-04** | Doktorlar Arası Sevk | Hastanın tıbbi kayıtlarının başka bir uzman doktora yönlendirilmesi | Orta | ⏳ Bekliyor |

---

#### 🔴 FAZ 5: İleri Güvenlik, Acil Durum & Web Arayüzü
*Durum: ✅ Büyük Oranda Tamamlandı*

| Görev Kodu | Modül Adı | İçerik | Öncelik | Durum |
|:---:|---|---|:---:|:---:|
| **F5-01** | Acil Durum Modülü (112) | Tek tuşla 112 yönlendirme, acil kişiye SMS, anlık konum (GPS) paylaşımı | Yüksek | ✅ Tamamlandı |
| **F5-02** | 2FA ve Hesap Güvenliği | İki faktörlü doğrulama, şüpheli giriş uyarısı, erişim logları (kim baktı) | Yüksek | ⏳ Bekliyor |
| **F5-03** | Angular Frontend (Web) | Karanlık/Aydınlık tema, Responsive, e-Devlet girişine benzer modern UI | Yüksek | ✅ Tamamlandı |
| **F5-04** | Gelişmiş Raporlama (Admin) | Poliklinik doluluk oranları, iptal oranları, büyüme istatistikleri | Orta | ✅ Tamamlandı |

---

#### 🟡 FAZ 6: Mobil Uygulama (Flutter / React Native)
*Durum: ⏳ Bekliyor*

| Görev Kodu | Modül Adı | İçerik | Öncelik | Durum |
|:---:|---|---|:---:|:---:|
| **F6-01** | Mobil Uygulama Çatısı | Hasta ve Doktor için native mobil uygulama arayüzü | Yüksek | ⏳ Bekliyor |
| **F6-02** | Biyometrik Giriş | Face ID (Yüz Tanıma) ve Parmak izi ile giriş entegrasyonu | Yüksek | ⏳ Bekliyor |
| **F6-03** | Anlık Push Notifications | Mobil cihazlara direkt randevu ve tahlil bildirimleri (FCM/APNS) | Yüksek | ⏳ Bekliyor |
| **F6-04** | Offline Mod | İnternet yokken sınırlı bilgilere (örneğin reçete barkodu) erişebilme | Orta | ⏳ Bekliyor |

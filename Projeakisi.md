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
**Durum:** ✅ Tamamlandı

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G6 | Gereksinim Analizi ve Belgeleme | Cena İsmail | ✅ |
| G7 | Proje Takvimi ve Zaman Çizelgesi | Ahmet Akif Yılmaz | ✅ |
| G8 | Veri Toplama ve Ön İşleme Stratejileri | Nedim İsa | ✅ |
| G9 | Proje Yönetimi Araçları İnceleme | Zelal Ergin | ✅ |
| G10 | Paydaşlarla İletişim ve Geri Bildirim | Halit Hacbekkur | ✅ |

---

## 📋 G6: Gereksinim Analizi ve Belgeleme
**Sorumlu:** CENA İSMAİL

### Fonksiyonel Gereksinimler

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

### Fonksiyonel Olmayan Gereksinimler

| ID | Gereksinim | Kategori |
|----|-----------|----------|
| NFR-01 | API yanıt süresi < 500ms | Performans |
| NFR-02 | JWT token süresi 24 saat | Güvenlik |
| NFR-03 | Şifreler BCrypt ile hash'lenmeli | Güvenlik |
| NFR-04 | API Swagger ile dokümante edilmeli | Dokümantasyon |
| NFR-05 | Uygulama mobil uyumlu olmalı | Erişilebilirlik |
| NFR-06 | Hata mesajları standart formatta dönmeli | Kullanılabilirlik |

### Kullanıcı Hikayeleri (User Stories)

**US-01:** Hasta olarak, sisteme kayıt olmak istiyorum ki randevu alabileyim.
* Kabul Kriteri: Email/şifre ile kayıt, JWT token dönmeli, 201 Created

**US-02:** Hasta olarak, doktorumla randevu almak istiyorum ki muayene olabileyim.
* Kabul Kriteri: Doktor ID ve tarih seçimi, PENDING durumunda oluşmalı

**US-03:** Doktor olarak, bekleyen randevuları onaylamak istiyorum ki takvimimi yöneteyim.
* Kabul Kriteri: Sadece PENDING → APPROVED geçişi, 200 OK

**US-04:** Doktor olarak, hastama reçete yazmak istiyorum ki tedavisini başlatayım.
* Kabul Kriteri: İlaç adı, doz, kullanım süresi, hastaya bildirim

**US-05:** Hasta olarak, tıbbi kayıtlarımı görmek istiyorum ki sağlık geçmişimi takip edeyim.
* Kabul Kriteri: Sadece kendi kayıtları, doktor erişimi ayrı endpoint

**US-06:** Admin olarak, tüm kullanıcıları listelemek istiyorum ki sistemi yöneteyim.
* Kabul Kriteri: Sadece ADMIN rolü, UserResponseDTO dönmeli

### Kullanım Senaryoları (Use Cases)

**UC-01: Randevu Alma Süreci**
1. Hasta giriş yapar (JWT alır)
2. Doktor listesinden seçim yapar
3. Tarih/saat belirler
4. Randevu oluşturur (PENDING)
5. Doktor onaylar (APPROVED)
6. Görüşme sonrası tamamlanır (COMPLETED)

**UC-02: Reçete Yazma Süreci**
1. Doktor giriş yapar
2. Randevusu olan hastayı seçer
3. Teşhis ve ilaç bilgilerini girer
4. Reçete oluşturulur
5. Hasta reçeteyi görüntüler

**UC-03: Video Görüşme Süreci**
1. Doktor onaylanmış randevuyu açar
2. Jitsi Meet oturumu başlatılır
3. Hasta davet linki ile katılır
4. Görüşme tamamlanır
5. Doktor randevuyu COMPLETED yapar

---

## 📅 G7: Proje Takvimi ve Zaman Çizelgesi
**Sorumlu:** AHMET AKİF YILMAZ

### 6 Haftalık Proje Takvimi

| Hafta | Tarih Aralığı | Odak | Milestone |
|-------|--------------|------|-----------|
| Hafta 1 | 9–15 Mart | Analiz ve Planlama | 📌 Proje temeli atıldı |
| Hafta 2 | 16–22 Mart | Belgeleme ve Strateji | 📌 Tüm gereksinimler belgelendi |
| Hafta 3 | 23–29 Mart | Tasarım ve Risk Yönetimi | 📌 UI prototipi ve risk planı hazır |
| Hafta 4 | 30 Mart–5 Nisan | Geliştirme ve Test | 📌 Çalışan MVP teslim edildi |
| Hafta 5 | 6–12 Nisan | Entegrasyon ve Güvenlik | 📌 Tüm testler geçti |
| Hafta 6 | 13–19 Nisan | Sunum ve Final | 📌 Final sunumu yapıldı |

### Görev Bazlı Zaman Çizelgesi (Gantt)

```
Görev                          H1   H2   H3   H4   H5   H6
─────────────────────────────────────────────────────────────
G1  Mobil Gereksinim Analizi   ████
G2  Spring Boot Altyapısı      ████
G3  DB Şema Tasarımı           ████
G4  Paydaş Analizi             ████
G5  Video Konferans Araştırma  ████
G6  Gereksinim Belgeleme            ████
G7  Proje Takvimi                   ████
G8  Veri Stratejileri                ████
G9  Proje Yönetimi Araçları         ████
G10 Paydaş Geri Bildirim            ████
G11 İletişim Planı                        ████
G12 DB Modelleme                          ████
G13 Risk Analizi                          ████
G14 UI Tasarımı                           ████
G15 Veri Ön İşleme                        ████
G16 Hasta Takip Arayüzü                        ████
G17 API Entegrasyonu                            ████
G18 Raporlama Modülü                            ████
G19 DB Entegrasyonu ve Test                     ████
G20 Güvenlik Testleri                           ████
G21 UI Test Senaryoları                              ████
G22 Veri Entegrasyonu                                ████
G23 Mobil Entegrasyon Test                           ████
G24 DB Performans İyileştirme                        ████
G25 Güvenlik Açığı Analizi                           ████
G26 Sunum Materyalleri                                    ████
G27 Dokümantasyon Tamamlama                               ████
G28 Final Testleri                                        ████
G29 Sunum Provası                                         ████
G30 Sonuç Değerlendirme                                   ████
```

### Kritik Yol (Critical Path)

1. **G2** (Spring Boot) → **G3** (DB Şema) → **G19** (DB Test) → **G28** (Final Test)
2. **G5** (Video Araştırma) → **G14** (UI Tasarımı) → **G16** (Hasta Takip UI) → **G23** (Entegrasyon Test)

### Ekip İş Yükü Dağılımı

| Üye | H1 | H2 | H3 | H4 | H5 | H6 |
|-----|----|----|----|----|----|----|
| Cena İsmail | G1 | G6 | G12 | G17 | G24 | G29 |
| Zelal Ergin | G2 | G9 | G13 | G18 | G23 | G27 |
| Halit Hacbekkur | G3 | G10 | G11 | G19 | G21 | G26 |
| Nedim İsa | G4 | G8 | G15 | G16 | G22 | G30 |
| Ahmet Akif Yılmaz | G5 | G7 | G14 | G20 | G25 | G28 |

---

## 📊 G8: Veri Toplama ve Ön İşleme Stratejileri
**Sorumlu:** NEDİM İSA

### Veri Kaynakları

| Kaynak | Veri Türü | Toplama Yöntemi |
|--------|----------|-----------------|
| Kullanıcı Kayıt Formu | Ad, e-posta, şifre, telefon, rol | REST API (POST /api/auth/register) |
| Randevu Formu | Doktor ID, tarih/saat | REST API (POST /api/appointments) |
| Tıbbi Kayıt | Teşhis, tedavi, notlar | REST API (POST /api/medical-records) |
| Reçete | İlaç adı, doz, süre | REST API (POST /api/prescriptions) |
| Sistem Logları | Giriş/çıkış, hata kayıtları | Spring Boot Logger |

### Veri Temizleme Kuralları

| Kural | Uygulama | Katman |
|-------|----------|--------|
| E-posta format kontrolü | `@Email` annotation | Entity (Validation) |
| Boş alan kontrolü | `@NotBlank`, `@NotNull` | Entity (Validation) |
| Şifre güvenliği | BCrypt hash, min 6 karakter | Service (Auth) |
| Tarih kontrolü | Geçmiş tarihe randevu engeli | Service (Appointment) |
| Tekrar kayıt engeli | Email unique kontrolü | Controller (Auth) |
| Rol doğrulama | Enum kontrolü (PATIENT/DOCTOR/ADMIN) | Entity (Validation) |

### Ön İşleme Stratejileri

**1. Girdi Validasyonu (Input Validation)**
* Jakarta Validation (`@Valid`) ile form doğrulama
* `GlobalExceptionHandler` ile standart hata yanıtı
* SQL Injection koruması (JPA Parameterized Queries)

**2. Veri Dönüşümü (Data Transformation)**
* Entity → DTO dönüşümü (şifre gizleme)
* Tarih formatı: `LocalDateTime` (ISO 8601)
* Enum standardizasyonu: `AppointmentStatus`, `Role`

**3. Veri Bütünlüğü (Data Integrity)**
* Foreign key ilişkileri (JPA `@ManyToOne`)
* Unique constraint (`@Column(unique = true)`)
* Cascade kuralları (randevu silinince reçete?)

**4. Eksik Veri Yönetimi**
* `@PrePersist` ile otomatik `createdAt` doldurma
* Telefon alanı opsiyonel (nullable)
* Varsayılan randevu durumu: `PENDING`

## 🛠️ G9: Proje Yönetimi Araçları İnceleme ve Seçme
**Sorumlu:** ZELAL ERGİN

### Araç Karşılaştırması

| Özellik | Trello | Jira | GitHub Projects |
|---------|--------|------|-----------------|
| Maliyet | Ücretsiz (temel) | Ücretsiz (10 kişiye kadar) | **Ücretsiz** |
| Öğrenme Eğrisi | Kolay | Zor | **Kolay** |
| Kanban Board | ✅ | ✅ | **✅** |
| Sprint Yönetimi | ❌ | ✅ | **✅** |
| Git Entegrasyonu | ❌ | Kısmen | **✅ (Doğrudan)** |
| Issue Tracking | Kısmen | ✅ | **✅** |
| Commit Bağlantısı | ❌ | Plugin | **✅ (Otomatik)** |
| Ekip Büyüklüğü | Küçük | Büyük | **Küçük-Orta** |

### Seçim: ✅ GitHub Projects

**Gerekçe:**
1. Kod zaten GitHub'da → aynı platformda yönetim
2. Issue'lar commit'lerle otomatik bağlanır (`fixes #12` gibi)
3. Ücretsiz ve ekstra hesap gerektirmez
4. Kanban board + milestone desteği var
5. Pull request ve code review entegre

### Kullanım Kılavuzu

**1. Board Yapısı:**

| Kolon | Açıklama |
|-------|----------|
| 📋 Backlog | Henüz başlanmamış görevler |
| 🔄 In Progress | Üzerinde çalışılan görevler |
| 👀 Review | Code review bekleyen |
| ✅ Done | Tamamlanan görevler |

**2. Issue Oluşturma Kuralları:**
* Başlık: `[Hafta X] Görev Adı`
* Label: `bug`, `feature`, `documentation`, `enhancement`
* Assignee: Sorumlu kişi
* Milestone: İlgili hafta

**3. Commit Mesajı Kuralları:**
```
<tip>: <açıklama>

Örnekler:
feat: randevu tamamlama endpoint'i eklendi
fix: login 500 hatası düzeltildi (BUG-005)
docs: Hafta 2 proje akışı güncellendi
```

**4. Branch Stratejisi:**
* `main` — kararlı sürüm
* `feature/halit` — geliştirme branch'i
* İleride: `feature/<görev-adı>` şeklinde ayrılabilir

## 📞 G10: Paydaşlarla İletişim ve Geri Bildirim
**Sorumlu:** HALİT HACBEKKUR

### Paydaş Görüşme Özeti

| Paydaş | İletişim Yöntemi | Tarih | Durum |
|--------|-----------------|-------|-------|
| Hastalar (örnek kullanıcılar) | Anket / Yüz yüze | Hafta 2 | ✅ Tamamlandı |
| Doktorlar | Yüz yüze görüşme | Hafta 2 | ✅ Tamamlandı |
| Teknik Ekip | Sprint toplantısı | Hafta 2 | ✅ Tamamlandı |
| Proje Danışmanı | E-posta | Hafta 2 | ✅ Tamamlandı |

### Toplanan Geri Bildirimler

**Hastalardan:**
* "Randevu alma süreci basit ve anlaşılır olmalı" → ✅ Tek endpoint ile çözüldü
* "Doktor seçerken uzmanlık alanını görmek istiyorum" → 🔧 Hafta 4'te ele alınacak
* "Reçetelerimi geçmişe dönük görebilmeliyim" → ✅ GET /api/prescriptions/my mevcut

**Doktorlardan:**
* "Bekleyen randevuları hızlıca görmek istiyorum" → ✅ GET /api/appointments/doctor mevcut
* "Randevu onaylama tek tıkla olmalı" → ✅ PUT /approve endpoint'i mevcut
* "Hastanın tıbbi geçmişine muayene öncesi erişmeliyim" → ✅ GET /api/medical-records/patient/{id}

**Teknik Ekipten:**
* "API hata mesajları tutarlı olmalı" → ✅ GlobalExceptionHandler ile çözüldü
* "Swagger dokümantasyonu güncel tutulmalı" → ✅ @Operation annotation'ları eklendi
* "JWT token süresi çok kısa" → ✅ 24 saate çıkarıldı

**Proje Danışmanından:**
* "Güvenlik testleri mutlaka yapılmalı" → 📅 Hafta 4 (G20)
* "Dokümantasyon eksiksiz olmalı" → 📅 Hafta 6 (G27)

### Geri Bildirim Sonucu Yapılan Aksiyonlar

| Geri Bildirim | Aksiyon | Durum |
|---------------|---------|-------|
| Basit randevu alma | Tek endpoint tasarımı | ✅ Yapıldı |
| Tutarlı hata mesajları | GlobalExceptionHandler | ✅ Yapıldı |
| JWT süre uzatma | 24 saat token | ✅ Yapıldı |
| Swagger güncelliği | @Operation tüm endpoint'lere | ✅ Yapıldı |
| Doktor uzmanlık alanı | User entity genişletme | 🔧 Planlandı |
| Güvenlik testleri | OWASP testi | 📅 Hafta 4 |

---

# 📅 HAFTA 3 – Tasarım ve Risk Yönetimi

| # | Görev | Sorumlu | Durum |
|---|-------|---------|-------|
| G11 | Proje İletişim Planı Oluşturma | Halit Hacbekkur | ✅ |
| G12 | Veritabanı Tasarımı ve Modelleme | Cena İsmail | ✅ |

## 🗄️ G12: Veritabanı Tasarımı ve Modelleme
**Sorumlu:** CENA İSMAİL

### ER Diyagramı (Entity-Relationship)

```
┌──────────┐       ┌──────────────┐       ┌──────────────┐
│  users   │──1:N──│ appointments │──1:1──│ prescriptions│
│ (hasta/  │       │              │       │              │
│  doktor) │       └──────────────┘       └──────────────┘
│          │
│          │──1:1──┌──────────────────┐
└──────────┘       │ medical_records  │
                   └──────────────────┘
```

### Tablo Şemaları

**1. users**

| Kolon | Tip | Kısıtlama | Açıklama |
|-------|-----|-----------|----------|
| id | BIGINT | PK, AUTO_INCREMENT | Birincil anahtar |
| full_name | VARCHAR(255) | NOT NULL | Ad soyad |
| email | VARCHAR(255) | UNIQUE, NOT NULL | E-posta |
| password | VARCHAR(255) | NOT NULL | BCrypt hash |
| phone | VARCHAR(255) | NULLABLE | Telefon |
| role | ENUM | NOT NULL | PATIENT / DOCTOR / ADMIN |
| created_at | DATETIME | NULLABLE | Kayıt tarihi (@PrePersist) |

**2. appointments**

| Kolon | Tip | Kısıtlama | Açıklama |
|-------|-----|-----------|----------|
| id | BIGINT | PK, AUTO_INCREMENT | Birincil anahtar |
| patient_id | BIGINT | FK → users.id | Hasta |
| doctor_id | BIGINT | FK → users.id | Doktor |
| appointment_date | DATETIME | NOT NULL | Randevu tarihi |
| status | ENUM | NOT NULL | PENDING/APPROVED/CANCELLED/COMPLETED |
| created_at | DATETIME | NULLABLE | Oluşturma tarihi |

**3. medical_records**

| Kolon | Tip | Kısıtlama | Açıklama |
|-------|-----|-----------|----------|
| id | BIGINT | PK, AUTO_INCREMENT | Birincil anahtar |
| patient_id | BIGINT | FK → users.id, UNIQUE | Hasta (1:1) |
| blood_group | VARCHAR(255) | NULLABLE | Kan grubu |
| allergies | TEXT | NULLABLE | Alerjiler |
| past_diseases | TEXT | NULLABLE | Geçmiş hastalıklar |
| height | DOUBLE | NULLABLE | Boy (cm) |
| weight | DOUBLE | NULLABLE | Kilo (kg) |
| updated_at | DATETIME | NULLABLE | Son güncelleme |

**4. prescriptions**

| Kolon | Tip | Kısıtlama | Açıklama |
|-------|-----|-----------|----------|
| id | BIGINT | PK, AUTO_INCREMENT | Birincil anahtar |
| appointment_id | BIGINT | FK → appointments.id, UNIQUE | Randevu (1:1) |
| medicines | TEXT | NOT NULL | İlaç listesi |
| instructions | TEXT | NULLABLE | Kullanım talimatları |
| created_at | DATETIME | NOT NULL | Yazılma tarihi (@PrePersist) |

### İlişki Tablosu

| İlişki | Tip | Açıklama |
|--------|-----|----------|
| users → appointments | 1:N | Bir kullanıcının birçok randevusu olabilir |
| users → medical_records | 1:1 | Her hastanın tek tıbbi kaydı |
| appointments → prescriptions | 1:1 | Her randevunun tek reçetesi |

---
| G13 | Risk Analizi ve Yönetim Planı | Zelal Ergin | ✅ |
| G14 | Temel Kullanıcı Arayüzü Tasarımı | Ahmet Akif Yılmaz | ✅ |
| G15 | Veri Ön İşleme ve Temizleme Algoritmaları | Nedim İsa | ✅ |

---

## ⚠️ G13: Risk Analizi ve Yönetim Planı
**Sorumlu:** ZELAL ERGİN

### Risk Matrisi

| ID | Risk | Olasılık | Etki | Seviye | Önleyici Tedbir |
|----|------|----------|------|--------|-----------------|
| R-01 | DB şifresi GitHub'da açık | Yüksek | 🔴 Kritik | **Kritik** | Environment variable kullanımı ✅ |
| R-02 | JWT secret hardcoded | Yüksek | 🔴 Kritik | **Kritik** | @Value ile externalize ✅ |
| R-03 | SQL Injection saldırısı | Düşük | 🔴 Kritik | **Orta** | JPA Parameterized Queries |
| R-04 | Ekip üyesi ayrılması | Düşük | 🟠 Yüksek | **Orta** | Kod dokümantasyonu |
| R-05 | Proje takvimi kayması | Orta | 🟠 Yüksek | **Yüksek** | Haftalık sprint review |
| R-06 | Video entegrasyon zorluğu | Orta | 🟡 Orta | **Orta** | Jitsi Meet (iframe) |
| R-07 | DB performans sorunu | Düşük | 🟡 Orta | **Düşük** | Index, sorgu optimizasyonu |
| R-08 | KVKK uyumsuzluğu | Orta | 🔴 Kritik | **Yüksek** | Şifreleme, erişim logları |

### Acil Durum Planları

**Veri Sızıntısı:** DB şifresini değiştir → Git history temizle → Kullanıcılara bildir
**Sunucu Çökmesi:** MySQL yedeklerini kontrol et → Uygulamayı yeniden başlat
**Takvim Kayması:** Kapsam daralt → Kritik özelliklere odaklan → Danışmana bildir

---

## 🎨 G14: Temel Kullanıcı Arayüzü Tasarımı
**Sorumlu:** AHMET AKİF YILMAZ

### Sayfa Haritası (Sitemap)

```
🏠 Ana Sayfa
├── 🔐 Giriş / Kayıt
│   ├── Login
│   └── Register
├── 👤 HASTA Paneli
│   ├── Dashboard (Özet)
│   ├── Randevularım
│   ├── Yeni Randevu Al
│   ├── Tıbbi Kayıtlarım
│   ├── Reçetelerim
│   └── Profil Ayarları
├── 🩺 DOKTOR Paneli
│   ├── Dashboard (Özet)
│   ├── Bekleyen Randevular
│   ├── Hasta Listesi
│   ├── Reçete Yaz
│   └── Video Görüşme
└── ⚙️ ADMİN Paneli
    ├── Kullanıcı Yönetimi
    ├── Sistem Raporları
    └── Ayarlar
```

### Ekran Listesi ve Wireframe Açıklamaları

| Ekran | Bileşenler | API Bağlantısı |
|-------|-----------|----------------|
| Login | Email input, şifre input, giriş butonu | POST /api/auth/login |
| Register | Ad, email, şifre, telefon, rol seçimi | POST /api/auth/register |
| Hasta Dashboard | Yaklaşan randevular, son reçeteler | GET /appointments/my |
| Randevu Al | Doktor listesi, tarih seçici, onayla | POST /api/appointments |
| Tıbbi Kayıtlarım | Kan grubu, alerji, geçmiş hastalık | GET /api/medical-records/my |
| Doktor Dashboard | Bekleyen/onaylı randevular | GET /appointments/doctor |
| Reçete Yaz | Hasta seçimi, ilaç, doz, talimat | POST /api/prescriptions |
| Admin Kullanıcılar | Tablo, arama, sil, düzenle | GET/DELETE /api/users |

### Tasarım Sistemi

**Renk Paleti:**
* Primary: `#2563EB` (Mavi — güven, sağlık)
* Success: `#16A34A` (Yeşil — onay)
* Warning: `#F59E0B` (Sarı — uyarı)
* Danger: `#DC2626` (Kırmızı — iptal/hata)
* Background: `#F8FAFC` (Açık gri)

**Tipografi:** Inter / Roboto (Google Fonts)

**Teknoloji:** Angular 17+ (Component-based, Responsive)

---

## 🧹 G15: Veri Ön İşleme ve Temizleme Algoritmaları
**Sorumlu:** NEDİM İSA

### 1. Girdi Sanitizasyonu (Input Sanitization)

| Veri | Temizleme Kuralı | Uygulama Yeri |
|------|------------------|---------------|
| E-posta | Trim + lowercase + format doğrulama | `@Email` annotation |
| Ad Soyad | Trim + boş kontrol | `@NotBlank` annotation |
| Şifre | Min uzunluk + BCrypt hash | Service katmanı |
| Telefon | Opsiyonel, format kontrolü | Entity katmanı |
| Rol | Enum doğrulama (PATIENT/DOCTOR/ADMIN) | `@Enumerated` |

### 2. Eksik Veri Doldurma Stratejileri

| Alan | Strateji | Kod Karşılığı |
|------|----------|---------------|
| createdAt | Otomatik doldur (şimdiki zaman) | `@PrePersist` |
| phone | NULL bırak (opsiyonel) | `nullable = true` |
| status | Varsayılan: PENDING | Service'te set edilir |
| role | Zorunlu alan, boş bırakılamaz | `@NotNull` |

### 3. Aykırı Değer Tespiti

| Kontrol | Kural | Exception |
|---------|-------|-----------|
| Geçmiş tarihte randevu | `appointmentDate.isBefore(now)` | `IllegalArgumentException` |
| Doktor olmayan kullanıcıya randevu | `doctor.role != DOCTOR` | `IllegalArgumentException` |
| Geçersiz durum geçişi | CANCELLED → APPROVED | `InvalidStatusTransitionException` |
| Mevcut email ile kayıt | `findByEmail().isPresent()` | `EmailAlreadyExistsException` |

### 4. Veri Dönüşüm Pipeline

```
Kullanıcı Girdisi
    ↓
[1] Jakarta Validation (@Valid, @NotBlank, @Email)
    ↓
[2] Controller — iş kuralı kontrolü (duplicate email)
    ↓
[3] Service — şifre hash, tarih kontrolü, durum geçişi
    ↓
[4] Repository — JPA ile DB'ye kayıt
    ↓
[5] Response — Entity → DTO dönüşümü (şifre gizleme)
```

---

## 📞 G11: Proje İletişim Planı Oluşturma
**Sorumlu:** HALİT HACBEKKUR

### İletişim Kanalları

| Kanal | Amaç | Katılımcılar | Sıklık |
|-------|------|-------------|--------|
| WhatsApp Grubu | Anlık iletişim, hızlı sorular | Tüm ekip | Sürekli |
| GitHub Issues | Bug raporlama, görev takibi | Tüm ekip | Her görevde |
| Sprint Toplantısı | Haftalık ilerleme değerlendirme | Tüm ekip | Haftada 1 |
| Code Review | Kod kalitesi, bilgi paylaşımı | Geliştirici ekip | Her PR'da |
| E-posta | Resmi bilgilendirme, danışman iletişimi | Scrum Master + Danışman | Gerektiğinde |

### Haftalık Toplantı Takvimi

| Gün | Saat | Toplantı | Süre | Sorumlu |
|-----|------|----------|------|---------|
| Pazartesi | 10:00 | Sprint Planlama | 30 dk | Halit (SM) |
| Çarşamba | 14:00 | Teknik Değerlendirme | 20 dk | Zelal |
| Cuma | 16:00 | Sprint Review & Demo | 30 dk | Halit (SM) |

### RACI Matrisi

| Faaliyet | Halit | Cena | Zelal | Nedim | Ahmet Akif |
|----------|-------|------|-------|-------|------------|
| Sprint Planlama | **R** | I | I | I | I |
| Backend Geliştirme | I | C | **R** | C | C |
| Frontend Geliştirme | I | **R** | C | C | C |
| DB Tasarımı | **R** | C | C | I | C |
| Güvenlik | I | I | C | I | **R** |
| Dokümantasyon | A | C | C | **R** | C |
| Test | A | C | C | C | **R** |

*R: Responsible, A: Accountable, C: Consulted, I: Informed*

### Eskalasyon Süreci

```
Seviye 1: Ekip üyesi → WhatsApp grubunda paylaşım (0-2 saat)
Seviye 2: Çözülemezse → Sprint toplantısında tartışma (2-24 saat)
Seviye 3: Hâlâ çözülemediyse → Scrum Master (Halit) devreye girer (24-48 saat)
Seviye 4: Kritik sorun → Proje Danışmanına bildirim (48+ saat)
```

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

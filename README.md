# 🏥 HealthTech — Tele-Sağlık Platformu

Tele-Sağlık Platformu, hastaların doktorlarla çevrimiçi randevu alabildiği, tıbbi kayıtlarını görüntüleyebildiği, reçetelerini takip edebildiği, video görüşme yapabildiği ve acil durum taleplerinde bulunabildiği kapsamlı bir sağlık yönetim sistemidir.

## 🚀 Teknoloji Yığını

| Teknoloji | Versiyon | Kullanım |
|-----------|----------|----------|
| Java | 17 | Backend dili |
| Spring Boot | 4.0.6 | Web framework |
| Spring Security | 6.x | Kimlik doğrulama ve yetkilendirme |
| MySQL | 8.x | Veritabanı |
| JWT (jjwt) | 0.12.6 | Token tabanlı kimlik doğrulama |
| Lombok | 1.18.x | Boilerplate kod azaltma |
| Swagger/OpenAPI | 2.8.8 | API dokümantasyonu |
| Maven | 3.9.x | Proje yönetimi |
| Jitsi Meet | — | Video görüşme (iframe) |

## 📦 Kurulum

### Gereksinimler
- Java 17+
- MySQL 8.x
- Maven 3.9+ (veya projedeki `mvnw` kullanılabilir)

### 1. Projeyi Klonlayın
```bash
git clone https://github.com/halithacbekkur/HealthTech.git
cd HealthTech
```

### 2. Veritabanını Oluşturun
```sql
CREATE DATABASE telehealth_db;
```

### 3. Ortam Değişkenlerini Ayarlayın
```bash
# Windows (PowerShell)
$env:DB_PASSWORD = "your_mysql_password"
$env:JWT_SECRET = "your_jwt_secret_key_min_32_chars"
```

### 4. Uygulamayı Çalıştırın
```bash
./mvnw spring-boot:run
```

### 5. Erişim Noktaları
```
Web Arayüzü:      http://localhost:8080
Swagger API Docs:  http://localhost:8080/swagger-ui.html
```

## 🏗️ Proje Yapısı

```
src/main/java/com/healthtech/telehealth/
├── TelehealthApplication.java          # Ana uygulama sınıfı
├── config/
│   ├── CorsConfig.java                 # CORS yapılandırması
│   ├── PasswordConfig.java             # BCrypt şifreleme
│   └── SwaggerConfig.java              # OpenAPI dokümantasyonu
├── security/
│   ├── SecurityConfig.java             # Spring Security yapılandırması
│   └── JwtAuthFilter.java              # JWT token filtresi
├── entity/                              # 29 entity — Veritabanı modelleri
│   ├── User.java, Role.java, Gender.java, AccountStatus.java
│   ├── Appointment.java, AppointmentStatus.java
│   ├── Prescription.java, PrescriptionStatus.java
│   ├── MedicalRecord.java, LabResult.java, LabResultStatus.java
│   ├── DoctorProfile.java, DoctorApprovalStatus.java
│   ├── DoctorCertificate.java, DoctorReview.java, DoctorSchedule.java
│   ├── Notification.java, Message.java, VideoCall.java, VideoCallStatus.java
│   ├── EmergencyRequest.java, EmergencyLevel.java, EmergencyStatus.java
│   ├── Address.java, EmergencyContact.java, InsuranceInfo.java
│   ├── AuditLog.java, EmailVerificationToken.java, PasswordResetToken.java
├── repository/                          # 19 repository — JPA veri erişim
├── service/                             # 13 service — İş mantığı
├── controller/                          # 13 controller — REST API endpoint'leri
├── dto/                                 # 31 DTO — Veri transfer nesneleri
└── exception/                           # 7 exception — Hata yönetimi

src/main/resources/
├── application.properties               # Uygulama yapılandırması
└── static/                              # Frontend (SPA)
    ├── index.html                       # Ana sayfa
    ├── css/style.css                    # Stil dosyası
    └── js/
        ├── api.js                       # API servis katmanı
        ├── auth.js                      # Kimlik doğrulama modülü
        ├── pages.js                     # Sayfa render modülü
        └── app.js                       # Ana uygulama yöneticisi
```

## 🔑 API Endpoint'leri (40+)

### Auth (Kimlik Doğrulama)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/auth/register | Yeni kullanıcı kaydı | Herkese açık |
| POST | /api/auth/login | JWT ile giriş | Herkese açık |
| POST | /api/auth/forgot-password | Şifre sıfırlama talebi | Herkese açık |
| PUT | /api/auth/change-password | Şifre değiştir | JWT gerekli |

### Users (Kullanıcı Yönetimi)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/users | Tüm kullanıcıları listele | JWT gerekli |
| GET | /api/users/{id} | Kullanıcı detay | JWT gerekli |
| GET | /api/users/me | Giriş yapan kullanıcı | JWT gerekli |
| GET | /api/users/doctors | Doktor listesi | JWT gerekli |
| PUT | /api/users/{id} | Kullanıcı güncelle | JWT gerekli |
| DELETE | /api/users/{id} | Kullanıcı sil | JWT gerekli |

### Profile (Profil Yönetimi)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/profile/me | Detaylı profil bilgileri | JWT gerekli |
| PUT | /api/profile/me | Profil güncelle | JWT gerekli |
| GET/POST | /api/profile/address | Adres bilgileri | JWT gerekli |
| GET/POST/DELETE | /api/profile/emergency-contacts | Acil durum kişileri | JWT gerekli |
| GET/POST | /api/profile/insurance | Sigorta bilgileri | JWT gerekli |
| PUT | /api/profile/freeze | Hesabı dondur | JWT gerekli |
| DELETE | /api/profile/delete-account | Hesabı sil (soft delete) | JWT gerekli |

### Appointments (Randevu Yönetimi)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/appointments | Randevu oluştur (çakışma kontrolü) | JWT (PATIENT) |
| GET | /api/appointments/my | Hasta randevuları | JWT gerekli |
| GET | /api/appointments/doctor/my | Doktor randevuları | JWT gerekli |
| GET | /api/appointments/patient/{id} | Hasta randevuları (admin) | JWT gerekli |
| GET | /api/appointments/doctor/{id} | Doktor randevuları (admin) | JWT gerekli |
| PUT | /api/appointments/{id}/approve | Randevu onayla | JWT (DOCTOR) |
| PUT | /api/appointments/{id}/cancel | Randevu iptal | JWT gerekli |
| PUT | /api/appointments/{id}/complete | Randevu tamamla | JWT (DOCTOR) |
| PUT | /api/appointments/{id}/reschedule | Randevu ertele | JWT gerekli |
| GET | /api/appointments/schedule/{id} | Doktor takvimi | JWT gerekli |
| POST | /api/appointments/schedule | Takvim ayarla | JWT (DOCTOR) |
| GET | /api/appointments/available-slots/{id} | Müsait saatler | JWT gerekli |

### Prescriptions (Reçete)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/prescriptions | Reçete yaz | JWT (DOCTOR) |
| GET | /api/prescriptions/my | Reçetelerim | JWT (PATIENT) |
| GET | /api/prescriptions/doctor/my | Yazdığım reçeteler | JWT (DOCTOR) |
| PUT | /api/prescriptions/{id}/status | Reçete durumu güncelle | JWT gerekli |

### Medical Records & Lab Results (Tıbbi Kayıt)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/medical-records/my | Tıbbi kaydım | JWT (PATIENT) |
| POST | /api/medical-records/my | Tıbbi kayıt oluştur/güncelle | JWT (PATIENT) |
| GET | /api/medical-records/patient/{id} | Hasta tıbbi kaydı | JWT (DOCTOR) |
| POST | /api/medical-records/lab-results/{id} | Lab sonucu ekle | JWT (DOCTOR) |
| GET | /api/medical-records/lab-results/my | Lab sonuçlarım | JWT (PATIENT) |
| GET | /api/medical-records/lab-results/patient/{id} | Hasta lab sonuçları | JWT (DOCTOR) |

### Doctor Profiles (Doktor Profili)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/doctor-profiles/me | Doktor profilim | JWT (DOCTOR) |
| POST | /api/doctor-profiles/me | Profil oluştur/güncelle | JWT (DOCTOR) |
| GET | /api/doctor-profiles/search | Onaylı doktorları ara | Herkese açık |
| GET | /api/doctor-profiles/{id} | Doktor detayı | Herkese açık |
| GET | /api/doctor-profiles/admin/pending | Bekleyen başvurular | JWT (ADMIN) |
| PUT | /api/doctor-profiles/admin/{id}/approve | Doktoru onayla | JWT (ADMIN) |
| PUT | /api/doctor-profiles/admin/{id}/reject | Doktoru reddet | JWT (ADMIN) |
| POST | /api/doctor-profiles/certificates | Sertifika ekle | JWT (DOCTOR) |
| DELETE | /api/doctor-profiles/certificates/{id} | Sertifika sil | JWT (DOCTOR) |
| POST | /api/doctor-profiles/reviews | Yorum yaz | JWT (PATIENT) |
| GET | /api/doctor-profiles/{id}/reviews | Yorumları getir | Herkese açık |

### Video Calls (Video Görüşme)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/video-calls | Görüşme odası oluştur | JWT (DOCTOR) |
| POST | /api/video-calls/join/{roomId} | Görüşmeye katıl | JWT gerekli |
| POST | /api/video-calls/end/{roomId} | Görüşmeyi sonlandır | JWT gerekli |
| PUT | /api/video-calls/{id}/notes | Görüşme notu kaydet | JWT (DOCTOR) |
| GET | /api/video-calls/my | Tüm görüşmelerim | JWT gerekli |
| GET | /api/video-calls/active | Aktif görüşmelerim | JWT gerekli |
| GET | /api/video-calls/room/{roomId} | Oda bilgileri | JWT gerekli |

### Emergency (Acil Durum)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| POST | /api/emergency | SOS talebi oluştur | JWT (PATIENT) |
| PUT | /api/emergency/{id}/respond | Talebi üstlen | JWT (DOCTOR) |
| PUT | /api/emergency/{id}/resolve | Acil durumu çöz | JWT (DOCTOR) |
| PUT | /api/emergency/{id}/cancel | Talebi iptal et | JWT (PATIENT) |
| GET | /api/emergency/my | Benim acil taleplerim | JWT gerekli |
| GET | /api/emergency/pending | Bekleyen talepler | JWT gerekli |
| GET | /api/emergency/all | Tüm talepler | JWT (ADMIN) |
| GET | /api/emergency/assigned | Üstlendiğim talepler | JWT (DOCTOR) |

### Notifications & Messages
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/notifications | Son 20 bildirim | JWT gerekli |
| GET | /api/notifications/unread-count | Okunmamış sayısı | JWT gerekli |
| PUT | /api/notifications/{id}/read | Okundu işaretle | JWT gerekli |
| PUT | /api/notifications/read-all | Tümünü okundu yap | JWT gerekli |
| POST | /api/messages | Mesaj gönder | JWT gerekli |
| GET | /api/messages/conversations | Konuşmalarım | JWT gerekli |
| GET | /api/messages/conversation/{id} | Konuşma geçmişi | JWT gerekli |

### Admin Panel
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/admin/report | Sistem raporu | JWT (ADMIN) |
| GET | /api/admin/users | Tüm kullanıcılar | JWT (ADMIN) |
| GET | /api/admin/users/role/{role} | Rol bazlı listeleme | JWT (ADMIN) |
| PUT | /api/admin/users/{id}/status | Kullanıcı durumu değiştir | JWT (ADMIN) |
| PUT | /api/admin/users/{id}/role | Kullanıcı rolü değiştir | JWT (ADMIN) |
| DELETE | /api/admin/users/{id} | Kullanıcı sil | JWT (ADMIN) |
| GET | /api/admin/audit-logs | İşlem logları | JWT (ADMIN) |
| POST | /api/admin/broadcast | Toplu bildirim gönder | JWT (ADMIN) |

### Reports (Raporlama)
| Metod | Endpoint | Açıklama | Yetki |
|-------|----------|----------|-------|
| GET | /api/reports/dashboard | Sistem istatistikleri | JWT gerekli |

## 🔒 Güvenlik

- **BCrypt** ile şifre hash'leme
- **JWT** token tabanlı kimlik doğrulama (24 saat geçerli)
- **@PreAuthorize** ile rol bazlı erişim kontrolü (PATIENT, DOCTOR, ADMIN)
- **CORS** yapılandırması (localhost:4200, :3000, :8080 izinli)
- **Environment variable** ile hassas bilgi yönetimi (DB_PASSWORD, JWT_SECRET)
- **Brute-force koruması** — 5 başarısız giriş sonrası 15 dakika hesap kilitleme
- **Audit logging** — Tüm kritik işlemler kayıt altına alınıyor (KVKK uyumu)
- **GlobalExceptionHandler** ile merkezi hata yönetimi

## 🗄️ Veritabanı Şeması

```
users (29 alan)
  ├──1:N── appointments ──1:1── prescriptions
  ├──1:1── medical_records
  ├──1:N── lab_results
  ├──1:1── doctor_profiles ──1:N── doctor_certificates
  │                         ──1:N── doctor_reviews
  ├──1:N── doctor_schedules
  ├──1:N── notifications
  ├──1:N── messages (sender/receiver)
  ├──1:N── video_calls (doctor/patient)
  ├──1:N── emergency_requests (patient/assigned_doctor)
  ├──1:1── addresses
  ├──1:N── emergency_contacts
  ├──1:1── insurance_info
  └──1:N── audit_logs
```

## 🌐 Frontend (SPA)

Uygulama, Spring Boot static resources üzerinde sunulan tek sayfalık bir web uygulamasıdır (SPA).

**Özellikler:**
- 🌙 Karanlık/Aydınlık tema desteği
- 📱 Responsive tasarım (mobil uyumlu)
- 🔔 Gerçek zamanlı bildirim sistemi
- 💬 Hasta-Doktor mesajlaşma
- 📹 Jitsi Meet video görüşme entegrasyonu
- 🚨 SOS acil durum butonu (GPS konum paylaşımı)
- ⭐ Doktor puanlama ve yorum sistemi
- 📊 Admin dashboard ve raporlama

## 👥 Ekip

| İsim | Rol |
|------|-----|
| Halid Hacbekkur | Scrum Master & Proje Yönetimi |
| Cena İsmail | Frontend Geliştirme / Mobil Analiz |
| Zelal Ergin | Backend Geliştirme / Altyapı |
| Nedim İsa | Gereksinim Toplama ve Belgeleme |
| Ahmet Akif Yılmaz | Veritabanı Tasarımı & Güvenlik |

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

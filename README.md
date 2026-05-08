# 🏥 HealthTech — Tele-Sağlık Platformu

Tele-Sağlık Platformu, hastaların doktorlarla çevrimiçi randevu alabildiği, tıbbi kayıtlarını görüntüleyebildiği ve reçetelerini takip edebildiği bir sağlık yönetim sistemidir.

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

### 5. API Dokümantasyonuna Erişin
```
http://localhost:8080/swagger-ui.html
```

## 🔑 API Endpoint'leri

### Auth (Kimlik Doğrulama)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| POST | /api/auth/register | Yeni kullanıcı kaydı |
| POST | /api/auth/login | JWT ile giriş |

### Users (Kullanıcı Yönetimi)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| GET | /api/users | Tüm kullanıcıları listele |
| GET | /api/users/{id} | Kullanıcı detay |
| GET | /api/users/me | Giriş yapan kullanıcı |
| GET | /api/users/doctors | Doktor listesi |
| PUT | /api/users/{id} | Kullanıcı güncelle |
| DELETE | /api/users/{id} | Kullanıcı sil |

### Appointments (Randevu Yönetimi)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| POST | /api/appointments | Randevu oluştur |
| GET | /api/appointments/my | Hasta randevuları |
| GET | /api/appointments/doctor | Doktor randevuları |
| PUT | /api/appointments/{id}/approve | Randevu onayla |
| PUT | /api/appointments/{id}/cancel | Randevu iptal |
| PUT | /api/appointments/{id}/complete | Randevu tamamla |

### Prescriptions (Reçete)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| POST | /api/prescriptions | Reçete yaz (DOCTOR) |
| GET | /api/prescriptions/my | Reçetelerim (PATIENT) |

### Medical Records (Tıbbi Kayıt)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| POST | /api/medical-records | Tıbbi kayıt oluştur |
| GET | /api/medical-records/my | Tıbbi kaydım |
| GET | /api/medical-records/patient/{id} | Hasta tıbbi kaydı (DOCTOR) |

### Reports (Raporlama)
| Metod | Endpoint | Açıklama |
|-------|----------|----------|
| GET | /api/reports/dashboard | Sistem istatistikleri |

## 🔒 Güvenlik

- **BCrypt** ile şifre hash'leme
- **JWT** token tabanlı kimlik doğrulama (24 saat geçerli)
- **@PreAuthorize** ile rol bazlı erişim kontrolü (PATIENT, DOCTOR, ADMIN)
- **CORS** yapılandırması (localhost:4200 izinli)
- Environment variable ile hassas bilgi yönetimi

## 🗄️ Veritabanı Şeması

```
users (id, full_name, email, password, phone, role, created_at)
  │
  ├──1:N── appointments (id, patient_id, doctor_id, date_time, status, created_at)
  │                │
  │                └──1:1── prescriptions (id, appointment_id, medication, dosage, instructions)
  │
  └──1:1── medical_records (id, patient_id, blood_type, allergies, chronic_diseases, height, weight, notes)
```

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

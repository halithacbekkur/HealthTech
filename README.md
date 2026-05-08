# 🏥 HealthTech – Tele-Sağlık Platformu

Tele-Sağlık Platformu, hasta ve doktor arasındaki sağlık hizmetlerini dijital ortama taşıyan bir web uygulamasıdır. Uzaktan randevu alma, video görüşme, tıbbi kayıt yönetimi ve e-reçete özelliklerini sunar.

---

## 🚀 Teknoloji Yığını

| Katman | Teknoloji |
|--------|-----------|
| **Backend** | Java 17, Spring Boot 4.0.6 |
| **Veritabanı** | MySQL 8.x |
| **ORM** | Spring Data JPA + Hibernate |
| **Güvenlik** | Spring Security + JWT (jjwt 0.12.6) |
| **API Dokümantasyonu** | springdoc-openapi 2.8.8 (Swagger UI) |
| **Build** | Maven |
| **Frontend** | Angular (planlanan) |
| **Video** | Jitsi Meet / WebRTC (planlanan) |

---

## 📁 Proje Yapısı

```
telehealth/
├── src/main/java/com/healthtech/telehealth/
│   ├── controller/          # REST API Controller'ları
│   │   ├── AuthController        # Giriş ve kayıt
│   │   ├── UserController        # Kullanıcı CRUD
│   │   ├── AppointmentController # Randevu yönetimi
│   │   ├── MedicalRecordController # Tıbbi kayıtlar
│   │   └── PrescriptionController  # Reçete yönetimi
│   ├── service/             # İş mantığı katmanı
│   ├── repository/          # Veritabanı erişim katmanı
│   ├── entity/              # JPA Entity modelleri
│   ├── dto/                 # Data Transfer Objects
│   ├── exception/           # Özel exception sınıfları
│   ├── security/            # JWT filtre ve güvenlik yapılandırması
│   └── config/              # Swagger ve uygulama yapılandırması
├── src/main/resources/
│   └── application.properties  # Veritabanı ve uygulama ayarları
├── docs/                    # Proje dokümantasyonu
│   ├── Gorev01_Mobil_Uygulama_Gereksinim_Analizi.md
│   ├── Gorev02_API_Dokumantasyonu.md
│   ├── Gorev03_Veritabani_Sema_Tasarimi.md
│   ├── Gorev04_Paydaslar_ve_Iletisim_Plani.md
│   └── Gorev05_Video_Konferans_Arastirmasi.md
├── Projeakisi.md            # Haftalık ilerleme dokümanı
├── pom.xml                  # Maven bağımlılıkları
└── README.md                # Bu dosya
```

---

## ⚙️ Kurulum ve Çalıştırma

### Gereksinimler
- Java 17+
- Maven 3.8+
- MySQL 8.x

### 1. Veritabanı Oluşturma
```sql
CREATE DATABASE telehealth_db;
```

### 2. `application.properties` Ayarları
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/telehealth_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### 3. Projeyi Çalıştırma
```bash
cd telehealth
mvn spring-boot:run
```

### 4. API'leri Test Etme
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **API Docs:** http://localhost:8080/v3/api-docs

---

## 📡 API Endpoint'leri

### 🔐 Authentication
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/auth/register` | Yeni kullanıcı kaydı |
| POST | `/api/auth/login` | Giriş (JWT token) |

### 👥 Users
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/users` | Tüm kullanıcılar |
| GET | `/api/users/{id}` | ID ile kullanıcı |
| GET | `/api/users/me` | Kendi profilim |
| PUT | `/api/users/{id}` | Güncelle |
| DELETE | `/api/users/{id}` | Sil |

### 📅 Appointments
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/appointments` | Randevu oluştur |
| GET | `/api/appointments/my` | Hasta randevuları |
| GET | `/api/appointments/doctor` | Doktor randevuları |
| PUT | `/api/appointments/{id}/approve` | Onayla (DOCTOR) |
| PUT | `/api/appointments/{id}/cancel` | İptal et |
| PUT | `/api/appointments/{id}/complete` | Tamamla (DOCTOR) |

### 🏥 Medical Records
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/medical-records/my` | Kendi kaydım (PATIENT) |
| POST | `/api/medical-records/my` | Kayıt oluştur/güncelle (PATIENT) |
| GET | `/api/medical-records/patient/{id}` | Hasta kaydı (DOCTOR) |

### 💊 Prescriptions
| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/prescriptions` | Reçete yaz (DOCTOR) |
| GET | `/api/prescriptions/my` | Reçetelerim (PATIENT) |

---

## 🔐 Kimlik Doğrulama

Sistem JWT (JSON Web Token) tabanlı kimlik doğrulama kullanır.

```
Authorization: Bearer <jwt_token>
```

**Roller:** `PATIENT`, `DOCTOR`, `ADMIN`

---

## 🔄 Randevu Durum Akışı

```
PENDING → APPROVED → COMPLETED
   ↓         ↓
CANCELLED  CANCELLED
```

- Sadece **PENDING** → APPROVED (doktor onaylar)
- Sadece **APPROVED** → COMPLETED (görüşme sonrası)
- **PENDING** veya **APPROVED** → CANCELLED (iptal)
- Tamamlanmış randevu iptal edilemez

---

## 👥 Ekip

| İsim | Rol |
|------|-----|
| Halid Hacbekkur | Scrum Master |
| Cena İsmail | Frontend Geliştirme |
| Zelal Ergin | Backend Geliştirme |
| Nedim İsa | Gereksinim Analizi |
| Ahmet Akif Yılmaz | Veritabanı & Güvenlik |

---

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

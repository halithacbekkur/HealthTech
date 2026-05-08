# 📡 API Dokümantasyonu – Tele-Sağlık Platformu

**Proje:** Tele-Sağlık / HealthTech Platformu  
**Görev:** Hafta 1 – Görev 2  
**Sorumlu:** ZELAL ERGİN  
**Tarih:** 2026-05-08  
**Versiyon:** 1.0  
**Base URL:** `http://localhost:8080`  
**Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

---

## 1. Proje Altyapısı Özeti

| Bileşen | Teknoloji | Versiyon |
|---------|-----------|---------|
| Framework | Spring Boot | 4.0.6 |
| Dil | Java | 17 |
| Veritabanı | MySQL | 8.x |
| ORM | Spring Data JPA + Hibernate | — |
| Güvenlik | Spring Security + JWT | jjwt 0.12.6 |
| Validasyon | Spring Boot Starter Validation | — |
| API Dokümantasyonu | springdoc-openapi (Swagger) | 2.8.8 |
| Build Aracı | Maven | — |

---

## 2. Kimlik Doğrulama (Authentication)

Sistem JWT (JSON Web Token) tabanlı kimlik doğrulama kullanır. Korunan endpoint'lere erişmek için `Authorization` header'ında token gönderilmelidir.

**Header formatı:**
```
Authorization: Bearer <jwt_token>
```

---

## 3. API Endpoint'leri

### 3.1 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Açıklama | Yetki |
|--------|----------|----------|-------|
| POST | `/api/auth/register` | Yeni kullanıcı kaydı | Herkese açık |
| POST | `/api/auth/login` | Kullanıcı girişi (JWT token alır) | Herkese açık |

**Register – İstek Gövdesi:**
```json
{
    "fullName": "Ali Yılmaz",
    "email": "ali@example.com",
    "password": "Sifre123",
    "phone": "05551234567",
    "role": "PATIENT"
}
```

**Login – İstek Gövdesi:**
```json
{
    "email": "ali@example.com",
    "password": "Sifre123"
}
```

**Login – Yanıt:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Roller:** `PATIENT`, `DOCTOR`, `ADMIN`

---

### 3.2 👥 User Management (`/api/users`)

| Method | Endpoint | Açıklama | Yetki |
|--------|----------|----------|-------|
| GET | `/api/users` | Tüm kullanıcıları listele | Giriş yapmış kullanıcı |
| GET | `/api/users/{id}` | ID ile kullanıcı getir | Giriş yapmış kullanıcı |
| GET | `/api/users/me` | Kendi profil bilgilerimi getir | Giriş yapmış kullanıcı |
| POST | `/api/users` | Yeni kullanıcı oluştur | Giriş yapmış kullanıcı |
| PUT | `/api/users/{id}` | Kullanıcı bilgilerini güncelle | Giriş yapmış kullanıcı |
| DELETE | `/api/users/{id}` | Kullanıcı sil | Giriş yapmış kullanıcı |

**Yanıt (UserResponseDTO):**
```json
{
    "id": 1,
    "fullName": "Ali Yılmaz",
    "email": "ali@example.com",
    "phone": "05551234567",
    "role": "PATIENT"
}
```

---

### 3.3 📅 Appointment Management (`/api/appointments`)

| Method | Endpoint | Açıklama | Yetki |
|--------|----------|----------|-------|
| POST | `/api/appointments` | Yeni randevu oluştur | Giriş yapmış hasta |
| GET | `/api/appointments/my?patientId={id}` | Hastanın randevularını listele | Giriş yapmış kullanıcı |
| GET | `/api/appointments/doctor?doctorId={id}` | Doktorun randevularını listele | Giriş yapmış kullanıcı |
| PUT | `/api/appointments/{id}/cancel` | Randevuyu iptal et | Giriş yapmış kullanıcı |
| PUT | `/api/appointments/{id}/approve` | Randevuyu onayla | Sadece DOCTOR |

**Randevu Oluşturma – İstek:**
```json
{
    "doctorId": 2,
    "appointmentDate": "2026-05-15T10:00:00"
}
```

**Yanıt (AppointmentResponseDTO):**
```json
{
    "id": 1,
    "patientName": "Ali Yılmaz",
    "doctorName": "Dr. Ayşe Demir",
    "appointmentDate": "2026-05-15T10:00:00",
    "status": "PENDING",
    "createdAt": "2026-05-08T09:00:00"
}
```

**Randevu Durumları:** `PENDING` → `APPROVED` → `COMPLETED` veya `CANCELLED`

---

### 3.4 🏥 Medical Records (`/api/medical-records`)

| Method | Endpoint | Açıklama | Yetki |
|--------|----------|----------|-------|
| GET | `/api/medical-records/my` | Kendi tıbbi kaydımı görüntüle | Sadece PATIENT |
| POST | `/api/medical-records/my` | Tıbbi kaydımı oluştur/güncelle | Sadece PATIENT |
| GET | `/api/medical-records/patient/{patientId}` | Hasta kaydını görüntüle | Sadece DOCTOR |

**Tıbbi Kayıt – İstek:**
```json
{
    "bloodGroup": "A+",
    "allergies": "Penisilin, Polen",
    "pastDiseases": "Astım",
    "height": 175.0,
    "weight": 72.5
}
```

**Yanıt (MedicalRecordResponseDTO):**
```json
{
    "id": 1,
    "patientName": "Ali Yılmaz",
    "bloodGroup": "A+",
    "allergies": "Penisilin, Polen",
    "pastDiseases": "Astım",
    "height": 175.0,
    "weight": 72.5,
    "updatedAt": "2026-05-08T09:30:00"
}
```

---

### 3.5 💊 Prescription Management (`/api/prescriptions`)

| Method | Endpoint | Açıklama | Yetki |
|--------|----------|----------|-------|
| POST | `/api/prescriptions` | Reçete oluştur | Sadece DOCTOR |
| GET | `/api/prescriptions/my` | Reçetelerimi görüntüle | Sadece PATIENT |

**Reçete Oluşturma – İstek:**
```json
{
    "appointmentId": 1,
    "medicines": "Parol 500mg, Augmentin 1000mg",
    "instructions": "Parol: Günde 3 defa, tok karnına. Augmentin: Günde 2 defa, 7 gün."
}
```

**Yanıt (PrescriptionResponseDTO):**
```json
{
    "id": 1,
    "appointmentId": 1,
    "doctorName": "Dr. Ayşe Demir",
    "patientName": "Ali Yılmaz",
    "medicines": "Parol 500mg, Augmentin 1000mg",
    "instructions": "Parol: Günde 3 defa, tok karnına. Augmentin: Günde 2 defa, 7 gün.",
    "createdAt": "2026-05-08T10:00:00"
}
```

---

## 4. Güvenlik Yapılandırması

| Endpoint | Erişim |
|----------|--------|
| `/api/auth/**` | Herkese açık (permitAll) |
| `/swagger-ui/**` | Herkese açık (permitAll) |
| `/v3/api-docs/**` | Herkese açık (permitAll) |
| `/api/appointments/*/approve` | Sadece DOCTOR rolü |
| `/api/medical-records/my` | Sadece PATIENT rolü |
| `/api/medical-records/patient/*` | Sadece DOCTOR rolü |
| `/api/prescriptions` (POST) | Sadece DOCTOR rolü |
| `/api/prescriptions/my` | Sadece PATIENT rolü |
| Diğer tüm endpoint'ler | Giriş yapmış kullanıcı (authenticated) |

---

## 5. Hata Yanıtları

| HTTP Kodu | Açıklama |
|-----------|----------|
| 200 | Başarılı |
| 400 | Geçersiz istek (validasyon hatası) |
| 401 | Kimlik doğrulama başarısız (token yok veya geçersiz) |
| 403 | Yetkisiz erişim (rol yetersiz) |
| 404 | Kaynak bulunamadı |
| 500 | Sunucu hatası |

**Hata Yanıt Formatı:**
```json
{
    "timestamp": "2026-05-08T09:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Kullanıcı bulunamadı: ID 99"
}
```

---

## 6. Veritabanı Bağlantısı

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/telehealth_db
spring.datasource.username=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 7. Swagger UI Kullanımı

1. Uygulamayı çalıştırın: `mvn spring-boot:run`
2. Tarayıcıda açın: `http://localhost:8080/swagger-ui/index.html`
3. Sağ üstteki **"Authorize"** butonuna tıklayın
4. Login endpoint'inden aldığınız JWT token'ı yapıştırın
5. Tüm endpoint'leri interaktif olarak test edin

---

*Hazırlayan: ZELAL ERGİN | Onaylayan: Halit Hacbekkur (Scrum Master)*

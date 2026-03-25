# 🏥 HealthTech — Tele-Sağlık Platformu

> Sağlık hizmetlerine erişimi kolaylaştıran, hasta-doktor iletişimini dijital ortama taşıyan uzaktan sağlık platformu.

---

## 📋 Proje Özeti

| Alan | Bilgi |
|------|-------|
| **Sprint** | 1. Hafta — 9–15 Mart 2025 |
| **Metodoloji** | Scrum |
| **Backend** | Java & Spring Boot |
| **Frontend** | Angular (Responsive) |
| **Veritabanı** | MySQL |
| **Kimlik Doğrulama** | JWT + 2FA (opsiyonel) |
| **Video Altyapısı** | WebRTC |
| **Güvenlik** | TLS 1.3, RBAC, KVKK/GDPR |

---

## 👥 Ekip

| İsim | Rol |
|------|-----|
| Halid Hacbekkur | Scrum Master / Proje Yönetimi |
| Nedim İsa | Gereksinim Toplama ve Belgeleme |
| Ömer Doğan | Sistem Kalitesi ve Teknoloji Analizi |
| Zelal Ergin | Geliştirme Ortamı Kurulumu |
| Ahmet Akif Yılmaz | Veri Modeli Tasarımı |

---

## 📅 1. Hafta — 9–15 Mart

### Halid Hacbekkur — Scrum Master / Proje Yönetimi

- GitHub repository oluşturuldu ve ekip erişimleri sağlandı
- `main` branch temel yapılandırmaları ve proje başlangıç ayarları gerçekleştirildi
- Proje sürecini takip etmek amacıyla `proje-akisi.md` dokümanı oluşturuldu
- Takım üyelerine Git & GitHub iş akışı hakkında temel eğitim verildi
- Sprint planlaması ve görev dağılımı organize edildi

---

### Nedim İsa — Gereksinim Toplama ve Belgeleme

Tele-Sağlık Platformu için fonksiyonel ve teknik gereksinimler analiz edilerek dokümante edilmiştir.

#### Fonksiyonel Gereksinimler

**Hasta Tarafı**
- Sisteme kayıt olabilme ve güvenli giriş yapabilme
- Profil ve sağlık bilgilerinin görüntülenmesi ve güncellenmesi
- Uzmanlık alanına göre doktor arama ve listeleme
- Online randevu oluşturma, görüntüleme, iptal etme ve yeniden planlama
- Randevu saatinde video görüşme başlatabilme
- Tıbbi kayıt geçmişini görüntüleyebilme

**Doktor Tarafı**
- Sisteme giriş yapabilme ve profil yönetimi
- Randevu taleplerini görüntüleme ve onaylama
- Video görüşme oturumu başlatabilme
- Hastaya ait tıbbi kayıtları görüntüleme ve yeni kayıt oluşturma
- Çalışma saatleri ve uygunluk takvimi yönetimi

**Yönetici (Admin) Tarafı**
- Kullanıcı yönetimi (hasta ve doktor)
- Rol atama ve doktor doğrulama işlemleri
- Sistem genelinde randevu ve kayıt verilerini izleme
- Sistem güvenliği ve veri bütünlüğü kontrolleri

#### Teknik Gereksinimler

| Katman | Teknoloji |
|--------|-----------|
| Backend | Java & Spring Boot |
| Frontend | Angular (Responsive) |
| Veritabanı | MySQL |
| Kimlik Doğrulama | JWT Authentication |
| Güvenli Transfer | TLS / HTTPS |
| Video Görüşme | WebRTC |

#### Kullanıcı Hikayeleri

> *Bir hasta olarak hızlı şekilde uygun doktordan randevu almak istiyorum.*
>
> *Bir doktor olarak randevu taleplerimi sistem üzerinden yönetmek istiyorum.*
>
> *Bir kullanıcı olarak uzaktan video görüşme ile sağlık danışmanlığı almak istiyorum.*
>
> *Bir yönetici olarak platformun güvenli ve düzenli çalışmasını sağlamak istiyorum.*

#### Örnek Kullanım Senaryosu

```
1. Hasta sisteme giriş yapar
2. Doktor araması gerçekleştirir
3. Uygun randevu saatini seçer
4. Randevu oluşturulur
5. Görüşme saatinde video oturumu başlatılır
6. Doktor tıbbi kayıt ekler
7. Hasta kayıtları panelden görüntüler
```

---

### Ömer Doğan — Sistem Kalitesi ve Teknoloji Analizi

#### 🔐 Güvenlik

| Konu | Hedef |
|------|-------|
| Veri gizliliği | KVKK / GDPR uyumu |
| Veri transferi | TLS 1.3 şifreleme protokolü |
| Kimlik doğrulama | JWT + isteğe bağlı 2FA |
| Yetkilendirme | RBAC modeli ile rol bazlı erişim |

#### 🚀 Performans ve Ölçeklenebilirlik

| Metrik | Hedef |
|--------|-------|
| Kritik işlem yanıt süresi | < 2 saniye |
| Eşzamanlı video oturumu | En az 100 |
| Veritabanı optimizasyonu | İndeksleme ve sorgu optimizasyonu |

#### 📱 Kullanılabilirlik

- Angular arayüzünün tüm cihazlarda **responsive** çalışması hedeflenmiştir
- **WCAG standartlarına uygun** erişilebilirlik planlanmıştır

#### ☁️ Güvenilirlik

- **%99.9 uptime** hedefi belirlenmiştir
- Spring Boot tarafında **Global Exception Handling** uygulanacaktır

---

### Zelal Ergin — Geliştirme Ortamı Kurulumu

- IntelliJ IDEA ve JDK kurulumu tamamlandı
- Git sürüm kontrol sistemi yapılandırıldı
- Repository klonlanarak yerel çalışma ortamı hazırlandı
- Backend ve frontend klasör mimarisi oluşturuldu
- İlk yapılandırmalar `main` branch'e başarıyla gönderildi

---

### Ahmet Akif Yılmaz — Veri Modeli Tasarımı

Kullanıcı, hasta, doktor ve randevu yönetimini kapsayan veritabanı modeli tasarlanmıştır.

#### Tablolar

**Users**

| Alan | Tip | Not |
|------|-----|-----|
| `id` | BIGINT | PK |
| `full_name` | VARCHAR | |
| `email` | VARCHAR | Unique |
| `password` | VARCHAR | Hashed |
| `phone` | VARCHAR | |
| `role` | ENUM | PATIENT / DOCTOR / ADMIN |
| `created_at` | DATETIME | |

**Patients**

| Alan | Tip | Not |
|------|-----|-----|
| `id` | BIGINT | PK |
| `user_id` | BIGINT | FK → Users |
| `birth_date` | DATE | |
| `gender` | VARCHAR | |
| `address` | VARCHAR | |
| `blood_type` | VARCHAR | |

**Doctors**

| Alan | Tip | Not |
|------|-----|-----|
| `id` | BIGINT | PK |
| `user_id` | BIGINT | FK → Users |
| `specialty_id` | BIGINT | FK → Specialties |
| `license_number` | VARCHAR | Unique |
| `experience_years` | INT | |

**Appointments**

| Alan | Tip | Not |
|------|-----|-----|
| `id` | BIGINT | PK |
| `patient_id` | BIGINT | FK → Patients |
| `doctor_id` | BIGINT | FK → Doctors |
| `appointment_date` | DATETIME | |
| `status` | ENUM | PENDING / CONFIRMED / CANCELLED |
| `notes` | TEXT | Nullable |

#### İlişkiler

```
Users ──────────── Patients       (1 - 1)
Users ──────────── Doctors        (1 - 1)
Patients ────────< Appointments   (1 - N)
Doctors  ────────< Appointments   (1 - N)
```

#### ER Diyagramı

![ER Diyagramı](er_diagram.jpg)

---

## 🗂️ Sonraki Adımlar

- [ ] Backend proje iskeletinin oluşturulması (Spring Boot)
- [ ] Angular proje başlangıcı ve routing yapısı
- [ ] MySQL şemasının oluşturulması ve migration
- [ ] JWT kimlik doğrulama entegrasyonu
- [ ] WebRTC altyapısının araştırılması

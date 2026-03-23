# 🏥 HealthTech – Proje Akışı & Haftalık İlerleme

Bu doküman, **HealthTech Tele‑Sağlık Platformu** projesinin haftalık ilerlemesini, takım üyelerinin görevlerini ve teknik gereksinimleri profesyonel ve takip edilebilir bir şekilde kayıt altına almak amacıyla hazırlanmıştır.

---

## 📅 1. Hafta (9–15 Mart)

### 👨‍💼 Halid Hacbekkur — *Scrum Master & Proje Yöneticisi*

* Proje için GitHub repository oluşturuldu.
* `main` branch için temel yapılandırmalar gerçekleştirildi.
* Proje iş akışını takip etmek amacıyla **projeakisi.md** dokümanı oluşturuldu.
* Takım üyelerine **Git & GitHub iş akışı** hakkında temel bilgilendirme yapıldı.

---

### 📝 Nedim İsa — *Gereksinim Toplama & Belgeleme*

⚠️ Bu hafta görev henüz tamamlanmamıştır.

---

### 🔐 Güvenlik Gereksinimleri (Security)

Sağlık verileri kapsamında **KVKK ve GDPR uyumluluğu** sistemin en kritik önceliğidir.

* **Veri Gizliliği:**
  Tüm veri transferleri **TLS 1.3** protokolü ile uçtan uca şifrelenmelidir.

* **Kimlik Doğrulama:**
  Sistem girişlerinde **JWT (JSON Web Token)** kullanılmalı ve mümkün olan durumlarda **İki Faktörlü Kimlik Doğrulama (2FA)** desteklenmelidir.

* **Yetkilendirme:**
  **Role-Based Access Control (RBAC)** modeli uygulanarak doktor, hasta ve admin rollerinin erişim yetkileri net biçimde ayrılmalıdır.

---

### ⚡ Performans & Ölçeklenebilirlik (Performance & Scalability)

* **Yanıt Süresi:**
  Randevu listeleme ve tıbbi kayıt sorgulama işlemleri **2 saniyenin altında** sonuçlanmalıdır.

* **Eşzamanlı Kullanım:**
  Video görüşme modülünde sistem en az **100 eşzamanlı aktif oturumu** kesinti yaşamadan destekleyebilmelidir.

* **Veritabanı Optimizasyonu:**
  **MySQL** üzerinde sık kullanılan sorgular için doğru **indexleme stratejileri** uygulanmalıdır.

---

### 🎨 Kullanılabilirlik & Mobil Uyumluluk (Usability)

* **Responsive Tasarım:**
  **Angular** ile geliştirilecek arayüz tüm cihazlarda (**masaüstü / tablet / mobil**) %100 uyumlu çalışmalıdır.

* **Erişilebilirlik:**
  Renk kontrastı ve font boyutları **WCAG standartlarına** uygun seçilerek yaşlı veya görme zorluğu yaşayan kullanıcılar için erişilebilirlik sağlanmalıdır.

---

### 🟢 Güvenilirlik & Süreklilik (Reliability & Availability)

* **Uptime Hedefi:**
  Sistem **7/24 erişilebilir** olmalı ve yıllık **%99.9 uptime** hedefi karşılanmalıdır.

* **Hata Yönetimi:**
  **Spring Boot Global Exception Handling** mekanizması kurularak kullanıcıya teknik hata mesajları yerine **anlaşılır geri bildirimler** sunulmalıdır.

---

### ⚙️ Zelal Ergin — *Geliştirme Ortamı Kurulumu*

* IntelliJ IDEA ve **JDK 25** kurulumu tamamlandı.
* Git sürüm kontrol sistemi yapılandırıldı ve GitHub entegrasyonu sağlandı.
* Uzak depo yerel ortama klonlandı.
* Proje mimarisine uygun **backend / frontend klasör yapısı** oluşturuldu.
* İlk hafta ödevi kapsamında yapılan ayarlar başarıyla `main` branch’e gönderildi.

---

### 🧠 Ahmet Akif Yılmaz — *Veri Modeli Tasarımı*

#### 📊 Tele-Sağlık Platformu Veritabanı Modeli

##### 🔹 Users Tablosu

| Alan       | Tip          | Açıklama           |
| ---------- | ------------ | ------------------ |
| id         | BIGINT       | Primary Key        |
| full_name  | VARCHAR(100) | Kullanıcı adı      |
| email      | VARCHAR(100) | E-posta            |
| password   | VARCHAR(255) | Şifre              |
| phone      | VARCHAR(20)  | Telefon            |
| role       | ENUM         | Kullanıcı rolü     |
| created_at | DATETIME     | Oluşturulma tarihi |

##### 🔹 Patients Tablosu

| Alan       | Tip          |
| ---------- | ------------ |
| id         | BIGINT       |
| user_id    | BIGINT       |
| birth_date | DATE         |
| gender     | VARCHAR(10)  |
| address    | VARCHAR(255) |
| blood_type | VARCHAR(5)   |

##### 🔹 Doctors Tablosu

| Alan             | Tip         |
| ---------------- | ----------- |
| id               | BIGINT      |
| user_id          | BIGINT      |
| specialty_id     | BIGINT      |
| license_number   | VARCHAR(50) |
| experience_years | INT         |

##### 🔹 Appointments Tablosu

| Alan             | Tip      |
| ---------------- | -------- |
| id               | BIGINT   |
| patient_id       | BIGINT   |
| doctor_id        | BIGINT   |
| appointment_date | DATETIME |
| status           | ENUM     |
| notes            | TEXT     |

---

### 🔗 Tablolar Arası İlişkiler

* Users → Patients (**1-1**)
* Users → Doctors (**1-1**)
* Patients → Appointments (**1-N**)
* Doctors → Appointments (**1-N**)
* Patients → Medical Records (**1-N**)
* Doctors → Medical Records (**1-N**)
* Medical Records → Prescriptions (**1-N**)

---

### 🗺️ ER Diyagramı

![ER Diagram](er_diagram.jpg)

Bu diyagram, sistemdeki kullanıcı rollerini, randevu ilişkilerini ve tıbbi kayıt akışını görselleştirerek **ölçeklenebilir ve sürdürülebilir bir veritabanı mimarisi** hedeflendiğini göstermektedir.

---

## ✅ Genel Değerlendirme (Hafta 1)

* Takımdaki **5 üyenin görev dağılımı korunmuştur.**
* Nedim İsa dışındaki tüm üyeler haftalık görevlerini tamamlamıştır.
* Proje altyapısı başarıyla oluşturulmuştur.
* Sonraki sprintte gereksinim analizi ve backend geliştirme sürecine başlanacaktır.

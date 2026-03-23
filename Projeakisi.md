# "HealthTech" – Proje Akışı ve Haftalık İlerleme

Bu dosya, "HealthTech" takımının haftalık proje ilerlemesini ve üyelerin katkılarını takip etmek için oluşturulmuştur.

## 1. Hafta (9–15 Mart)

**Halid Hacbekkur (Scrum Master / Yönetici):**
- GitHub repository oluşturuldu.
- main branch için temel ayarlar yapıldı.
- Proje için projeakisi.md dosyası oluşturuldu.
- Takım üyelerine Git ve GitHub iş akışı hakkında kısa bilgilendirme yapıldı.

**Nedim İsa:**


**Ömer Doğan:**
# 1. Güvenlik (Security)

Sağlık verileri (**KVKK / GDPR uyumu**) en yüksek önceliğe sahiptir.

- **Veri Gizliliği:**  
  Tüm veriler taşıma sırasında **TLS 1.3** protokolü ile şifrelenmelidir.

- **Kimlik Doğrulama:**  
  Sisteme girişlerde **JWT (JSON Web Token)** kullanılmalı ve mümkünse **İki Faktörlü Doğrulama (2FA)** desteklenmelidir.

- **Yetkilendirme:**  
  **Role Based Access Control (RBAC)** ile doktor, hasta ve adminin erişebileceği alanlar kesin çizgilerle ayrılmalıdır.


# 2. Performans ve Ölçeklenebilirlik (Performance & Scalability)

- **Yanıt Süresi:**  
  Online randevu listeleme veya tıbbi kayıt sorgulama gibi işlemler **2 saniyenin altında** sonuçlanmalıdır.

- **Eşzamanlılık:**  
  Sistem, video konferans (sanal görüşme) sırasında aynı anda **en az 100 aktif oturumu** donma yaşanmadan destekleyebilmelidir.

- **Veritabanı Optimizasyonu:**  
  **MySQL** üzerinde sık kullanılan sorgular için uygun **indeksleme** yapılarak gecikmeler önlenmelidir.


# 3. Kullanılabilirlik ve Mobil Uyumluluk (Usability)

- **Responsive Tasarım:**  
  **Angular** tarafında geliştirilecek arayüz; masaüstü, tablet ve akıllı telefonlarla **%100 uyumlu** olmalıdır.

- **Erişilebilirlik:**  
  Renk paleti ve font boyutları, yaşlı veya görme zorluğu çeken hastaların kullanımı için uygun (**WCAG standartlarında**) seçilmelidir.


# 4. Güvenilirlik ve Erişilebilirlik (Reliability & Availability)

- **Çalışma Süresi (Uptime):**  
  Sistem **7/24 erişilebilir** olmalı ve yıllık **%99.9 erişilebilirlik** oranına sahip olmalıdır.

- **Hata Yönetimi:**  
  **Spring Boot** tarafında merkezi bir hata yakalama mekanizması (**Global Exception Handling**) kurularak, kullanıcıya teknik hata yerine **anlamlı geri bildirimler** verilmelidir.

**Zelal Ergin:**

* Gerekli yazılım geliştirme araçlarının (IntelliJ IDEA, JDK 25) kurulumu ve yapılandırması tamamlandı.
* Proje için Git sürüm kontrol sistemi kuruldu ve GitHub hesabı ile entegrasyon sağlandı.
* Uzak depo (repository) yerel bilgisayara klonlanarak çalışma ortamı hazırlandı.
* Proje mimarisine uygun olarak temel `backend` ve `frontend` klasör yapısı oluşturuldu.
* İlk hafta ödevi kapsamında geliştirme ortamı ayarları yapılarak ana depoya (main branch) başarılı bir şekilde gönderildi (push edildi).

Tele-Sağlık Platformu Veri Modeli

1. Giriş
Bu doküman, tele-sağlık platformu için tasarlanan veritabanı modelini açıklamaktadır. Sistem; hasta, doktor ve yönetici kullanıcılarını, randevu işlemlerini ve tıbbi kayıtları yönetmek amacıyla tasarlanmıştır.
________________________________________
2. Veritabanı Tabloları
Users
Alan	Veri Tipi	Açıklama
id	BIGINT	Primary Key
full_name	VARCHAR(100)	Kullanıcı adı
email	VARCHAR(100)	E-posta
password	VARCHAR(255)	Şifre
phone	VARCHAR(20)	Telefon
role	ENUM	Kullanıcı rolü
created_at	DATETIME	Oluşturulma tarihi
________________________________________
Patients
Alan	Veri Tipi
id	BIGINT
user_id	BIGINT
birth_date	DATE
gender	VARCHAR(10)
address	VARCHAR(255)
blood_type	VARCHAR(5)
________________________________________
Doctors
Alan	Veri Tipi
id	BIGINT
user_id	BIGINT
specialty_id	BIGINT
license_number	VARCHAR(50)
experience_years	INT
________________________________________
Appointments
Alan	Veri Tipi
id	BIGINT
patient_id	BIGINT
doctor_id	BIGINT
appointment_date	DATETIME
status	ENUM
notes	TEXT
________________________________________
3. Tablolar Arasındaki İlişkiler
Users → Patients 1-1
Users → Doctors 1-1
Patients → Appointments 1-N
Doctors → Appointments 1-N
Patients → Medical Records 1-N
Doctors → Medical Records 1-N
Medical Records → Prescriptions 1-N
________________________________________

4. SQL Başlangıç Şeması
CREATE TABLE users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
full_name VARCHAR(100),
email VARCHAR(100),
password VARCHAR(255),
phone VARCHAR(20),
role ENUM('PATIENT','DOCTOR','ADMIN'),
created_at DATETIME
);

CREATE TABLE patients (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
user_id BIGINT,
birth_date DATE,
gender VARCHAR(10),
address VARCHAR(255),
blood_type VARCHAR(5)
);

CREATE TABLE doctors (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
user_id BIGINT,
specialty_id BIGINT,
license_number VARCHAR(50),
experience_years INT
);

CREATE TABLE appointments (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
patient_id BIGINT,
doctor_id BIGINT,
appointment_date DATETIME,
status ENUM('PENDING','APPROVED','COMPLETED','CANCELLED'),
notes TEXT
);
________________________________________



ER Diyagram Açıklaması
Bu veri modeli, tele-sağlık platformundaki kullanıcı, hasta, doktor ve randevu ilişkilerini göstermektedir. Sistem içerisinde kullanıcılar farklı roller üstlenebilir ve hastalar ile doktorlar arasında randevu ilişkileri kurulabilir. Bu yapı sistemin düzenli ve genişletilebilir bir veritabanı mimarisi ile çalışmasını sağlamaktadır.

## ER Diagram

![ER Diyagramı](er_diyagram.jpg)

Bu ER diyagramı, tele-sağlık platformunun veritabanı yapısını göstermektedir. Sistem içerisinde kullanıcılar hasta veya doktor rolünde olabilir. Hastalar ve doktorlar arasında randevu ilişkileri kurulmaktadır. Randevular sonucunda tıbbi kayıtlar oluşturulabilir ve bu kayıtlar üzerinden reçeteler tanımlanabilir. Bu yapı sistemin düzenli ve genişletilebilir bir veritabanı mimarisi ile çalışmasını sağlamaktadır.




**Ahmet Akif Yılmaz:**


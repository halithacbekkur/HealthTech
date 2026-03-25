# 🏥 HealthTech – Proje Akışı ve Haftalık İlerleme

Bu doküman, **HealthTech takımının** haftalık proje ilerlemesini ve ekip üyelerinin katkılarını takip etmek amacıyla hazırlanmıştır.

---

## 📅 1. Hafta (9–15 Mart)

### 👤 Halid Hacbekkur *(Scrum Master / Yönetici)*

- GitHub repository oluşturuldu.  
- `main` branch için temel yapılandırmalar gerçekleştirildi.  
- Proje sürecini takip etmek amacıyla **projeakisi.md** dosyası oluşturuldu.  
- Takım üyelerine Git ve GitHub iş akışı hakkında kısa bilgilendirme yapıldı.  

---

### 👤 Nedim İsa

Tele-Sağlık Platformu projesi kapsamında sistemin fonksiyonel ve teknik gereksinimlerini belirlemek amacıyla kapsamlı bir analiz süreci yürütülmüştür. Bu süreçte platformun kullanıcı rolleri, sistem beklentileri ve kullanım senaryoları detaylı şekilde incelenmiş ve dokümante edilmiştir.

---

## 📌 Fonksiyonel Gereksinimler

###  Hasta Tarafı

- Hasta kullanıcıların sisteme kayıt olabilmesi ve giriş yapabilmesi  
- Kişisel bilgilerini ve sağlık profilini görüntüleyebilmesi ve güncelleyebilmesi  
- Doktorların uzmanlık alanına göre filtrelenerek listelenmesi  
- Uygun tarih ve saat seçerek online randevu oluşturabilmesi  
- Mevcut randevularını görüntüleyebilmesi, iptal edebilmesi veya yeniden planlayabilmesi  
- Randevu saatinde sistem üzerinden video görüşme başlatabilmesi  
- Kendi tıbbi kayıtlarına erişebilmesi ve geçmiş verilerini görüntüleyebilmesi  

---

### 👨‍⚕️ Doktor Tarafı

- Doktor kullanıcıların sisteme giriş yapabilmesi ve profilini yönetebilmesi  
- Randevu taleplerini görüntüleyebilmesi ve onaylayabilmesi  
- Planlanan randevular için video görüşme oturumu başlatabilmesi  
- Hastaya ait tıbbi bilgileri görüntüleyebilmesi ve yeni kayıt ekleyebilmesi  
- Çalışma saatlerini ve uygunluk durumunu sistem üzerinden ayarlayabilmesi  

---

### 🛠️ Yönetici (Admin) Tarafı

- Sistem kullanıcılarını (hasta ve doktor) yönetebilmesi  
- Doktor doğrulama ve rol atama işlemlerini gerçekleştirebilmesi  
- Sistem genelinde randevu ve kayıt verilerini izleyebilmesi  
- Sistem güvenliği ve veri bütünlüğünü sağlamak için gerekli kontrol mekanizmalarını yönetebilmesi  

---

## ⚙️ Teknik Gereksinimler

- Backend geliştirme sürecinde Java ve Spring Boot framework kullanılması  
- Frontend tarafında Angular tabanlı responsive arayüz geliştirilmesi  
- Verilerin güvenli şekilde saklanması için MySQL veritabanı kullanılması  
- Sistem erişimlerinde JWT tabanlı kimlik doğrulama mekanizması uygulanması  
- Tüm veri transferlerinin TLS/HTTPS protokolü üzerinden gerçekleştirilmesi  
- Video görüşme altyapısı için WebRTC veya benzeri gerçek zamanlı iletişim teknolojilerinin kullanılması  

---

## 📖 Kullanıcı Hikayeleri (User Stories)

- “Bir hasta olarak uygun bir doktordan online randevu almak istiyorum, böylece sağlık hizmetine hızlı şekilde erişebilirim.”  
- “Bir doktor olarak randevu taleplerini sistem üzerinden yönetmek istiyorum, böylece çalışma planımı düzenleyebilirim.”  
- “Bir kullanıcı olarak video görüşme yaparak uzaktan sağlık danışmanlığı almak istiyorum.”  
- “Bir yönetici olarak sistemdeki kullanıcıları kontrol etmek istiyorum, böylece platform güvenli ve düzenli çalışabilir.”  

---

## 🎯 Kullanım Senaryosu (Use Case Örneği)

1. Hasta sisteme giriş yapar  
2. Uzmanlık alanına göre doktor araması yapar  
3. Uygun randevu saatini seçer  
4. Randevu oluşturulur  
5. Randevu saatinde video görüşme başlatılır  
6. Doktor görüşme sonrası tıbbi kayıt ekler  
7. Hasta kayıtlarını panelden görüntüler  

---

### 👤 Ömer Doğan  

## 1. Güvenlik (Security)

Sağlık verilerinin korunması (**KVKK / GDPR uyumu**) en yüksek öncelik olarak belirlenmiştir.

- **Veri Gizliliği:**  
  Tüm verilerin aktarım sırasında **TLS 1.3** protokolü ile şifrelenmesi planlanmaktadır.

- **Kimlik Doğrulama:**  
  Sistem girişlerinde **JWT (JSON Web Token)** kullanılması ve mümkünse **İki Faktörlü Doğrulama (2FA)** desteği sağlanması hedeflenmektedir.

- **Yetkilendirme:**  
  **Role Based Access Control (RBAC)** yaklaşımı ile doktor, hasta ve admin erişim alanlarının net şekilde ayrılması öngörülmektedir.

---

## 2. Performans ve Ölçeklenebilirlik (Performance & Scalability)

- **Yanıt Süresi:**  
  Online randevu listeleme veya tıbbi kayıt sorgulama işlemlerinin **2 saniyenin altında** sonuçlanması hedeflenmektedir.

- **Eşzamanlılık:**  
  Video konferans sırasında sistemin aynı anda **en az 100 aktif oturumu** sorunsuz desteklemesi planlanmaktadır.

- **Veritabanı Optimizasyonu:**  
  **MySQL** üzerinde sık kullanılan sorgular için uygun **indeksleme** stratejilerinin uygulanması düşünülmektedir.

---

## 3. Kullanılabilirlik ve Mobil Uyumluluk (Usability)

- **Responsive Tasarım:**  
  **Angular** ile geliştirilecek arayüzün masaüstü, tablet ve mobil cihazlarla **%100 uyumlu** olması hedeflenmektedir.

- **Erişilebilirlik:**  
  Renk paleti ve yazı boyutlarının, yaşlı veya görme zorluğu yaşayan kullanıcılar için uygun (**WCAG standartlarında**) olması planlanmaktadır.

---

## 4. Güvenilirlik ve Erişilebilirlik (Reliability & Availability)

- **Çalışma Süresi (Uptime):**  
  Sistemin **7/24 erişilebilir** olması ve yıllık **%99.9 erişilebilirlik** oranına sahip olması hedeflenmektedir.

- **Hata Yönetimi:**  
  **Spring Boot** tarafında merkezi bir hata yakalama mekanizması (**Global Exception Handling**) kurulması ve kullanıcıya teknik hata yerine anlamlı geri bildirim verilmesi planlanmaktadır.

---

### 👤 Zelal Ergin

- Gerekli yazılım geliştirme araçlarının (**IntelliJ IDEA, JDK 25**) kurulumu ve yapılandırması tamamlandı.  
- Proje için Git sürüm kontrol sistemi kuruldu ve GitHub hesabı ile entegrasyon sağlandı.  
- Uzak depo (**repository**) yerel bilgisayara klonlanarak çalışma ortamı hazırlandı.  
- Proje mimarisine uygun olarak temel `backend` ve `frontend` klasör yapısı oluşturuldu.  
- İlk hafta ödevi kapsamında geliştirme ortamı ayarları yapılarak ana depoya (**main branch**) başarıyla gönderildi (*push edildi*).  

---

### 👤 Ahmet Akif Yılmaz  

## Tele-Sağlık Platformu Veri Modeli

### 1. Giriş

Bu doküman, tele-sağlık platformu için tasarlanan veritabanı modelini açıklamaktadır. Sistem; hasta, doktor ve yönetici kullanıcılarını, randevu işlemlerini ve tıbbi kayıtları yönetmek amacıyla tasarlanmıştır.

---

### 2. Veritabanı Tabloları

#### Users

| Alan | Veri Tipi | Açıklama |
|------|-----------|----------|
| id | BIGINT | Primary Key |
| full_name | VARCHAR(100) | Kullanıcı adı |
| email | VARCHAR(100) | E-posta |
| password | VARCHAR(255) | Şifre |
| phone | VARCHAR(20) | Telefon |
| role | ENUM | Kullanıcı rolü |
| created_at | DATETIME | Oluşturulma tarihi |

#### Patients

| Alan | Veri Tipi |
|------|-----------|
| id | BIGINT |
| user_id | BIGINT |
| birth_date | DATE |
| gender | VARCHAR(10) |
| address | VARCHAR(255) |
| blood_type | VARCHAR(5) |

#### Doctors

| Alan | Veri Tipi |
|------|-----------|
| id | BIGINT |
| user_id | BIGINT |
| specialty_id | BIGINT |
| license_number | VARCHAR(50) |
| experience_years | INT |

#### Appointments

| Alan | Veri Tipi |
|------|-----------|
| id | BIGINT |
| patient_id | BIGINT |
| doctor_id | BIGINT |
| appointment_date | DATETIME |
| status | ENUM |
| notes | TEXT |

---

### 3. Tablolar Arasındaki İlişkiler

- Users → Patients (1-1)  
- Users → Doctors (1-1)  
- Patients → Appointments (1-N)  
- Doctors → Appointments (1-N)  
- Patients → Medical Records (1-N)  
- Doctors → Medical Records (1-N)  
- Medical Records → Prescriptions (1-N)

---

### ER Diyagram Açıklaması

Bu veri modeli, tele-sağlık platformundaki kullanıcı, hasta, doktor ve randevu ilişkilerini göstermektedir. Sistem içerisinde kullanıcılar farklı roller üstlenebilir ve hastalar ile doktorlar arasında randevu ilişkileri kurulabilir. Bu yapı sistemin düzenli ve genişletilebilir bir veritabanı mimarisi ile çalışmasını sağlamaktadır.

## ER Diagram

![ER Diyagramı](er_diagram.jpg)

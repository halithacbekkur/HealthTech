# 🏥 HealthTech – Proje Akışı ve Haftalık İlerleme

Bu doküman, **HealthTech (Tele-Sağlık Platformu)** projesinin haftalık ilerleme durumunu ve ekip üyelerinin projeye olan katkılarını takip etmek amacıyla hazırlanmıştır.

Proje; sağlık hizmetlerine erişimi kolaylaştırmak, hasta ve doktor arasındaki iletişimi dijital ortama taşımak ve uzaktan sağlık hizmetlerini daha verimli hale getirmek amacıyla geliştirilmektedir.

---

## 📅 1. Hafta (9–15 Mart)

---

## 👤 Halid Hacbekkur *(Scrum Master / Proje Yönetimi)*

- Proje için GitHub repository oluşturuldu ve ekip erişimleri sağlandı.  
- `main` branch temel yapılandırmaları ve proje başlangıç ayarları gerçekleştirildi.  
- Proje sürecinin düzenli şekilde takip edilmesi amacıyla **projeakisi.md** dokümanı oluşturuldu.  
- Takım üyelerine Git ve GitHub iş akışı hakkında temel bilgilendirme yapıldı.  
- Sprint planlaması ve görev dağılımı organize edildi.

---

## 👤 Nedim İsa *(Gereksinim Toplama ve Belgeleme)*

Tele-Sağlık Platformu için sistemin fonksiyonel ve teknik gereksinimlerini belirlemek amacıyla analiz süreci yürütülmüş ve temel kullanım senaryoları dokümante edilmiştir.

### 📌 Fonksiyonel Gereksinimler

#### Hasta Tarafı

- Sisteme kayıt olabilme ve güvenli giriş yapabilme  
- Profil ve sağlık bilgilerinin görüntülenmesi ve güncellenmesi  
- Uzmanlık alanına göre doktor arama ve listeleme  
- Online randevu oluşturma, görüntüleme, iptal etme ve yeniden planlama  
- Randevu saatinde video görüşme başlatabilme  
- Tıbbi kayıt geçmişini görüntüleyebilme  

#### Doktor Tarafı

- Sisteme giriş yapabilme ve profil yönetimi  
- Randevu taleplerini görüntüleme ve onaylama  
- Video görüşme oturumu başlatabilme  
- Hastaya ait tıbbi kayıtları görüntüleme ve yeni kayıt oluşturma  
- Çalışma saatleri ve uygunluk takvimi yönetimi  

#### Yönetici (Admin) Tarafı

- Kullanıcı yönetimi (hasta ve doktor)  
- Rol atama ve doktor doğrulama işlemleri  
- Sistem genelinde randevu ve kayıt verilerini izleme  
- Sistem güvenliği ve veri bütünlüğü kontrolleri  

---

### ⚙️ Teknik Gereksinimler

- Backend geliştirme için **Java & Spring Boot** kullanımı  
- Frontend geliştirme için **Angular tabanlı responsive arayüz**  
- Veri yönetimi için **MySQL veritabanı**  
- Kimlik doğrulama için **JWT Authentication**  
- Güvenli veri transferi için **TLS / HTTPS protokolü**  
- Gerçek zamanlı görüşmeler için **WebRTC altyapısı**  

---

### 📖 Kullanıcı Hikayeleri

- *Bir hasta olarak hızlı şekilde uygun doktordan randevu almak istiyorum.*  
- *Bir doktor olarak randevu taleplerimi sistem üzerinden yönetmek istiyorum.*  
- *Bir kullanıcı olarak uzaktan video görüşme ile sağlık danışmanlığı almak istiyorum.*  
- *Bir yönetici olarak platformun güvenli ve düzenli çalışmasını sağlamak istiyorum.*

---

### 🎯 Örnek Kullanım Senaryosu

1. Hasta sisteme giriş yapar  
2. Doktor araması gerçekleştirir  
3. Uygun randevu saatini seçer  
4. Randevu oluşturulur  
5. Görüşme saatinde video oturumu başlatılır  
6. Doktor tıbbi kayıt ekler  
7. Hasta kayıtları panelden görüntüler  

---

## 👤 Ömer Doğan *(Sistem Kalitesi ve Teknoloji Analizi)*

### 🔐 Güvenlik

- Sağlık verilerinin korunması için **KVKK / GDPR uyum hedefi** belirlenmiştir.  
- Veri transferinde **TLS 1.3 şifreleme protokolü** planlanmıştır.  
- Sistem girişlerinde **JWT ve isteğe bağlı 2FA doğrulama** öngörülmüştür.  
- **RBAC yetkilendirme modeli** ile kullanıcı erişimleri ayrıştırılacaktır.

### 🚀 Performans ve Ölçeklenebilirlik

- Kritik işlemlerin **2 saniyenin altında yanıt süresi** hedeflenmiştir.  
- Video konferans sırasında **en az 100 eşzamanlı oturum** desteği planlanmıştır.  
- MySQL üzerinde **indeksleme ve sorgu optimizasyonu** çalışmaları öngörülmüştür.

### 📱 Kullanılabilirlik

- Angular arayüzünün tüm cihazlarda **responsive çalışması** hedeflenmiştir.  
- **WCAG standartlarına uygun erişilebilirlik** planlanmıştır.

### ☁️ Güvenilirlik

- Sistem için **%99.9 uptime** hedefi belirlenmiştir.  
- Spring Boot tarafında **Global Exception Handling** uygulanacaktır.

---

## 👤 Zelal Ergin *(Geliştirme Ortamı Kurulumu)*

- IntelliJ IDEA ve JDK kurulumu tamamlandı.  
- Git sürüm kontrol sistemi yapılandırıldı.  
- Repository klonlanarak yerel çalışma ortamı hazırlandı.  
- Backend ve frontend klasör mimarisi oluşturuldu.  
- İlk yapılandırmalar ana branch’e başarıyla gönderildi.

---

## 👤 Ahmet Akif Yılmaz *(Veri Modeli Tasarımı)*

Tele-Sağlık platformu için kullanıcı, hasta, doktor ve randevu yönetimini kapsayan veritabanı modeli tasarlanmıştır.

### 📊 Tablolar

#### Users

| Alan | Tip |
|------|-----|
| id | BIGINT |
| full_name | VARCHAR |
| email | VARCHAR |
| password | VARCHAR |
| phone | VARCHAR |
| role | ENUM |
| created_at | DATETIME |

#### Patients

| Alan | Tip |
|------|-----|
| id | BIGINT |
| user_id | BIGINT |
| birth_date | DATE |
| gender | VARCHAR |
| address | VARCHAR |
| blood_type | VARCHAR |

#### Doctors

| Alan | Tip |
|------|-----|
| id | BIGINT |
| user_id | BIGINT |
| specialty_id | BIGINT |
| license_number | VARCHAR |
| experience_years | INT |

#### Appointments

| Alan | Tip |
|------|-----|
| id | BIGINT |
| patient_id | BIGINT |
| doctor_id | BIGINT |
| appointment_date | DATETIME |
| status | ENUM |
| notes | TEXT |

---

### 🔗 İlişkiler

- Users → Patients (1-1)  
- Users → Doctors (1-1)  
- Patients → Appointments (1-N)  
- Doctors → Appointments (1-N)  

---

## 🧩 ER Diagram

![ER Diyagramı](er_diagram.jpg)

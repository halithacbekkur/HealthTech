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

*(Bu hafta kapsamında görev teslimi bulunmamaktadır.)*

---

### 👤 Ömer Doğan  

## 1️⃣ Güvenlik (Security)

Sağlık verilerinin korunması (**KVKK / GDPR uyumu**) en yüksek öncelik olarak belirlenmiştir.

- **Veri Gizliliği:**  
  Tüm verilerin aktarım sırasında **TLS 1.3** protokolü ile şifrelenmesi planlanmaktadır.

- **Kimlik Doğrulama:**  
  Sistem girişlerinde **JWT (JSON Web Token)** kullanılması ve mümkünse **İki Faktörlü Doğrulama (2FA)** desteği sağlanması hedeflenmektedir.

- **Yetkilendirme:**  
  **Role Based Access Control (RBAC)** yaklaşımı ile doktor, hasta ve admin erişim alanlarının net şekilde ayrılması öngörülmektedir.

---

## 2️⃣ Performans ve Ölçeklenebilirlik (Performance & Scalability)

- **Yanıt Süresi:**  
  Online randevu listeleme veya tıbbi kayıt sorgulama işlemlerinin **2 saniyenin altında** sonuçlanması hedeflenmektedir.

- **Eşzamanlılık:**  
  Video konferans sırasında sistemin aynı anda **en az 100 aktif oturumu** sorunsuz desteklemesi planlanmaktadır.

- **Veritabanı Optimizasyonu:**  
  **MySQL** üzerinde sık kullanılan sorgular için uygun **indeksleme** stratejilerinin uygulanması düşünülmektedir.

---

## 3️⃣ Kullanılabilirlik ve Mobil Uyumluluk (Usability)

- **Responsive Tasarım:**  
  **Angular** ile geliştirilecek arayüzün masaüstü, tablet ve mobil cihazlarla **%100 uyumlu** olması hedeflenmektedir.

- **Erişilebilirlik:**  
  Renk paleti ve yazı boyutlarının, yaşlı veya görme zorluğu yaşayan kullanıcılar için uygun (**WCAG standartlarında**) olması planlanmaktadır.

---

## 4️⃣ Güvenilirlik ve Erişilebilirlik (Reliability & Availability)

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

### 1️⃣ Giriş  

Bu doküman, tele-sağlık platformu için tasarlanan veritabanı modelini açıklamaktadır. Sistem; hasta, doktor ve yönetici kullanıcılarını, randevu işlemlerini ve tıbbi kayıtları yönetmek amacıyla tasarlanmıştır.

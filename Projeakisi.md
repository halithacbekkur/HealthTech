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


**Ahmet Akif Yılmaz:**


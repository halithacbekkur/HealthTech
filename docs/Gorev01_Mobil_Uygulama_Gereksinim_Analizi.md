# 📱 Mobil Uygulama Gereksinim Analizi Dokümanı

**Proje:** Tele-Sağlık / HealthTech Platformu  
**Görev:** Hafta 1 – Görev 1  
**Sorumlu:** CENA İSMAİL  
**Tarih:** 2026-05-08  
**Versiyon:** 2.0  

---

## 1. Giriş ve Amaç

Bu doküman, Tele-Sağlık Platformu'nun **hasta** ve **doktor** kullanıcıları için mobil uygulama gereksinimlerini kapsamlı şekilde tanımlar. Doküman; fonksiyonel gereksinimler, kullanıcı hikayeleri, kullanım senaryoları, kabul kriterleri, arayüz ihtiyaçları, güvenlik gereksinimleri ve teknik altyapı bilgilerini içerir.

**Hedef Kitle:** Hasta, Doktor, Sistem Yöneticisi  
**Platform:** Responsive Web (Angular, mobile-first) + Gelecekte Native Mobil (opsiyonel)  
**Backend:** Spring Boot REST API (mevcut altyapı)

---

## 2. Hasta Mobil Modülü Gereksinimleri

### 2.1 Kimlik Doğrulama & Profil

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| H-01 | E-posta ve şifre ile kayıt olma | Yüksek | ✅ Mevcut | `POST /api/auth/register` |
| H-02 | Güvenli giriş yapma (JWT) | Yüksek | ✅ Mevcut | `POST /api/auth/login` |
| H-03 | Profil bilgilerini görüntüleme | Yüksek | ✅ Mevcut | `GET /api/users/me` |
| H-04 | Profil bilgilerini güncelleme | Orta | ✅ Mevcut | `PUT /api/users/{id}` |
| H-05 | Şifre değiştirme | Orta | 🔧 Hafta 4 – Görev 17 | `PUT /api/users/change-password` |
| H-06 | Profil fotoğrafı yükleme | Düşük | 🔧 Hafta 4 – Görev 16 | `POST /api/users/avatar` |

### 2.2 Randevu Yönetimi

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| H-07 | Uzmanlık alanına göre doktor arama | Yüksek | 🔧 Hafta 4 – Görev 17 | `GET /api/doctors/search?specialty=...` |
| H-08 | Doktor müsaitlik takvimi görüntüleme | Yüksek | 🔧 Hafta 4 – Görev 17 | `GET /api/doctors/{id}/availability` |
| H-09 | Randevu oluşturma | Yüksek | ✅ Mevcut | `POST /api/appointments` |
| H-10 | Randevularımı listeleme | Yüksek | ✅ Mevcut | `GET /api/appointments/my` |
| H-11 | Randevu iptal etme | Yüksek | ✅ Mevcut | `PUT /api/appointments/{id}/cancel` |
| H-12 | Randevu hatırlatma bildirimi | Orta | 🔧 Hafta 5 – Görev 22 | E-posta / Push bildirim servisi |

### 2.3 Tıbbi Kayıtlar

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| H-13 | Tıbbi kayıtlarımı görüntüleme | Yüksek | ✅ Mevcut | `GET /api/medical-records/**` |
| H-14 | Reçetelerimi görüntüleme | Yüksek | ✅ Mevcut | `GET /api/prescriptions/**` |
| H-15 | Sağlık geçmişi özeti | Orta | 🔧 Hafta 4 – Görev 18 | `GET /api/patients/{id}/history` |

### 2.4 Video Görüşme

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| H-16 | Video görüşme başlatma | Yüksek | 🔧 Hafta 1-G5 (Plan) / Hafta 4 (Kod) | WebRTC / Jitsi Meet |
| H-17 | Görüşme sırasında mesajlaşma | Orta | 🔧 Hafta 4 – Görev 17 | WebSocket chat servisi |
| H-18 | Görüşme geçmişi | Düşük | 🔧 Hafta 4 – Görev 18 | `GET /api/video/history` |

---

## 3. Doktor Mobil Modülü Gereksinimleri

### 3.1 Kimlik Doğrulama & Profil

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| D-01 | Giriş (DOCTOR rolü) | Yüksek | ✅ Mevcut | `POST /api/auth/login` |
| D-02 | Profil ve uzmanlık yönetimi | Yüksek | ✅ Mevcut | `GET/PUT /api/users` |
| D-03 | Çalışma saatleri belirleme | Orta | 🔧 Hafta 4 – Görev 17 | `POST /api/doctors/schedule` |

### 3.2 Randevu Yönetimi

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| D-04 | Randevu taleplerini listeleme | Yüksek | ✅ Mevcut | `GET /api/appointments/doctor` |
| D-05 | Randevuyu onaylama | Yüksek | ✅ Mevcut | `PUT /api/appointments/{id}/approve` |
| D-06 | Randevuyu iptal etme | Yüksek | ✅ Mevcut | `PUT /api/appointments/{id}/cancel` |
| D-07 | Randevu takvimi görünümü | Orta | 🔧 Hafta 3 – Görev 14 | Frontend takvim bileşeni |

### 3.3 Hasta Takibi & Tıbbi Kayıt

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| D-08 | Hasta tıbbi kaydı görüntüleme | Yüksek | ✅ Mevcut | `GET /api/medical-records/**` |
| D-09 | Tıbbi kayıt oluşturma/güncelleme | Yüksek | ✅ Mevcut | `POST/PUT /api/medical-records` |
| D-10 | Reçete yazma | Yüksek | ✅ Mevcut | `POST /api/prescriptions` |
| D-11 | Hasta listesi görüntüleme | Orta | 🔧 Hafta 4 – Görev 16 | `GET /api/doctors/{id}/patients` |

### 3.4 Video Görüşme

| ID | Gereksinim | Öncelik | Durum | API / Plan |
|----|-----------|---------|-------|------------|
| D-12 | Video görüşme başlatma | Yüksek | 🔧 Hafta 1-G5 (Plan) / Hafta 4 (Kod) | WebRTC / Jitsi Meet |
| D-13 | Görüşme sırasında not alma | Orta | 🔧 Hafta 4 – Görev 17 | `POST /api/appointments/{id}/notes` |

---

## 4. Kullanıcı Hikayeleri (User Stories)

### 4.1 Hasta Hikayeleri

| ID | Kullanıcı Hikayesi | Kabul Kriteri |
|----|-------------------|---------------|
| US-01 | Bir hasta olarak, sisteme kayıt olup giriş yapabilmek istiyorum ki sağlık hizmetlerine erişebileyim. | Kayıt sonrası JWT token ile giriş yapılabilmeli. Hatalı bilgide uyarı gösterilmeli. |
| US-02 | Bir hasta olarak, uzmanlık alanına göre doktor arayabilmek istiyorum ki ihtiyacıma uygun doktoru bulabileyim. | Uzmanlık, isim veya lokasyona göre filtreleme yapılabilmeli. Sonuçlar kart görünümünde listelenmeli. |
| US-03 | Bir hasta olarak, uygun doktordan online randevu alabilmek istiyorum ki hastaneye gitmeden sağlık danışmanlığı alabileyim. | Doktor müsait saatlerinden seçim yapılabilmeli. Randevu oluşturulunca onay mesajı gösterilmeli. |
| US-04 | Bir hasta olarak, randevu saatimde video görüşme başlatabilmek istiyorum ki doktorumla yüz yüze iletişim kurabileyim. | Onaylanan randevu saatinde "Görüşme Başlat" butonu aktif olmalı. Kamera ve mikrofon izni istenmeli. |
| US-05 | Bir hasta olarak, tıbbi kayıtlarımı ve reçetelerimi görüntüleyebilmek istiyorum ki sağlık geçmişimi takip edebileyim. | Kan grubu, alerji, geçmiş hastalıklar ve reçete listesi görüntülenebilmeli. |
| US-06 | Bir hasta olarak, randevularımı görüntüleyip iptal edebilmek istiyorum ki programımı yönetebilmeli. | Bekleyen, onaylanan, tamamlanan randevular filtrelenebilmeli. İptal işlemi onay kutusundan sonra yapılmalı. |

### 4.2 Doktor Hikayeleri

| ID | Kullanıcı Hikayesi | Kabul Kriteri |
|----|-------------------|---------------|
| US-07 | Bir doktor olarak, gelen randevu taleplerini görüp onaylayabilmek istiyorum ki hasta programımı yönetebilmeli. | Bekleyen talepler listede görünmeli. Tek tıkla onaylama/reddetme yapılabilmeli. |
| US-08 | Bir doktor olarak, hastamın tıbbi kayıtlarını görüntüleyip güncelleyebilmek istiyorum ki doğru teşhis koyabileyim. | Görüşme öncesi hastanın tüm kayıtları erişilebilir olmalı. Yeni kayıt oluşturulabilmeli. |
| US-09 | Bir doktor olarak, görüşme sonrası reçete yazabilmek istiyorum ki hastam tedavisine başlayabilsin. | İlaç adı, dozaj ve kullanım talimatı girilebilmeli. Reçete randevuya bağlı olmalı. |
| US-10 | Bir doktor olarak, video görüşme başlatıp not alabilmek istiyorum ki muayeneyi dijital ortamda yapabileyim. | Görüşme başlatıldığında karşı tarafın kamerasını görmeli. Not alanı görüşme sırasında erişilebilir olmalı. |
| US-11 | Bir doktor olarak, çalışma saatlerimi belirleyebilmek istiyorum ki hastalar sadece uygun saatlerimden randevu alabilsin. | Haftalık takvimde müsait saatler seçilebilmeli. Kapalı saatlere randevu alınamamalı. |

---

## 5. Kullanım Senaryoları (Use Cases)

### Senaryo 1: Hasta Randevu Alma

```
Aktör: Hasta
Ön Koşul: Hasta sisteme giriş yapmış olmalı
Ana Akış:
  1. Hasta "Doktor Ara" ekranına gider
  2. Uzmanlık alanı seçer (ör: Dahiliye)
  3. Listelenen doktorlardan birini seçer
  4. Doktorun müsait tarih/saatlerini görür
  5. Uygun saati seçer ve "Randevu Al" butonuna tıklar
  6. Sistem randevuyu PENDING durumunda oluşturur
  7. Hasta onay mesajı görür
Alternatif Akış:
  3a. Uygun doktor bulunamazsa bilgilendirme mesajı gösterilir
  5a. Seçilen saat doluysa uyarı verilir
Son Koşul: Randevu veritabanına kaydedilir, doktora bildirim gider
```

### Senaryo 2: Doktor Randevu Onaylama ve Görüşme

```
Aktör: Doktor
Ön Koşul: Doktor sisteme giriş yapmış, bekleyen randevu var
Ana Akış:
  1. Doktor "Dashboard" ekranında bekleyen talepleri görür
  2. Hasta bilgilerini ve tıbbi kaydını inceler
  3. "Onayla" butonuna tıklar → Randevu APPROVED olur
  4. Randevu saati geldiğinde "Görüşme Başlat" butonu aktif olur
  5. Doktor video görüşmeyi başlatır
  6. Görüşme sonunda tıbbi kayıt oluşturur
  7. Gerekirse reçete yazar
Alternatif Akış:
  3a. Doktor "Reddet" butonuna tıklar → Randevu CANCELLED olur
Son Koşul: Tıbbi kayıt ve reçete veritabanına kaydedilir
```

### Senaryo 3: Video Görüşme

```
Aktör: Hasta ve Doktor
Ön Koşul: Randevu APPROVED durumunda, randevu saati gelmiş
Ana Akış:
  1. Her iki taraf da "Görüşme Başlat" butonunu görür
  2. İlk tıklayan taraf bekleme odasına alınır
  3. Diğer taraf katılınca görüşme başlar
  4. Kamera, mikrofon ve ekran paylaşımı kullanılabilir
  5. Görüşme bitince "Sonlandır" butonuna tıklanır
  6. Randevu durumu COMPLETED olarak güncellenir
Alternatif Akış:
  2a. Bağlantı hatası olursa yeniden bağlanma seçeneği sunulur
Son Koşul: Görüşme süresi kaydedilir, doktor tıbbi kayıt girebilir
```

---

## 6. Kullanıcı Arayüzü (UI/UX) Gereksinimleri

### 6.1 Hasta Ekranları

| Ekran | Açıklama | İçerik Detayı | Planlanan Hafta |
|-------|----------|---------------|-----------------|
| Giriş / Kayıt | E-posta, şifre, tam ad | Form validasyonu, "Şifremi Unuttum" linki, rol seçimi | Hafta 3 – Görev 14 |
| Ana Sayfa (Dashboard) | Yaklaşan randevular | Kartlarda doktor adı, tarih, saat, durum gösterilir. Hızlı erişim butonları | Hafta 3 – Görev 14 |
| Doktor Arama | Uzmanlık filtresi | Doktor kartları: fotoğraf, isim, uzmanlık, puan. Arama çubuğu | Hafta 4 – Görev 16 |
| Randevu Oluşturma | Tarih/saat seçici | Takvim görünümü, müsait saatler yeşil, dolu saatler gri | Hafta 3 – Görev 14 |
| Randevularım | Liste görünümü | Durum filtreleri (Bekleyen/Onaylı/Tamamlanan/İptal). Detay sayfasına link | Hafta 4 – Görev 16 |
| Tıbbi Kayıtlarım | Sağlık bilgileri | Kan grubu, boy, kilo, alerji, geçmiş hastalıklar kartları | Hafta 4 – Görev 16 |
| Reçetelerim | Reçete listesi | Tarih, doktor, ilaç listesi, kullanım talimatı | Hafta 4 – Görev 16 |
| Video Görüşme | Kamera kontrolü | Tam ekran video, kontrol çubuğu (sessize al, kamera kapat, paylaş, bitir) | Hafta 4 – Görev 17 |
| Profil Ayarları | Bilgi güncelleme | Form alanları, fotoğraf yükleme, şifre değiştirme | Hafta 3 – Görev 14 |

### 6.2 Doktor Ekranları

| Ekran | Açıklama | İçerik Detayı | Planlanan Hafta |
|-------|----------|---------------|-----------------|
| Giriş | E-posta, şifre | Doktor rolü otomatik tanınır | Hafta 3 – Görev 14 |
| Dashboard | Günlük özet | Bugünkü randevular, bekleyen talepler sayısı, bildirimler | Hafta 3 – Görev 14 |
| Randevu Yönetimi | Onay/red | Hasta bilgisi, randevu tarihi, onayla/reddet butonları | Hafta 4 – Görev 16 |
| Hasta Detay | Tıbbi geçmiş | Hastanın tüm kayıtları, reçeteleri, geçmiş randevuları | Hafta 4 – Görev 16 |
| Reçete Yazma | Reçete formu | İlaç adı, dozaj, süre, kullanım talimatı alanları | Hafta 4 – Görev 16 |
| Video Görüşme | Kamera + not | Sol: video ekranı, Sağ: not alma paneli | Hafta 4 – Görev 17 |
| Çalışma Saatleri | Takvim | Haftalık takvimde müsait saatleri işaretleme | Hafta 4 – Görev 17 |

### 6.3 Genel UI Prensipleri

- **Renk Paleti:** Sağlık temalı (mavi-beyaz-yeşil tonları)
- **Tipografi:** Google Fonts – Inter veya Roboto
- **Responsive:** Mobile-first yaklaşım (360px – 1920px arası)
- **Dark Mode:** Opsiyonel, kullanıcı tercihine göre
- **Animasyonlar:** Sayfa geçişlerinde yumuşak fade/slide efektleri
- **Erişilebilirlik:** WCAG 2.1 AA uyumlu, yeterli kontrast oranı

---

## 7. Güvenlik ve Gizlilik Gereksinimleri

| ID | Gereksinim | Açıklama |
|----|-----------|----------|
| SEC-01 | JWT Token tabanlı kimlik doğrulama | Her API isteğinde Authorization header'da Bearer token gönderilmeli |
| SEC-02 | Şifre güvenliği | BCrypt hashing, minimum 8 karakter, büyük/küçük harf + rakam zorunlu |
| SEC-03 | HTTPS/TLS 1.3 | Tüm veri transferi şifreli olmalı |
| SEC-04 | Rol bazlı erişim kontrolü (RBAC) | PATIENT sadece kendi verilerine, DOCTOR kendi hastalarına, ADMIN tüm verilere erişebilmeli |
| SEC-05 | Oturum süresi | JWT token 24 saat geçerli, sonra yeniden giriş gerekli |
| SEC-06 | KVKK / GDPR uyumu | Kişisel sağlık verileri özel kategori olarak korunmalı |
| SEC-07 | Veri şifreleme | Hassas tıbbi veriler veritabanında şifrelenmiş tutulmalı |
| SEC-08 | Input validasyonu | XSS, SQL Injection saldırılarına karşı tüm girişler doğrulanmalı |
| SEC-09 | Video görüşme güvenliği | Görüşmeler end-to-end şifrelenmeli, sadece randevu tarafları erişebilmeli |
| SEC-10 | Audit log | Kritik işlemler (giriş, kayıt değişikliği, reçete) loglanmalı |

---

## 8. Teknik Gereksinimler

| Kategori | Gereksinim | Durum |
|----------|-----------|-------|
| **Frontend** | Angular 17+ (responsive, mobile-first) | 🔧 Hafta 3 – Görev 14 |
| **Backend** | Spring Boot 4.x REST API | ✅ Mevcut |
| **Kimlik Doğrulama** | JWT Token (jjwt-api 0.12.6) | ✅ Mevcut |
| **Veritabanı** | MySQL 8.x | ✅ Mevcut |
| **ORM** | Spring Data JPA + Hibernate | ✅ Mevcut |
| **API Dokümantasyonu** | Swagger/OpenAPI (springdoc 2.8.8) | ✅ Mevcut |
| **Video Konferans** | WebRTC / Jitsi Meet | 🔧 Hafta 1 – Görev 5 (Araştırma) |
| **Bildirim** | E-posta + Push notification | 🔧 Hafta 5 – Görev 22 |
| **Minimum Çözünürlük** | 360x640 px | 🔧 Hafta 3 – Görev 14 |
| **Tarayıcı** | Chrome, Safari, Firefox (son 2 versiyon) | 🔧 Hafta 5 – Görev 23 |

---

## 9. API Geliştirme Yol Haritası

| API Endpoint | Açıklama | Planlanan Hafta |
|-------------|----------|-----------------|
| `GET /api/doctors/search?specialty=...` | Doktor arama | Hafta 4 – Görev 17 |
| `GET /api/doctors/{id}/availability` | Müsaitlik takvimi | Hafta 4 – Görev 17 |
| `PUT /api/users/change-password` | Şifre değiştirme | Hafta 4 – Görev 17 |
| `POST /api/doctors/schedule` | Çalışma saatleri | Hafta 4 – Görev 17 |
| `POST /api/video/session` | Video oturumu | Hafta 4 – Görev 17 |
| `GET /api/patients/{id}/history` | Sağlık geçmişi | Hafta 4 – Görev 18 |
| `GET /api/doctors/{id}/patients` | Hasta listesi | Hafta 4 – Görev 16 |
| `GET /api/video/history` | Görüşme geçmişi | Hafta 4 – Görev 18 |
| `POST /api/appointments/{id}/notes` | Görüşme notları | Hafta 4 – Görev 17 |
| `POST /api/users/avatar` | Profil fotoğrafı | Hafta 4 – Görev 16 |
| Bildirim endpoint'leri | Push / E-posta | Hafta 5 – Görev 22 |

---

## 10. Gereksinim Özet Tablosu

| Kategori | Toplam | ✅ Mevcut | 🔧 Planlandı |
|----------|--------|-----------|--------------|
| Hasta Gereksinimleri | 18 | 8 | 10 |
| Doktor Gereksinimleri | 13 | 8 | 5 |
| Kullanıcı Hikayeleri | 11 | — | 11 (Tümü tanımlı) |
| Kullanım Senaryoları | 3 | — | 3 (Tümü tanımlı) |
| Güvenlik Gereksinimleri | 10 | 4 | 6 |
| **Toplam** | **55** | **20** | **35** |

---

## 11. Sonuç

Bu doküman, Tele-Sağlık Platformu'nun mobil uygulama gereksinimlerini **eksiksiz** şekilde tanımlamaktadır. Toplam **55 gereksinim** belirlenmiş olup, mevcut backend altyapısı 20 gereksinimi zaten karşılamaktadır. Kalan 35 gereksinim ilgili hafta ve görevlere atanmıştır.

Tüm gereksinimler; kullanıcı hikayeleri, kabul kriterleri, kullanım senaryoları, güvenlik gereksinimleri ve UI ekran tanımları ile desteklenmiştir.

---

*Hazırlayan: CENA İSMAİL | Onaylayan: Halit Hacbekkur (Scrum Master)*

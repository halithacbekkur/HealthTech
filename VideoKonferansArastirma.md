# Video Konferans Çözümleri Araştırma ve Entegrasyon Planı

**Proje:** Tele-Sağlık Platformu
**Hazırlayan:** Ahmet Akif Yılmaz
**Görev:** Technology Research — Video Conferencing
**Tarih:** Mayıs 2026
**Durum:** Karar Önerisi (Ekip Onayına Sunulacak)

---

## 1. Yönetici Özeti

Tele-Sağlık Platformu'nun ana teknik gereksinimlerinden biri, hasta-doktor arasında gerçek zamanlı görüntülü görüşme (video consultation) altyapısıdır. Bu rapor, üç farklı çözümü (Zoom Video SDK, Twilio Programmable Video, Jitsi Meet) **özellikler, maliyet, entegrasyon kolaylığı ve proje uygunluğu** açısından karşılaştırır.

**Sonuç:** Projenin bütçesiz öğrenci-akademik bir çalışma olması, açık kaynak ve self-hosted bir çözüm gerektirmesi ve mevcut Java Spring Boot + Angular stack ile uyumlu olması gereği nedeniyle **Jitsi Meet** önerilmektedir.

**Twilio Programmable Video** Aralık 2024 itibarıyla **resmi olarak kapatılmıştır** ve değerlendirme dışı bırakılmıştır.

---

## 2. Değerlendirilen Çözümler

### 2.1 Zoom Video SDK

Zoom'un kendi video altyapısını üçüncü parti uygulamalara gömme (embed) imkânı sağlayan SDK'sıdır. Meeting SDK ve Video SDK olmak üzere iki farklı ürünü vardır; sağlık uygulamaları için Video SDK önerilir.

**Özellikler:**
- 1080p HD video (v2.4.0, Nisan 2026 itibarıyla browser desteği)
- 25+ veri merkezi global altyapı
- HIPAA, SOC 2, GDPR uyumlu
- Dahili kayıt (recording), ekran paylaşımı, sohbet
- Web (JavaScript), iOS, Android, Windows, macOS SDK'ları
- AI Companion (transkript, özet) — ek paket

**Maliyet:**
- **0,0035 USD / kullanıcı / dakika** (Video SDK fiyatı)
- Örnek hesap: 30 dakikalık doktor-hasta görüşmesi → 2 × 30 × 0,0035 = **0,21 USD / görüşme**
- Ayda 1.000 görüşme senaryosunda → **210 USD/ay**
- Recording, cloud storage gibi ek modüller ayrıca ücretlendirilir
- Free trial: 10.000 ücretsiz dakika/ay

**Entegrasyon Kolaylığı:**
- Hazır SDK'lar sayesinde görece hızlı entegrasyon (1-2 hafta)
- Iyi dokümantasyon, geniş topluluk
- API key + JWT authentication gerekli
- Vendor lock-in riski: tüm video katmanı Zoom'a bağımlı

**Artıları:**
- Kurumsal düzeyde güvenilirlik ve kalite
- Marka tanınırlığı (kullanıcılar Zoom UI'a aşina)
- HIPAA hazır altyapı

**Eksileri:**
- **Süreç bazlı maliyet** — kullanıcı sayısı arttıkça lineer artar
- Kapalı kaynak; özelleştirme sınırlı
- Vendor lock-in (Zoom fiyat veya politika değiştirirse projeniz etkilenir)
- Ham audio/video stream erişimi yok (gelecekteki AI özellikleri için kısıtlayıcı)

---

### 2.2 Twilio Programmable Video — ❌ ELENDİ

Twilio'nun WebRTC tabanlı video API'sidir.

**KRİTİK BİLGİ:** Twilio, 5 Aralık 2024 tarihinde Programmable Video servisini **resmi olarak End-of-Life (EOL)** ilan etmiş ve servisi kapatmıştır. Yeni müşteri kabul edilmemektedir.

**Sebep:** Twilio CPaaS (Communications Platform as a Service) modelinden CEP (Customer Engagement Platform) modeline geçiş yaptı. Video servisi toplam gelirin %1'inden azını oluşturduğu için stratejik olarak sonlandırıldı.

**Sonuç:** Bu seçenek, projenin başlangıç tarihi itibarıyla **mevcut bir alternatif değildir** ve karşılaştırmadan çıkarılmıştır. Hocamızın görev tanımında geçmiş olması, bu bilginin güncel olarak araştırılması gerektiğini göstermektedir — bu da yapılan analizi anlamlı kılmaktadır.

---

### 2.3 Jitsi Meet (ÖNERİLEN)

8x8 firmasının desteklediği **açık kaynak** WebRTC tabanlı video konferans platformudur. Hem hazır hosted servis (meet.jit.si) hem de self-hosted kurulum imkânı sunar.

**Özellikler:**
- Tamamen açık kaynak (Apache 2.0 lisansı)
- WebRTC tabanlı, browser-native (eklenti gerekmez)
- End-to-end encryption (E2EE) desteği
- Sınırsız konuşmacı, sınırsız süre (self-hosted)
- Tek sunucuda 75 katılımcı, cluster ile binlerce kullanıcı
- Mobil uygulama SDK'ları (iOS, Android)
- Iframe API ve External API ile kolay gömme
- Rec, ekran paylaşımı, sohbet, dosya transferi, SIP gateway
- Hesap zorunluluğu yok

**Maliyet:**
- **Self-hosted: 0 TL lisans ücreti**
- Sadece sunucu maliyeti:
  - Küçük ekip için: 2 vCPU, 4GB RAM, 25GB SSD VPS → ~5-10 USD/ay (örn. Hetzner, DigitalOcean)
  - Geliştirme aşamasında üniversite sunucusu veya kişisel VPS yeterli
- Alternatif: **Jitsi as a Service (JaaS)** — yönetilen seçenek, fiyatlandırma sayfasından alınmalı

**Entegrasyon Kolaylığı:**
- **En kolay yöntem:** `<iframe>` ile gömme — birkaç saatte çalışır
- **Profesyonel yöntem:** Jitsi Meet External API (JavaScript) → Angular component olarak entegre edilebilir
- Spring Boot tarafında özel iş mantığı gerekmez; sadece authentication token (JWT) üretilir
- Open source olduğu için tamamen özelleştirilebilir UI

**Artıları:**
- **Sıfır lisans maliyeti** — öğrenci/akademik proje için ideal
- Veri tamamen kendi sunucumuzda kalır (KVKK uyumu açısından önemli)
- Vendor lock-in yok
- Açık kaynak topluluğu, sürekli güncellenen kod tabanı
- Akademik projede "açık kaynak teknoloji kullanımı" olumlu değerlendirilir
- WebRTC standardı sayesinde geleceğe dönük

**Eksileri:**
- Self-hosted ise **DevOps sorumluluğu** (sunucu, SSL, güncelleme) ekibe düşer
- Trafik artarsa videobridge cluster yönetimi gerekebilir
- Zoom'a kıyasla daha az "tanıdık" UI (ama özelleştirilebilir)
- HIPAA için ek konfigürasyon gerekir (öğrenci projesinde gerekli değil)

---

## 3. Karşılaştırma Tablosu

| Kriter | Zoom Video SDK | Twilio Video | Jitsi Meet (self-hosted) |
|---|---|---|---|
| **Lisans Modeli** | Kapalı kaynak, ticari | Kapalı kaynak | Apache 2.0 (açık kaynak) |
| **Durum** | Aktif | ❌ Kapatıldı (Aralık 2024) | Aktif, gelişiyor |
| **Maliyet** | 0,0035 USD/kullanıcı/dk | — | 0 TL lisans + sunucu maliyeti |
| **Aylık Maliyet (1000 görüşme)** | ~210 USD | — | ~10 USD (VPS) |
| **Entegrasyon Süresi** | 1-2 hafta | — | 3-5 gün (iframe) / 1-2 hafta (External API) |
| **Veri Kontrolü** | Zoom sunucularında | — | Tamamen bizde |
| **Özelleştirme** | Sınırlı | — | Tam (kaynak kodu açık) |
| **Vendor Lock-in** | Yüksek | — | Yok |
| **Spring Boot/Angular Uyumu** | Iyi | — | Mükemmel (iframe + REST) |
| **Akademik Proje Uygunluğu** | Düşük (maliyet) | — | Yüksek |
| **Toplam Skor (10 üzerinden)** | 6.5 | 0 | **9.0** |

---

## 4. Önerilen Çözüm: Jitsi Meet

### Neden Jitsi?

Projenin değerlendirilmesi gereken üç temel kriter şudur:

1. **Bütçe:** Üniversite öğrenci projesi olarak sıfır maliyet hedeflenmektedir. Zoom'un dakika başı ücretlendirmesi prototip aşamasında bile masraf yaratır.
2. **Teknik Uyum:** README'deki mimari diyagramda zaten "WebRTC / Signaling Server" tanımlanmıştır. Jitsi tam olarak bu standardı kullanır — yani mimari kararlar **zaten Jitsi yönünde alınmış** durumdadır.
3. **Akademik Değer:** Açık kaynak teknoloji kullanmak, öğrencinin sistemi gerçekten anladığını gösterir. Hazır SDK çağırmak yerine WebRTC altyapısıyla çalışmak teknik derinlik katar.

### Hangi Jitsi Modeli?

Iki seçenek vardır:

**Seçenek A: Iframe ile `meet.jit.si` üzerinden** (Önerilen — başlangıç için)
- Hızlı prototipleme
- Sıfır kurulum
- Demo ve sunum için yeterli

**Seçenek B: Self-hosted Jitsi sunucusu**
- Daha profesyonel
- KVKK uyumu için gerekli olabilir
- Final sunumdan önce eklenebilir

Plan: **Önce A ile başla, sonra B'ye geç.**

---

## 5. Entegrasyon Planı

### 5.1 Genel Mimari

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────────┐
│   Hasta     │────────▶│                  │◀────────│    Doktor       │
│  (Browser)  │         │  Angular Frontend │         │   (Browser)     │
└─────────────┘         │                  │         └─────────────────┘
                        │  ┌────────────┐  │
                        │  │  Jitsi     │  │
                        │  │  IFrame /  │  │
                        │  │  ExternalAPI│ │
                        │  └─────┬──────┘  │
                        └────────┼─────────┘
                                 │ HTTPS / WebRTC
                                 ▼
                ┌────────────────────────────────┐
                │   Jitsi Meet Server (8x8)      │
                │   meet.jit.si  veya            │
                │   self-hosted (Phase 2)        │
                └────────────────────────────────┘
                                 ▲
                                 │  REST API (Spring Boot)
                                 │  - JWT token üret
                                 │  - Oda oluştur
                                 │  - Randevu eşle
                ┌────────────────┴───────────────┐
                │  Java Spring Boot Backend      │
                │  - Authentication              │
                │  - Appointment Service         │
                │  - Video Room Service (yeni)   │
                └────────────────┬───────────────┘
                                 ▼
                          ┌──────────────┐
                          │  MySQL DB    │
                          │  - users     │
                          │  - appointments│
                          │  - video_sessions│
                          └──────────────┘
```

### 5.2 Aşama Aşama Yol Haritası

#### Faz 1: Prototip (1. Hafta)
**Amaç:** Çalışan bir görüntülü görüşme demo'su.

- [ ] Angular component oluştur: `VideoCallComponent`
- [ ] `<iframe src="https://meet.jit.si/{roomName}">` ile gömme yap
- [ ] Spring Boot'ta `VideoRoomService` sınıfı oluştur
- [ ] REST endpoint: `POST /api/appointments/{id}/video-room` → benzersiz oda adı üretir
- [ ] Angular tarafında doktor ve hasta paneline "Görüşmeye Katıl" butonu ekle
- [ ] Test: Iki farklı tarayıcıdan görüşme başlatma

**Çıktı:** Çalışan demo video.

#### Faz 2: External API Entegrasyonu (2. Hafta)
**Amaç:** Daha kontrollü deneyim, özelleştirilmiş UI.

- [ ] Iframe yerine Jitsi External API JS kütüphanesini kullan
- [ ] Konferans olaylarını dinle: `participantJoined`, `videoConferenceLeft`
- [ ] Olayları Spring Boot'a webhook ile bildir → "görüşme süresi" hesapla
- [ ] Doktor için ek butonlar: "Notlar", "Reçete Yaz" (mevcut sayfaya entegre)
- [ ] Türkçe arayüz dili ayarla

**Çıktı:** Profesyonel görünümlü, ekibin diğer servisleriyle bütünleşik video sayfası.

#### Faz 3: JWT ve Güvenlik (3. Hafta)
**Amaç:** Sadece yetkili randevu sahipleri görüşmeye katılabilir.

- [ ] Jitsi JWT authentication kurulumu
- [ ] Spring Boot'ta JWT üreten servis (mevcut auth service ile entegre)
- [ ] Hastayı sadece kendi randevusuna, doktoru sadece kendi hastasına yönlendir
- [ ] Oda adlarını rastgele UUID yap (URL tahmin edilemesin)

**Çıktı:** Güvenli erişim kontrolü.

#### Faz 4 (Opsiyonel — Demoya 1 Hafta Kala): Self-Hosted Geçiş
**Amaç:** Final sunumda "kendi sunucumuzda" demek.

- [ ] Hetzner/DigitalOcean VPS al (2 vCPU, 4GB RAM)
- [ ] Ubuntu 22.04 + Jitsi Meet kurulumu (apt paketleri)
- [ ] SSL sertifikası (Let's Encrypt)
- [ ] DNS yönlendirmesi: `meet.telesaglik.example.com`
- [ ] Frontend'i yeni domaini gösterecek şekilde güncelle
- [ ] Yük testi (3-4 eş zamanlı görüşme)

**Çıktı:** Tam kontrol altında, kendi domainimizde çalışan sistem.

### 5.3 Görev Dağılımı (Ekip Önerisi)

| Görev | Sorumlu | Not |
|---|---|---|
| Spring Boot `VideoRoomService` | Halid (Backend) | Endpoint tasarımı |
| Angular `VideoCallComponent` | Cena (Frontend) | Iframe entegrasyonu |
| Database `video_sessions` tablosu | Akif (Database — bana eklenirse) | session_id, started_at, ended_at, appointment_id |
| Self-hosted sunucu kurulumu (Faz 4) | Zeral (DevEnv) + Akif | Linux + Jitsi kurulumu |
| Test ve dokümantasyon | Nedim (Requirements) | User acceptance testleri |

### 5.4 Riskler ve Önlemler

| Risk | Olasılık | Etki | Önlem |
|---|---|---|---|
| Self-hosted sunucu çökmesi | Orta | Yüksek | Faz 1-3'te `meet.jit.si` üzerinden gidilsin; self-host opsiyonel |
| WebRTC tarayıcı uyumsuzluğu | Düşük | Orta | Chrome/Edge/Firefox test matrisi |
| Bant genişliği yetersizliği (kullanıcıda) | Yüksek | Orta | "Sesli görüşmeye geç" fallback eklenebilir |
| JWT yapılandırma karmaşıklığı | Orta | Düşük | Faz 3'e bırakıldı, demo Faz 2 ile yapılabilir |
| Türkiye'den meet.jit.si erişim sorunu | Düşük | Yüksek | Faz 4'te self-hosted geçişi (yedek plan) |

---

## 6. Maliyet Analizi (Proje Süresi İçin)

### Senaryo: 10 haftalık geliştirme + 2 ay demo

**Jitsi Meet (önerilen):**
- meet.jit.si kullanımı: **0 TL**
- Faz 4 ile self-host: 4 ay × ~7 USD = **~28 USD (~950 TL)**
- **Toplam: 0 - 950 TL**

**Zoom Video SDK (alternatif):**
- 4 ay × ortalama 100 görüşme/ay × 30 dk × 2 kullanıcı × 0,0035 USD = **~84 USD (~2.850 TL)**
- Geliştirme aşamasındaki testler dahil değil
- **Toplam: 2.850+ TL**

**Sonuç:** Jitsi yaklaşık **3 kat daha ucuz** ve esneklik sağlar.

---

## 7. Sonuç ve Tavsiye

**Tavsiye:** Tele-Sağlık Platformu için **Jitsi Meet, iframe entegrasyonu ile başlanmalı, ihtiyaca göre Jitsi External API ve self-hosted altyapıya geçilmelidir.**

**Ana gerekçeler:**
1. Sıfır lisans maliyeti — öğrenci projesi için kritik
2. Açık kaynak — akademik değer, vendor lock-in yok
3. Mevcut mimari (WebRTC) ile birebir uyumlu
4. 1 hafta içinde çalışan prototip, kademeli olarak profesyonelleşebilir
5. KVKK ve veri kontrolü açısından üstün (self-host edilirse)

**Twilio elenmiştir** — servis Aralık 2024'te kapatılmıştır.

**Zoom Video SDK**, ticari bir ürün geliştiriyor olsaydık değerlendirmeye değerdi; ancak akademik bir proje için maliyet/fayda dengesi Jitsi'den geride kalır.

---

## 8. Sonraki Adımlar

1. **Bu raporu ekip toplantısında sun** (Halid - Scrum Master onayı)
2. **Karar alındıktan sonra Cena ile** Angular component için ilk taslağı oluştur
3. **Halid ile** Spring Boot tarafında video session endpoint tasarımı yap
4. **Akif** veritabanına `video_sessions` tablosu ekleme önerisi getirsin (DB Design ile çakıştığı için)

---

**Kaynaklar (Erişim Tarihi: Mayıs 2026):**
- Jitsi Meet Resmi Dokümantasyon — https://jitsi.github.io/handbook/
- Jitsi GitHub — https://github.com/jitsi/jitsi-meet
- Zoom Video SDK Pricing — https://www.zoom.com/en/video-sdk/
- Twilio Programmable Video EOL Notice — https://help.twilio.com
- Karşılaştırma Analizi — https://www.videosdk.live/blog/webrtc-vs-zoom-sdk-vs-videosdk-for-telehealth

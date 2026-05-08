# 🎥 Video Konferans Modülü Araştırması ve Entegrasyon Planı

**Proje:** Tele-Sağlık / HealthTech Platformu  
**Görev:** Hafta 1 – Görev 5  
**Sorumlu:** AHMET AKİF YILMAZ  
**Tarih:** 2026-05-08  
**Versiyon:** 1.0  

---

## 1. Giriş

Tele-Sağlık Platformu'nda hasta ve doktor arasında gerçek zamanlı video görüşme yapılabilmesi için uygun bir video konferans çözümü araştırılmıştır. Bu doküman; Zoom API, Twilio Video, Jitsi Meet ve OpenVidu çözümlerini karşılaştırarak en uygun seçeneği belirler.

---

## 2. Araştırılan Çözümler

### 2.1 Jitsi Meet
- **Tür:** Açık kaynak, self-hosted
- **Protokol:** WebRTC tabanlı
- **Maliyet:** Ücretsiz (self-hosted), sunucu maliyeti var
- **Lisans:** Apache 2.0
- **Özellikler:** Ekran paylaşımı, chat, kayıt, şifreleme, API desteği
- **Entegrasyon:** iframe veya Jitsi IFrame API ile kolay entegrasyon
- **Avantajlar:**
  - Açık kaynak, topluluk desteği güçlü
  - Kurulum ve entegrasyon kolay
  - Hesap gerektirmez, link ile katılım
  - End-to-end şifreleme desteği
  - WebRTC standardına uygun — yeni framework'lar destekli
- **Dezavantajlar:**
  - Self-hosted için sunucu yönetimi gerekli
  - Büyük ölçekli kullanımda performans ayarı gerekebilir

### 2.2 Zoom Video SDK
- **Tür:** Ticari, bulut tabanlı
- **Maliyet:** ~$10/ay/host (ücretli plan gerekli)
- **Özellikler:** HD video, kayıt, breakout rooms, API/SDK
- **Entegrasyon:** REST API + SDK (karmaşık)
- **Avantajlar:**
  - Yüksek kaliteli video, güvenilir altyapı
  - Geniş API dokümantasyonu
- **Dezavantajlar:**
  - Ücretli (eğitim projesi için maliyetli)
  - SDK entegrasyonu karmaşık
  - API rate limit kısıtlamaları

### 2.3 Twilio Programmable Video
- **Tür:** Ticari, bulut PaaS
- **Maliyet:** ~$0.004/katılımcı/dakika (kullandıkça öde)
- **Özellikler:** WebRTC tabanlı, ölçeklenebilir, SDK
- **Entegrasyon:** REST API + JavaScript SDK
- **Avantajlar:**
  - Esnek fiyatlandırma (kullandıkça öde)
  - İyi dokümantasyon
  - Ölçeklenebilir
- **Dezavantajlar:**
  - Maliyetli olabilir (kullanım arttıkça)
  - Twilio hesap doğrulaması gerekli

### 2.4 OpenVidu
- **Tür:** Açık kaynak, self-hosted
- **Maliyet:** Community sürümü ücretsiz
- **Özellikler:** WebRTC, kayıt, ekran paylaşımı
- **Entegrasyon:** REST API + JavaScript kütüphanesi
- **Avantajlar:**
  - Açık kaynak, Spring Boot ile uyumlu
  - Docker ile kolay kurulum
- **Dezavantajlar:**
  - Topluluk Jitsi kadar büyük değil
  - Pro özellikleri ücretli

---

## 3. Karşılaştırma Tablosu

| Kriter | Jitsi Meet | Zoom SDK | Twilio Video | OpenVidu |
|--------|-----------|----------|-------------|----------|
| **Maliyet** | Ücretsiz | ~$10/ay | ~$0.004/dk/kişi | Ücretsiz (CE) |
| **Açık Kaynak** | ✅ Evet | ❌ Hayır | ❌ Hayır | ✅ Evet |
| **Entegrasyon Kolaylığı** | Kolay | Zor | Orta | Orta |
| **WebRTC Desteği** | ✅ Evet | Kısmi | ✅ Evet | ✅ Evet |
| **Self-Hosted** | ✅ Evet | ❌ Hayır | ❌ Hayır | ✅ Evet |
| **Ekran Paylaşımı** | ✅ Evet | ✅ Evet | ✅ Evet | ✅ Evet |
| **Kayıt** | ✅ Evet | ✅ Evet | ✅ Evet | ✅ Evet |
| **E2E Şifreleme** | ✅ Evet | ✅ Evet | ✅ Evet | Kısmi |
| **Eşzamanlı Kullanıcı** | ~75/oda | 1000+ | 50/oda | ~100/oda |
| **Topluluk Desteği** | Güçlü | İyi | İyi | Orta |
| **Spring Boot Uyumu** | Kolay | Zor | Orta | İyi |
| **Genel Puan** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |

---

## 4. Önerilen Çözüm: Jitsi Meet

### 4.1 Neden Jitsi Meet?

1. **Sıfır maliyet** — Eğitim projesi için en uygun; sunucu maliyeti çok düşük
2. **Kolay entegrasyon** — iframe ile hızlı entegrasyon, karmaşık SDK gerektirmez
3. **Açık kaynak** — Apache 2.0 lisansı, ticari kısıtlama yok
4. **WebRTC standardı** — Tüm modern tarayıcılarda çalışır, ek plugin gerektirmez
5. **Güvenlik** — End-to-end şifreleme, yetki kontrolüne uygun
6. **Topluluk** — Aktif geliştirici topluluğu, zengin dokümantasyon

### 4.2 Hangi Jitsi Modeli?

Projemiz için **Jitsi IFrame API** modeli önerilmektedir:
- Angular ile `iframe` üzerinden Jitsi oturumu açılır
- Backend'de `JWT` token ile oturum doğrulaması yapılır
- Randevu ID'si ile benzersiz oda adı oluşturulur

**Plan:** Önce API ile başla, sonra ihtiyaç olursa self-hosted Jitsi kurulumuna geç.

---

## 5. Entegrasyon Planı

### 5.1 Genel Mimari

```
Hasta / Doktor (Angular Frontend)
        ↓
   Jitsi IFrame API
        ↓
   Jitsi Meet Sunucusu (meet.jit.si veya self-hosted)
        ↓
   WebRTC Peer-to-Peer Bağlantı
```

### 5.2 Aşama Aşama Yol Haritası

| Aşama | Hafta | Görev |
|-------|-------|-------|
| Faz 1 | 1. Hafta | Altyapı ve araştırma: Angular VideoCallComponent + Jitsi IFrame API |
| Faz 2 | 2. Hafta | API Entegrasyon: JWT tabanlı oda oluşturma, güvenlik, görüşme süresi |
| Faz 3 | 3. Hafta | İleri özellikler: Ekran paylaşımı, kayıt, chat entegrasyonu |
| Faz 4 | 4. Hafta | Test: Performans testleri, kullanılabilirlik testleri, son düzeltmeler |

### 5.3 Örnek Angular Bileşenleri

```
- src/app/video/video-conference.component.ts — ana bileşen
- src/app/video/video-conference.service.ts — Jitsi API servisi
- src/app/models/video-session.model.ts — veri modeli
- src/app/services/video_session.service.ts — API çağrıları
- src/app/components/video-controls.component.ts — kontrol butonları
```

---

## 5.4 Riskler ve Önlemler

| Risk | Olasılık | Etki | Önlem |
|------|----------|------|-------|
| Jitsi API'si erişilemez olabilir | Düşük | Yüksek | Self-hosted Jitsi kurulumu hazır bulundur |
| WebRTC tarayıcı uyumsuzluğu | Düşük | Orta | Chrome/Firefox/Safari test matrisi oluştur |
| Eşzamanlı kullanıcı performansı | Orta | Orta | Yük testi yapılacak, gerekirse ölçeklendirme |
| Bağlantı kalitesi düşüklüğü | Orta | Orta | Adaptif bitrate, bağlantı durumu göstergesi |
| Güvenlik açığı | Düşük | Yüksek | JWT ile oda erişim kontrolü, E2E şifreleme |

---

## 6. Maliyet Analizi

| Çözüm | Başlangıç Maliyeti | Aylık Maliyet | Eğitim Projesi Uygunluğu |
|-------|--------------------|--------------|-|
| **Jitsi Meet (meet.jit.si)** | $0 | $0 | ✅ En uygun |
| Jitsi Meet (Self-hosted) | $5-$20 (VPS) | $5-$20 | ✅ Uygun |
| Zoom Video SDK | $0 | ~$10/host | ⚠️ Maliyetli |
| Twilio Video | $0 | Kullanıma göre | ⚠️ Değişken |

**Sonuç:** Ücretsiz `meet.jit.si` ile başlayıp, ihtiyaç halinde self-hosted'a geçilebilir.

---

## 7. Sonuç ve Tavsiye

Tele-Sağlık Platformu için **Jitsi Meet** önerilmektedir. Seçim gerekçeleri:

- ✅ Sıfır lisans maliyeti — Eğitim projesi için ideal
- ✅ Açık kaynak — Kodları incelenebilir, katkıda bulunulabilir
- ✅ Kolay entegrasyon — iframe ile hızlı Angular entegrasyonu
- ✅ WebRTC standardı — Ek plugin gerektirmez
- ✅ Güvenlik — E2E şifreleme, JWT oda kontrolü
- ✅ Hızlı prototip — Birkaç satır kodla çalışan demo üretilebilir
- ✅ Topluluk — Zengin dokümantasyon, aktif geliştirici topluluğu

---

## 8. Sonraki Adımlar

1. Bu araştırma sonuçlarını ekip ile paylaş (Scrum Master onayı)
2. Angular frontend'de Jitsi IFrame API entegrasyonuna başla
3. Randevu sistemi ile video oturumunu bağla
4. Hafta 4'te entegrasyon testlerini tamamla

---

## Kaynaklar

- Jitsi Meet Dokümantasyonu — https://jitsi.github.io/handbook/
- Jitsi IFrame API — https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-iframe/
- Zoom SDK — https://developers.zoom.us/
- OpenVidu — https://openvidu.io/
- Twilio Programmable Video — https://www.twilio.com/docs/video
- WebRTC Standartları — https://webrtc.org/

---

*Hazırlayan: AHMET AKİF YILMAZ | Onaylayan: Halit Hacbekkur (Scrum Master)*

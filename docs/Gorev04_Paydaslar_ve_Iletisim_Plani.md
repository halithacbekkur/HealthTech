# 👥 Proje Paydaşları Analizi ve İletişim Planı

**Proje:** Tele-Sağlık / HealthTech Platformu  
**Görev:** Hafta 1 – Görev 4  
**Sorumlu:** NEDİM İSA  
**Tarih:** 2026-05-08  
**Versiyon:** 1.0  

---

## 1. Paydaş Tanımları

### 1.1 Birincil Paydaşlar (Doğrudan Kullanıcılar)

| Paydaş | Rol | Açıklama |
|--------|-----|----------|
| **Hasta** | Son Kullanıcı | Platformdan randevu alan, video görüşme yapan ve tıbbi kayıtlarını takip eden kişi |
| **Doktor** | Hizmet Sağlayıcı | Randevu onaylayan, video görüşme yapan, tıbbi kayıt ve reçete oluşturan sağlık profesyoneli |
| **Sistem Yöneticisi (Admin)** | Platform Yöneticisi | Kullanıcı hesaplarını, rolleri ve sistem genelini yöneten kişi |

### 1.2 İkincil Paydaşlar (Dolaylı Etkilenenler)

| Paydaş | Rol | Açıklama |
|--------|-----|----------|
| **Sağlık Personeli** | Destek | Doktora yardımcı olan hemşire, laborant gibi personel |
| **Proje Ekibi** | Geliştirici | Platformu geliştiren yazılım mühendisliği öğrencileri |
| **Proje Danışmanı / Hoca** | Denetleyici | Projeyi değerlendiren ve yönlendiren akademik danışman |
| **Sağlık Kurumu** | Kurumsal | Platformun kullanılacağı hastane veya klinik |

---

## 2. Paydaş İhtiyaç Analizi

### 2.1 Hasta İhtiyaçları

| İhtiyaç | Öncelik | Karşılama Yöntemi |
|---------|---------|-------------------|
| Kolay ve hızlı randevu alma | Yüksek | Doktor arama + tek tıkla randevu oluşturma |
| Uzaktan doktor görüşmesi | Yüksek | Video konferans modülü (WebRTC/Jitsi) |
| Tıbbi geçmişini görüntüleme | Yüksek | Tıbbi kayıt ve reçete ekranları |
| Güvenli kişisel veri saklama | Yüksek | JWT kimlik doğrulama + HTTPS + RBAC |
| Mobil uyumlu arayüz | Orta | Angular responsive tasarım |
| Randevu hatırlatması | Orta | Bildirim servisi (e-posta/push) |
| 7/24 erişim | Orta | Cloud deployment + %99.9 uptime hedefi |

### 2.2 Doktor İhtiyaçları

| İhtiyaç | Öncelik | Karşılama Yöntemi |
|---------|---------|-------------------|
| Randevu taleplerini yönetme | Yüksek | Doktor dashboard + onay/red sistemi |
| Hasta tıbbi kayıtlarına erişim | Yüksek | Rol bazlı tıbbi kayıt görüntüleme |
| Reçete yazabilme | Yüksek | Reçete oluşturma modülü |
| Video görüşme yapabilme | Yüksek | Video konferans entegrasyonu |
| Çalışma saatleri belirleme | Orta | Müsaitlik takvimi |
| Hasta listesi görüntüleme | Orta | Doktorun hasta listesi endpoint'i |
| Görüşme sırasında not alma | Orta | Görüşme notları modülü |

### 2.3 Sistem Yöneticisi İhtiyaçları

| İhtiyaç | Öncelik | Karşılama Yöntemi |
|---------|---------|-------------------|
| Kullanıcı hesaplarını yönetme | Yüksek | Admin CRUD endpoint'leri |
| Doktor doğrulama/onaylama | Yüksek | Rol atama sistemi |
| Sistem verilerini izleme | Orta | Raporlama ve analiz modülü (Hafta 4) |
| Güvenlik kontrolü | Yüksek | OWASP testleri, audit log |
| Sistem performansını izleme | Orta | Performans metrikleri |

### 2.4 Proje Danışmanı İhtiyaçları

| İhtiyaç | Öncelik | Karşılama Yöntemi |
|---------|---------|-------------------|
| Proje ilerleme takibi | Yüksek | Haftalık ilerleme raporları (Projeakisi.md) |
| Teknik dokümantasyon | Yüksek | API dokümanı, veritabanı şeması, README |
| Çalışan demo/sunum | Yüksek | Swagger UI + çalışan backend |
| Test sonuçları | Orta | Test raporları (Hafta 5-6) |

### 2.5 Sağlık Personeli İhtiyaçları

| İhtiyaç | Öncelik | Karşılama Yöntemi |
|---------|---------|-------------------|
| Hasta bilgilerine erişim | Orta | Gelecekte STAFF rolü eklenebilir |
| Randevu takibi | Orta | Dashboard üzerinden izleme |
| Basit ve anlaşılır arayüz | Orta | Kullanıcı dostu UI tasarımı |

---

## 3. Paydaş Etki-İlgi Matrisi

```
                    ETKİ (Proje üzerindeki güç)
                    Düşük              Yüksek
               ┌──────────────┬──────────────────┐
    Yüksek     │  İZLE        │  YAKIN YÖNETİM   │
    İLGİ       │              │                  │
               │ Sağlık       │ Hasta            │
               │ Personeli    │ Doktor           │
               │              │ Proje Danışmanı  │
               ├──────────────┼──────────────────┤
    Düşük      │  MİNİMUM     │  BİLGİLENDİR     │
    İLGİ       │  ÇABA        │                  │
               │              │ Sağlık Kurumu    │
               │              │ Admin            │
               └──────────────┴──────────────────┘
```

---

## 4. İletişim Planı

### 4.1 Ekip İçi İletişim

| Kanal | Sıklık | Katılımcılar | Amaç |
|-------|--------|-------------|------|
| **WhatsApp Grubu** | Günlük | Tüm ekip | Anlık iletişim, soru-cevap |
| **GitHub Issues/PR** | Her commit | Tüm ekip | Kod inceleme, hata takibi |
| **Haftalık Toplantı** | Haftada 1 | Tüm ekip | Sprint planlaması, ilerleme değerlendirme |
| **Scrum Daily Standup** | Günlük (kısa) | Tüm ekip | Dün ne yaptım, bugün ne yapacağım, engeller |

### 4.2 Danışman İletişimi

| Kanal | Sıklık | Amaç |
|-------|--------|------|
| **Ders saati** | Haftada 1 | İlerleme sunumu, geri bildirim alma |
| **E-posta** | Gerektiğinde | Resmi bilgilendirme, doküman paylaşımı |
| **Demo sunumu** | Sprint sonları | Çalışan ürün gösterimi |

### 4.3 Paydaş Bilgilendirme Planı

| Paydaş | İletişim Yöntemi | Sıklık | İçerik |
|--------|-----------------|--------|--------|
| Hasta | Uygulama içi bildirim | Sürekli | Randevu durumu, hatırlatma |
| Doktor | Uygulama içi bildirim + E-posta | Sürekli | Yeni randevu talebi, hasta bilgisi |
| Admin | Dashboard raporları | Haftalık | Sistem metrikleri, kullanıcı istatistikleri |
| Proje Danışmanı | Projeakisi.md + Sunum | Haftalık | Sprint ilerleme raporu |
| Sağlık Kurumu | Rapor | Aylık | Sistem kullanım istatistikleri |

---

## 5. Ekip Üyeleri ve Sorumluluklar

| Üye | Rol | Ana Sorumluluk | İletişim Kanalı |
|-----|-----|---------------|-----------------|
| Halit Hacbekkur | Scrum Master | Proje yönetimi, koordinasyon | WhatsApp + GitHub |
| Nedim İsa | Gereksinim Analizi | Paydaş iletişimi, dokümantasyon | WhatsApp + E-posta |
| Zelal Ergin | Backend Geliştirme | API geliştirme, altyapı | GitHub + WhatsApp |
| Ahmet Akif Yılmaz | Veritabanı & Güvenlik | DB tasarımı, güvenlik testleri | GitHub + WhatsApp |
| Cena İsmail | Frontend Geliştirme | UI/UX, mobil uyumluluk | GitHub + WhatsApp |

---

## 6. Eskalasyon Prosedürü

```
Seviye 1: Ekip üyesi → WhatsApp grubunda paylaşım
Seviye 2: Çözülemezse → Scrum Master (Halit) devreye girer
Seviye 3: Kritik sorun → Proje Danışmanı'na bilgilendirme
```

---

## 7. Sonuç

Toplam **7 paydaş grubu** belirlenmiş, her birinin ihtiyaçları analiz edilmiş ve karşılama yöntemleri tanımlanmıştır. İletişim planı; ekip içi günlük iletişim, haftalık toplantılar ve danışman geri bildirimi olmak üzere üç katmanlı olarak yapılandırılmıştır.

---

*Hazırlayan: NEDİM İSA | Onaylayan: Halit Hacbekkur (Scrum Master)*

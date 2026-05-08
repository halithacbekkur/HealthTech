# 📅 Proje Takvimi — HealthTech Tele-Sağlık Platformu

**Hazırlayan:** Ahmet Akif Yılmaz
**Tarih:** Mayıs 2026
**Metodoloji:** Agile / Scrum (Haftalık Sprintler)

---

## 🗓️ Genel Zaman Çizelgesi

| Hafta | Tarih | Sprint Hedefi | Durum |
|---|---|---|---|
| Hafta 1 | 9–15 Mart | Proje kurulumu, gereksinim analizi | ✅ Tamamlandı |
| Hafta 2 | 16–22 Mart | Backend altyapısı, User API | ✅ Tamamlandı |
| Hafta 3 | 23–29 Mart | JWT güvenliği, API olgunlaştırma | ✅ Tamamlandı |
| Hafta 4 | 30 Mar–5 Nis | Doctor & Patient entity'leri | 🔄 Devam Ediyor |
| Hafta 5 | 6–12 Nis | Frontend (Angular), Video entegrasyonu | 🔄 Devam Ediyor |
| Hafta 6 | 13 Nis–8 May | Test, güvenlik analizi, UI prototipi | 🔄 Devam Ediyor |
| Hafta 7 | 9–15 May | Entegrasyon, son düzeltmeler | ⏳ Planlandı |
| Hafta 8 | 16–22 May | Final demo, dokümantasyon | ⏳ Planlandı |

---

## 📋 Sprint Detayları

### ✅ Hafta 1 — Proje Kurulumu (9–15 Mart)
**Hedef:** Temel altyapının kurulması

| Görev | Sorumlu | Durum |
|---|---|---|
| GitHub repository kurulumu | Halid | ✅ |
| Gereksinim analizi | Nedim | ✅ |
| Teknoloji araştırması | Ömer | ✅ |
| Geliştirme ortamı kurulumu | Zelal | ✅ |
| Veritabanı modeli tasarımı | Akif | ✅ |
| Sprint planlaması | Halid | ✅ |

---

### ✅ Hafta 2 — Backend Altyapısı (16–22 Mart)
**Hedef:** User CRUD ve Authentication

| Görev | Sorumlu | Durum |
|---|---|---|
| Spring Boot projesi kurulumu | Halid | ✅ |
| User entity ve CRUD | Halid | ✅ |
| Register / Login endpoint | Halid | ✅ |
| BCrypt şifreleme | Halid | ✅ |
| MySQL bağlantısı | Halid | ✅ |
| Postman ile API testi | Akif | ✅ |

---

### ✅ Hafta 3 — Güvenlik (23–29 Mart)
**Hedef:** JWT entegrasyonu, API olgunlaştırma

| Görev | Sorumlu | Durum |
|---|---|---|
| JWT token üretimi | Halid | ✅ |
| JwtAuthFilter | Halid | ✅ |
| /api/users/me endpoint | Halid | ✅ |
| DTO yapısı | Halid | ✅ |
| Global Exception Handler | Halid | ✅ |
| Validation kuralları | Halid | ✅ |

---

### 🔄 Hafta 4 — Hasta & Doktor Modülleri (30 Mar–5 Nis)
**Hedef:** Doctor, Patient, Appointment entity ve API'leri

| Görev | Sorumlu | Durum |
|---|---|---|
| Doctor entity ve CRUD | Halid | ⏳ |
| Patient entity ve CRUD | Halid | ⏳ |
| Appointment (Visit) entity | Halid | ⏳ |
| Prescription entity | Halid | ⏳ |
| Role-based access control | Halid | ⏳ |
| DB migration scripts | Akif | ⏳ |

---

### 🔄 Hafta 5 — Frontend & Video (6–12 Nis)
**Hedef:** Angular arayüzü, Jitsi entegrasyonu

| Görev | Sorumlu | Durum |
|---|---|---|
| Angular proje kurulumu | Cena | ⏳ |
| Login / Register sayfası | Cena | ⏳ |
| Hasta dashboard | Cena | ⏳ |
| Doktor dashboard | Cena | ⏳ |
| Video görüşme (Jitsi iframe) | Cena + Akif | ⏳ |
| Spring Boot video servisi | Halid | ⏳ |

---

### 🔄 Hafta 6 — Test & Güvenlik (13 Nis–8 Mayıs)
**Hedef:** Final testler, güvenlik analizi, UI prototipi

| Görev | Sorumlu | Durum |
|---|---|---|
| Video konferans araştırması | Akif | ✅ |
| Final test raporu | Akif | ✅ |
| OWASP güvenlik analizi | Akif | ✅ |
| UI prototipi (HTML/CSS) | Akif | ✅ |
| Proje takvimi | Akif | ✅ |
| Kritik bug düzeltmeleri | Halid | ⏳ |

---

### ⏳ Hafta 7 — Entegrasyon (9–15 Mayıs)
**Hedef:** Tüm modüllerin birleştirilmesi

| Görev | Sorumlu | Tahmini Süre |
|---|---|---|
| Backend-Frontend entegrasyonu | Halid + Cena | 3 gün |
| JWT - Angular entegrasyonu | Cena | 1 gün |
| Video görüşme tam entegrasyon | Cena + Halid | 2 gün |
| E2E test | Akif | 1 gün |
| Bug düzeltme | Tüm ekip | 2 gün |

---

### ⏳ Hafta 8 — Final (16–22 Mayıs)
**Hedef:** Demo hazırlığı, dokümantasyon

| Görev | Sorumlu | Tahmini Süre |
|---|---|---|
| Final demo hazırlığı | Tüm ekip | 2 gün |
| Teknik dokümantasyon | Nedim + Akif | 2 gün |
| README güncelleme | Halid | 1 gün |
| Sunum hazırlama | Tüm ekip | 2 gün |
| Son testler | Akif | 1 gün |

---

## ⚠️ Riskler ve Önlemler

| Risk | Olasılık | Önlem |
|---|---|---|
| Doctor/Patient entity gecikmesi | Orta | Hafta 7'de önceliklendir |
| Frontend entegrasyon sorunu | Yüksek | Hafta 7 başında erken test |
| Video görüşme teknik sorunu | Orta | Jitsi iframe ile hızlı prototip |
| Final demo'ya yetişememe | Düşük | Hafta 7'de buffer süre bırakıldı |

---

## 👥 Ekip Görev Özeti

| Kişi | Rol | Tamamlanan | Bekleyen |
|---|---|---|---|
| Halid Hacbekkur | Scrum Master + Backend | 3 Sprint | 2 Sprint |
| Nedim İsa | Gereksinim | Hafta 1 | Dokümantasyon |
| Ömer Doğan | Teknoloji Araştırma | Hafta 1 | — |
| Zelal Ergin | DevOps | Hafta 1 | Self-hosted server |
| Ahmet Akif Yılmaz | DB + Test + Güvenlik | Hafta 1, 6 | Hafta 7 E2E test |
| Cena İsmail | Frontend | — | Hafta 5, 7 |

---

**Kaynak:** Projeakisi.md ve sprint board verileri
**Son Güncelleme:** Mayıs 2026

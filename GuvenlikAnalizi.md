# 🔐 Güvenlik Analizi Raporu — OWASP Top 10

**Proje:** HealthTech Tele-Sağlık Platformu
**Hazırlayan:** Ahmet Akif Yılmaz
**Görev:** Güvenlik Analizi ve OWASP Test Uygulaması
**Tarih:** Mayıs 2026
**Test Edilen Sürüm:** feature/ahmetakif branch (3. Hafta sonu)

---

## 📊 Özet

| OWASP Kategori | Durum | Kritiklik |
|---|---|---|
| A01 — Broken Access Control | ⚠️ AÇIK | 🔴 Kritik |
| A02 — Cryptographic Failures | ⚠️ AÇIK | 🔴 Kritik |
| A03 — Injection | ✅ Güvenli | 🟢 Düşük |
| A04 — Insecure Design | ⚠️ AÇIK | 🟠 Orta |
| A05 — Security Misconfiguration | ⚠️ AÇIK | 🔴 Kritik |
| A06 — Vulnerable Components | ✅ Güvenli | 🟢 Düşük |
| A07 — Auth Failures | ⚠️ AÇIK | 🔴 Kritik |
| A08 — Software Integrity Failures | ✅ Güvenli | 🟢 Düşük |
| A09 — Logging Failures | ⚠️ AÇIK | 🟠 Orta |
| A10 — SSRF | ✅ Güvenli | 🟢 Düşük |

**Toplam:** 5 kategoride açık tespit edildi, 5 kategoride güvenli.

---

## 🔴 A01 — Broken Access Control (Kırık Erişim Kontrolü)

### Tanım
Kullanıcıların yetkisi olmayan kaynaklara erişebildiği durumlardır.

### Tespit Edilen Açık

**SecurityConfig.java** dosyasında tüm endpoint'ler herkese açık bırakılmış:

```java
.authorizeHttpRequests(auth -> auth
    .anyRequest().permitAll()  // ← HERKESİ SERBEST BIRAKIYOR
);
```

### Test Adımı
```
GET http://localhost:8080/api/users
Authorization: (yok)

Beklenen: 401 Unauthorized
Gerçekleşen: 200 OK + tüm kullanıcı listesi
```

### Etki
Token olmadan herhangi biri tüm kullanıcı listesine, hasta bilgilerine ve doktor verilerine erişebilir.

### Çözüm
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()
    .anyRequest().authenticated()
)
.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
```

---

## 🔴 A02 — Cryptographic Failures (Kriptografik Hatalar)

### Tanım
Hassas verilerin yetersiz şifreleme veya açık metin ile saklanması/iletilmesidir.

### Tespit Edilen Açık 1: DB Şifresi Açık Metin

**application.properties** dosyasında:
```properties
spring.datasource.password=ZorZorZor2006.
```
Bu dosya public GitHub repository'de yer alıyor. **Herkes görebilir.**

### Tespit Edilen Açık 2: JWT Secret Hardcoded

**JwtService.java** dosyasında:
```java
private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";
```
JWT secret kaynak kodda sabit. Saldırgan bu secret ile sahte token üretebilir ve sisteme admin olarak girebilir.

### Tespit Edilen Açık 3: Register Şifre Hash'i Sızıyor

**AuthController.java** dosyasında register endpoint'i tüm User nesnesini döndürüyor — BCrypt hash dahil.

### Etki
- DB şifresi: Yetkisiz veritabanı erişimi, tüm sağlık verilerinin çalınması
- JWT secret: Sahte token üretimi, kimlik sahteciliği
- Hash sızıntısı: Offline brute-force saldırısı riski

### Çözüm
```properties
# application.properties
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```
```java
// JwtService.java
@Value("${jwt.secret}")
private String SECRET;
```
```java
// AuthController.java — DTO döndür
public UserResponseDTO register(@Valid @RequestBody User request) { ... }
```

---

## 🟢 A03 — Injection (Enjeksiyon)

### Tanım
SQL, NoSQL, komut enjeksiyonu gibi saldırılardır.

### Test Sonucu: GÜVENLİ ✅

Proje Spring Data JPA ve parametreli sorgular kullanıyor:
```java
Optional<User> findByEmail(String email);  // JPA — SQL injection güvenli
```
Ham SQL sorgusu (`@NativeQuery` veya `Statement`) kullanılmadığı için SQL injection riski bulunmuyor.

---

## 🟠 A04 — Insecure Design (Güvensiz Tasarım)

### Tanım
Güvenlik gözetilmeden tasarlanmış mimari ve iş mantığı hataları.

### Tespit Edilen Açık 1: Rate Limiting Yok

Login endpoint'inde deneme sınırı yok. Saldırgan binlerce farklı şifreyle deneme yapabilir (brute-force).

```
POST /api/auth/login  →  sınırsız deneme yapılabilir
```

### Tespit Edilen Açık 2: JWT Token'da Role Yok

Token sadece email içeriyor, rol bilgisi yok:
```java
Jwts.builder().subject(email)  // role yok!
```
Bu durumda gelecekte RBAC (rol tabanlı erişim) uygulanamaz.

### Tespit Edilen Açık 3: Duplicate Email Kontrolü Yok

Aynı email ile iki kez kayıt denenince uygulama 500 Internal Server Error veriyor. Bilgi sızıntısına yol açabilir.

### Çözüm Önerileri
- Bucket4j veya Spring Rate Limiter ile login denemesi sınırla (örn. 5 deneme / dakika)
- JWT claim'lerine `role` ve `userId` ekle
- Email duplicate kontrolünü kod seviyesinde yap, `DataIntegrityViolationException` yakala

---

## 🔴 A05 — Security Misconfiguration (Güvenlik Yanlış Yapılandırması)

### Tanım
Güvenlik ayarlarının yanlış veya eksik yapılandırılmasıdır.

### Tespit Edilen Açık 1: Spring Security Devre Dışı

Yukarıda A01'de detaylandırıldı. `anyRequest().permitAll()` tüm güvenliği devre dışı bırakıyor.

### Tespit Edilen Açık 2: JwtAuthFilter SecurityContext'e Bağlı Değil

**JwtAuthFilter.java** token'ı doğruluyor ama `SecurityContextHolder`'a authentication eklenmiyor:

```java
// Bu satır eksik:
SecurityContextHolder.getContext().setAuthentication(auth);
```

Bu yüzden `@PreAuthorize("hasRole('ADMIN')")` gibi annotationlar çalışmaz.

### Tespit Edilen Açık 3: CSRF Tamamen Kapatılmış

```java
.csrf(csrf -> csrf.disable())
```
REST API için kabul edilebilir ama stateless JWT ile birlikte düzgün dökümante edilmeli.

### Çözüm
```java
// SecurityConfig.java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

---

## 🟢 A06 — Vulnerable and Outdated Components (Savunmasız Bileşenler)

### Test Sonucu: GÜVENLİ ✅

`pom.xml` incelendi:
- Spring Boot: 4.0.6 (güncel)
- jjwt: 0.12.6 (güncel, eski 0.9.x değil)
- BCrypt: Spring Security dahili (güncel)

Bilinen CVE (güvenlik açığı) içeren bağımlılık tespit edilmedi.

---

## 🔴 A07 — Identification and Authentication Failures (Kimlik Doğrulama Hataları)

### Tanım
Kimlik doğrulama mekanizmalarının hatalı uygulanması.

### Tespit Edilen Açık 1: Hatalı Şifrede 500 Hata

```java
throw new RuntimeException("Şifre yanlış");  // 500 dönüyor
```
Standart davranış 401 Unauthorized olmalı. 500 hatası sistem iç yapısı hakkında bilgi sızdırabilir.

### Tespit Edilen Açık 2: /api/users/me NullPointerException

Token gönderilmezse:
```java
String token = authHeader.substring(7);  // authHeader null → NPE → 500
```

### Tespit Edilen Açık 3: POST /api/users Şifre Hash'lemiyor

`UserController.createUser()` şifreyi düz metin olarak kaydediyor. Aynı kullanıcı login olamaz.

### Test Adımları
```
# Test 1 — Yanlış şifre
POST /api/auth/login
{ "email": "test@test.com", "password": "yanlis" }
Beklenen: 401 | Gerçekleşen: 500

# Test 2 — Token olmadan /me
GET /api/users/me
Authorization: (yok)
Beklenen: 401 | Gerçekleşen: 500 NullPointerException
```

---

## 🟠 A09 — Security Logging and Monitoring Failures (Loglama Hataları)

### Tanım
Güvenlik olaylarının loglanmaması veya izlenememesi.

### Tespit Edilen Açık

Sistemde güvenlik olayları için loglama yok:
- Başarısız login denemeleri loglanmıyor
- Token doğrulama hatalarında sadece `System.out.println` kullanılıyor:

```java
// JwtAuthFilter.java
System.out.println("Token geçerli, kullanıcı: " + email);  // production'da olmamalı
```

- SLF4J / Logback ile yapılandırılmış loglama yok
- Güvenlik olayları için alert mekanizması yok

### Çözüm Önerisi
```java
private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

log.warn("Başarısız giriş denemesi: {}", email);
log.info("Başarılı giriş: {}", email);
```

---

## 🟢 A10 — SSRF (Server-Side Request Forgery)

### Test Sonucu: GÜVENLİ ✅

Mevcut kodda dış URL'lere istek yapan bir yapı bulunmuyor. Video konferans entegrasyonu henüz eklenmediği için SSRF riski yok. Jitsi entegrasyonu eklendiğinde bu madde tekrar değerlendirilmeli.

---

## 📋 Genel Değerlendirme ve Öncelik Sırası

### Acil (Bu Sprint)
1. DB şifresi değiştirilmeli ve `.gitignore`'a eklenmeli
2. Spring Security konfigürasyonu düzeltilmeli
3. JWT secret environment variable'a taşınmalı

### Kısa Vade (Sonraki Sprint)
4. Exception handling düzeltilmeli (401 vs 500)
5. Rate limiting eklenmeli
6. JWT token'a role eklenmeli
7. Loglama altyapısı kurulmalı

### Orta Vade
8. RBAC tam implementasyonu
9. 2FA desteği (Projeakisi.md'de belirtilmiş)
10. Video entegrasyonu sonrası SSRF kontrolü

---

## 🛠️ Test Araçları ve Yöntemler

| Yöntem | Araç | Kapsam |
|---|---|---|
| Statik Kod Analizi | Manuel inceleme | Tüm Java dosyaları |
| API Güvenlik Testi | Postman | 7 endpoint, 11 senaryo |
| Bağımlılık Taraması | pom.xml incelemesi | 8 bağımlılık |
| OWASP Kontrol Listesi | OWASP Top 10 2021 | 10 kategori |

---

**Hazırlayan:** Ahmet Akif Yılmaz
**Kaynak:** OWASP Top 10 2021 — https://owasp.org/Top10/

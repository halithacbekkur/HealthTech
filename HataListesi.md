# 🐛 Test Raporu — Hata ve İyileştirme Listesi

**Test Eden:** Ahmet Akif Yılmaz
**Tarih:** Mayıs 2026
**Test Edilen Sürüm:** main branch (3. Hafta sonu)
**Test Yöntemi:** Kod incelemesi (static analysis) + Postman entegrasyon testi + JUnit birim testi

---

## 📊 Özet

| Seviye | Adet | Açıklama |
|---|---|---|
| 🔴 Kritik | 4 | Acil müdahale gereken güvenlik/işlevsellik sorunları |
| 🟠 Orta | 5 | Düzeltilmesi gereken işlevsel hatalar |
| 🟡 Düşük | 4 | İyileştirme önerileri |
| **TOPLAM** | **13** | |

---

## 🔴 KRİTİK Seviye

### BUG-001: Veritabanı Şifresi Repository'de Açıkta

**Dosya:** `src/main/resources/application.properties`
**Satır:** 5

**Sorun:**
```properties
spring.datasource.password=ZorZorZor2006.
```

DB şifresi düz metin olarak public GitHub repository'de yer alıyor. Herhangi biri bu şifreyle veritabanına erişebilir.

**Etki:** Veri sızıntısı, yetkisiz erişim, KVKK ihlali

**Çözüm Önerisi:**
1. Şifreyi hemen değiştir (mevcut şifre artık güvenli değil — internette herkes görmüş olabilir)
2. `application.properties` dosyasını `.gitignore`'a ekle
3. Şifreyi environment variable olarak kullan:
```properties
spring.datasource.password=${DB_PASSWORD}
```
4. Geliştirme için `application-dev.properties` (gitignore'lu) kullanın
5. Git geçmişinden eski şifreyi tamamen silmek için `git-filter-repo` veya BFG Repo-Cleaner kullanılabilir

---

### BUG-002: Spring Security Tüm Endpoint'leri Açık Bırakıyor

**Dosya:** `src/main/java/com/healthtech/telehealth/security/SecurityConfig.java`
**Satır:** 16

**Sorun:**
```java
.authorizeHttpRequests(auth -> auth
    .anyRequest().permitAll()
);
```

JWT yazılmış ama Spring Security her isteğe izin veriyor. Token olmadan da `GET /api/users` çalışıyor.

**Test Adımı:**
```bash
curl http://localhost:8080/api/users
# Beklenen: 401 Unauthorized
# Gerçekleşen: 200 OK + tüm kullanıcı listesi döner
```

**Etki:** Tüm korumalı endpoint'ler herkese açık. Authentication sistemi pratikte çalışmıyor.

**Çözüm Önerisi:**
```java
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

### BUG-003: JWT Secret Kod İçinde Sabit (Hardcoded)

**Dosya:** `src/main/java/com/healthtech/telehealth/service/JwtService.java`
**Satır:** 13

**Sorun:**
```java
private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";
```

JWT secret kaynak kodda yer alıyor. GitHub'da public olduğu için herkes görebilir, sahte token üretebilir.

**Etki:** Saldırgan kendi JWT token'larını üretip sisteme yönetici olarak girebilir.

**Çözüm Önerisi:**
```java
@Value("${jwt.secret}")
private String SECRET;
```

`application.properties`:
```properties
jwt.secret=${JWT_SECRET}
```

Çalıştırırken environment variable olarak verilir.

---

### BUG-004: Register Endpoint'i Hash'lenmiş Şifreyi Geri Döndürüyor

**Dosya:** `src/main/java/com/healthtech/telehealth/controller/AuthController.java`
**Satır:** 42-46

**Sorun:**
```java
@PostMapping("/register")
public User register(@Valid @RequestBody User request){
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    return userRepository.save(request);
}
```

Endpoint, kayıt sonrası tüm `User` nesnesini döndürüyor — **password (hash bile olsa) dahil**. Hash bile olsa şifre alanları API yanıtında olmamalı.

**Etki:** Bilgi sızıntısı. Hash'leri elde eden saldırgan offline brute-force saldırısı yapabilir.

**Çözüm Önerisi:**
```java
@PostMapping("/register")
public UserResponseDTO register(@Valid @RequestBody User request){
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    User saved = userRepository.save(request);
    return new UserResponseDTO(saved.getId(), saved.getFullName(),
                               saved.getEmail(), saved.getPhone(), saved.getRole());
}
```

---

## 🟠 ORTA Seviye

### BUG-005: Login'de Hatalı Şifrede 500 Internal Server Error

**Dosya:** `AuthController.java`
**Satır:** 31-34

**Sorun:**
```java
if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    throw new RuntimeException("Şifre yanlış");
}
```

Generic `RuntimeException` `GlobalExceptionHandler` tarafından yakalanmıyor → kullanıcı `500 Internal Server Error` alır. Standart `401 Unauthorized` olmalı.

**Test Adımı:**
```
POST /api/auth/login
{ "email": "test@test.com", "password": "yanlis" }

Beklenen: HTTP 401
Gerçekleşen: HTTP 500
```

**Çözüm Önerisi:** Özel exception oluştur ve handler ekle:
```java
public class InvalidCredentialsException extends RuntimeException { ... }

@ExceptionHandler(InvalidCredentialsException.class)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public Map<String, Object> handleInvalidCredentials(...) { ... }
```

---

### BUG-006: `/api/users/me` Token Olmadan NullPointerException Atar

**Dosya:** `UserController.java`
**Satır:** 50-55

**Sorun:**
```java
String authHeader = request.getHeader("Authorization");
String token = authHeader.substring(7);  // null kontrolü yok!
```

`Authorization` header gönderilmezse `authHeader` null olur, `null.substring()` NullPointerException atar → 500 hata döner.

**Test Adımı:**
```
GET /api/users/me  (header olmadan)
Beklenen: 401 Unauthorized
Gerçekleşen: 500 NullPointerException
```

**Çözüm Önerisi:**
```java
if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    throw new UnauthorizedException("Token gerekli");
}
```

---

### BUG-007: `POST /api/users` Şifreyi Düz Metin Olarak Kaydediyor

**Dosya:** `UserController.java` + `UserService.java`
**Satır:** 32 (controller) + 37 (service)

**Sorun:**
```java
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.createUser(user);  // şifre hash'lenmiyor!
}

public User createUser(User user) {
    return userRepository.save(user);  // direkt kaydediyor
}
```

`AuthController.register()` BCrypt ile hash'liyor ama `UserController.createUser()` hash'lemiyor. Aynı işi yapan iki endpoint farklı davranıyor.

**Etki:** Bu endpoint'le oluşturulan kullanıcılar şifrelerini düz metin olarak DB'ye yazıyor. Aynı kullanıcı sonra login olamaz çünkü hash karşılaştırması başarısız olur.

**Çözüm Önerisi:** `UserService.createUser` içinde hash'leme yapılmalı veya bu endpoint kaldırılmalı (zaten `register` var).

---

### BUG-008: Update Endpoint'i Rol Değişikliğine İzin Veriyor

**Dosya:** `UserService.java`
**Satır:** 53-71

**Sorun:** `updateUser` metodu role alanını güncellememekle birlikte kullanıcı request'inde rolü gönderirse — endpoint'in tasarımı incelenmeli. Mevcut kodda role set edilmediği için **role bilgisi kayboluyor** kullanıcı güncellendiğinde (PATIENT iken NULL olabilir).

**Test Adımı:**
```
PUT /api/users/1
{ "fullName": "Yeni İsim", "email": "yeni@mail.com", "phone": "555..." }

Beklenen: Diğer alanlar korunmalı
Gerçekleşen: role NULL olur (DB'de @NotNull olduğu için kayıt başarısız olabilir)
```

**Çözüm Önerisi:** Role alanı opsiyonel update olarak gelirse mevcut role korunmalı. Sadece ADMIN role değiştirebilmeli (yetkilendirme).

---

### BUG-009: Aynı Email ile Çift Kayıt → 500 Hata

**Dosya:** `AuthController.java`
**Satır:** 42-46

**Sorun:** `@Column(unique = true)` var ama duplicate email kontrolü kod içinde yok. Aynı email ile register denenirse Hibernate `DataIntegrityViolationException` atar → kullanıcı **500 Internal Server Error** görür.

**Test Adımı:**
```
POST /api/auth/register  (aynı email iki kez)
Beklenen: 409 Conflict + "Bu email zaten kullanılıyor" mesajı
Gerçekleşen: 500 Internal Server Error
```

**Çözüm Önerisi:**
```java
if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    throw new EmailAlreadyExistsException("Bu email zaten kayıtlı");
}
```

---

## 🟡 DÜŞÜK Seviye (İyileştirme Önerileri)

### BUG-010: `User.createdAt` Hiç Doldurulmuyor

**Dosya:** `User.java`
**Satır:** 32

`createdAt` alanı tanımlı ama register sırasında set edilmiyor → DB'de NULL kalıyor.

**Çözüm Önerisi:**
```java
@PrePersist
protected void onCreate() {
    this.createdAt = LocalDateTime.now();
}
```

---

### BUG-011: JWT Token'da Sadece Email Var

**Dosya:** `JwtService.java`
**Satır:** 19-26

Token claim'lerinde sadece subject (email) var. Role-based access control için **role** ve **userId** de claim olarak eklenmeli.

**Çözüm Önerisi:**
```java
public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("role", user.getRole())
        .claim("userId", user.getId())
        ...
}
```

---

### BUG-012: Login Endpoint'inde Rate Limiting Yok

**Dosya:** `AuthController.java`

Brute-force saldırılarına açık. Bucket4j veya Spring Security rate limiter eklenmeli.

---

### BUG-013: JwtAuthFilter Spring Security Context'ine Authentication Eklemiyor

**Dosya:** `JwtAuthFilter.java`

Filter token'ı doğruluyor ama `SecurityContextHolder`'a Authentication objesi eklemiyor. Bu yüzden `@PreAuthorize` gibi annotationlar çalışmaz.

**Çözüm Önerisi:**
```java
UsernamePasswordAuthenticationToken auth =
    new UsernamePasswordAuthenticationToken(email, null, authorities);
SecurityContextHolder.getContext().setAuthentication(auth);
```

---

## ✅ Test Edilemeyenler / Sonraki Sprint İçin

Bu sürümde aşağıdaki bileşenler **henüz mevcut olmadığı için** test edilememiştir:

- ❌ Doctor entity ve endpoint'leri (DB modelinde var, kodda yok)
- ❌ Patient entity ve endpoint'leri
- ❌ Visit (Appointment) entity ve endpoint'leri
- ❌ Prescription entity
- ❌ Frontend (Angular) — repoda kod yok
- ❌ Video görüşme entegrasyonu (gelecek sprint görevi)

---

## 📋 Test Sonucu Özeti

**Test Edilen Endpoint Sayısı:** 7
- POST /api/auth/register
- POST /api/auth/login
- GET /api/users
- GET /api/users/{id}
- POST /api/users
- PUT /api/users/{id}
- DELETE /api/users/{id}
- GET /api/users/me

**Geçen Test:** 3 / 11 (mutlu yol senaryoları)
**Başarısız Test:** 8 / 11 (hata yönetimi ve güvenlik testleri)

**Genel Değerlendirme:**
3 haftalık geliştirme sonucunda backend'in temel iskeleti başarıyla kurulmuştur. JPA, Lombok, JWT gibi modern teknolojiler doğru şekilde entegre edilmiştir. Ancak **Spring Security konfigürasyonu**, **exception handling** ve **gizli bilgi yönetimi** alanlarında kritik iyileştirmeler gereklidir. Yukarıdaki BUG-001, BUG-002 ve BUG-003 maddeleri sonraki sprint'te öncelikli olarak ele alınmalıdır.

---

**Hazırlayan:** Ahmet Akif Yılmaz
**Görev:** Final Test ve Hata Tespiti
**Sprint:** Hafta 6

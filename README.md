<h1 align="center">🏥 Tele-Sağlık Platformu</h1>
<h3 align="center">Digital HealthTech Solution for Remote Healthcare</h3>

<p align="center">
<img src="https://img.shields.io/badge/Status-In%20Development-blue"/>
<img src="https://img.shields.io/badge/Backend-Java%20SpringBoot-red"/>
<img src="https://img.shields.io/badge/Frontend-Angular-dd0031"/>
<img src="https://img.shields.io/badge/Database-MySQL-blue"/>
<img src="https://img.shields.io/badge/Architecture-Layered-green"/>
<img src="https://img.shields.io/badge/License-Educational-lightgrey"/>
</p>

---

## 📌 Proje Hakkında

Tele-Sağlık Platformu, hasta ve doktor arasındaki iletişimi dijital ortama taşıyan modern bir sağlık teknolojisi çözümüdür.
Platform sayesinde kullanıcılar online randevu oluşturabilir, doktor ile görüntülü görüşme yapabilir ve sağlık geçmişlerini güvenli şekilde takip edebilir.

Bu proje ekip çalışması, Agile / Scrum metodolojisi ve katmanlı yazılım mimarisi prensipleri doğrultusunda geliştirilmektedir.

---

## ✨ Platform Özellikleri

* 📅 Online randevu oluşturma ve yönetme
* 🎥 Gerçek zamanlı görüntülü doktor görüşmesi
* 📂 Dijital sağlık kayıtlarının takibi
* 🔐 Güvenli kimlik doğrulama sistemi
* 👥 Rol bazlı kullanıcı yönetimi (Hasta / Doktor / Admin)
* 📊 Sağlık geçmişi görüntüleme

---

## 🧠 Yazılım Mimarisi

Proje **Layered Architecture** yapısı ile geliştirilmektedir.

* Presentation Layer
* Business Logic Layer
* Data Access Layer
* Database Layer

---

## 🎥 Video Consultation Architecture

```mermaid
flowchart TD
    A[👤 Hasta] --> B[🌐 Angular Frontend]
    C[👨‍⚕️ Doktor] --> B

    B --> D[🔐 Authentication Service]
    B --> E[📅 Appointment Service]
    B --> F[🎥 Video Call Service]

    D --> G[(👥 User Database)]
    E --> H[(📂 Medical Records Database)]
    E --> I[(📅 Appointment Database)]

    F --> J[🔄 WebRTC / Signaling Server]
    J --> K[📹 Real-Time Video Communication]

    B --> L[📨 Notification Service]
    L --> M[✉️ Email / SMS Notifications]

    E --> N[🩺 Doctor Panel]
    E --> O[🧾 Patient Panel]
```

---

## 👥 Proje Ekibi

| İsim              | Rol                               |
| ----------------- | --------------------------------- |
| Halid Hacbekkur   | Scrum Master & Project Management |
| Nedim İsa         | Requirements Analysis             |
| Ömer Doğan        | Technology Research               |
| Zelal Ergin       | Development Environment           |
| Ahmet Akif Yılmaz | Database Design                   |
| Cena İsmail       | Frontend Development              |

---

## 📈 Sprint Progress

Sprint 1

████████░░ **80%**

Sprint 2

██░░░░░░░░ **20%**

---

## 🗺️ Project Roadmap

* ✅ Teknoloji seçimi
* ✅ Geliştirme ortamı kurulumu
* 🔄 Backend API geliştirme
* 🔄 Frontend ekran geliştirme
* ⏳ Authentication sistemi
* ⏳ Video görüşme entegrasyonu
* ⏳ Mobil uyumluluk

---

## 📊 GitHub Stats

<p align="center">
<img src="https://github-readme-stats.vercel.app/api?username=halithacbekkur&show_icons=true&theme=tokyonight"/>
<img src="https://github-readme-streak-stats.herokuapp.com/?user=halithacbekkur&theme=tokyonight"/>
</p>

---

## 📈 Activity Graph

<p align="center">
<img src="https://github-readme-activity-graph.vercel.app/graph?username=halithacbekkur&theme=tokyo-night"/>
</p>

---

## 🐍 Contribution Snake

<p align="center">
<img src="https://raw.githubusercontent.com/platane/snk/output/github-contribution-grid-snake.svg"/>
</p>

---

## 📁 Project Structure

```
tele-saglik-platformu
│
├── backend
├── frontend
├── database
└── docs
```

---

## 🚀 Installation

```
git clone https://github.com/organizasyon/tele-saglik-platformu.git
```

```
cd backend
mvn spring-boot:run
```

```
cd frontend
npm install
ng serve
```

---

## 🤝 Contributors

Halid Hacbekkur
Nedim İsa
Ömer Doğan
Zelal Ergin
Ahmet Akif Yılmaz
Cena İsmail

---

## 📜 License

Educational Project

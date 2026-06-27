## TaskFlow Java Desktop Application

> Aplikasi manajemen tugas berbasis Java Desktop (JavaFX) dengan pipeline CI/CD menggunakan GitHub Actions.

---
## Deskripsi Proyek

TaskFlow adalah aplikasi desktop untuk manajemen tugas yang dibangun menggunakan Java 17 dan JavaFX 21. Aplikasi ini memungkinkan pengguna untuk mengelola tugas melalui antarmuka grafis yang meliputi halaman login, dashboard, daftar task, dan form input task.

## Arsitektur Pipeline CI/CD

Pipeline CI/CD proyek ini menggunakan GitHub Actions dengan empat tahap utama yang dikerjakan secara paralel oleh masing-masing anggota tim di branch masing-masing:

```
Developer (branch masing-masing)
        │
        ▼
┌───────────────────────────────────────────────────┐
│                  GitHub Actions                   │
│                                                   │
│  ┌─────────────┐   ┌──────────────┐               │
│  │ Continuous  │   │  Continuous  │               │
│  │ Integration │   │   Testing    │               │
│  │  (CI)       │   │   (CT)       │               │
│  └─────────────┘   └──────────────┘               │
│                                                   │
│  ┌─────────────┐   ┌──────────────┐               │
│  │ Continuous  │   │  Continuous  │               │
│  │ Inspection  │   │ Deployment   │               │
│  │  (Code QA)  │   │   (CD)       │               │
│  └─────────────┘   └──────────────┘               │
└───────────────────────────────────────────────────┘
        │
        ▼
   Main Branch (Merge)
```

Setiap push ke branch anggota akan memicu pipeline secara otomatis. Pipeline dikonfigurasi untuk gagal (fail) secara otomatis apabila terdapat test yang tidak lulus, sehingga kode yang bermasalah tidak dapat masuk ke branch utama.

---

## Pembagian Tugas Anggota Kelompok

| No | Nama | Peran CI/CD | Komponen yang Dibangun | Branch |
|----|------|-------------|------------------------|--------|
| 1 | Deswita Syaharani | Continuous Integration | Halaman Dashboard | `dashboard` |
| 2 | Nadya Aulia Salma | Continuous Deployment | Halaman Login | `login` |
| 3 | Aisyah | Continuous Inspection | Halaman Task | `task` |
| 4 | Apriliani | Continuous Testing | Halaman TaskForm | `taskform` |

Keterangan peran:
- Continuous Integration — Mengintegrasikan perubahan kode secara otomatis dan memastikan build berhasil setiap kali ada push ke repository.
- Continuous Deployment — Mengotomatiskan proses deployment aplikasi setelah pipeline berhasil dijalankan.
- Continuous Inspection — Melakukan analisis kualitas kode secara otomatis menggunakan tools seperti SonarCloud.
- Continuous Testing — Menjalankan pengujian otomatis (unit test) sebagai bagian dari pipeline dan memastikan pipeline gagal apabila terdapat test yang tidak lulus.

---

## Tools dan Teknologi

| Tahap Pipeline | Tools / Teknologi | Keterangan |
|----------------|-------------------|------------|
| Build | Apache Maven 3.x | Build automation dan dependency management |
| Bahasa Pemrograman| Java 17 (Temurin) | Bahasa utama pengembangan |
| Framework UI | JavaFX 21.0.2 | Antarmuka grafis desktop |
| Unit Testing | JUnit Jupiter 5.10 | Framework pengujian otomatis |
| CI/CD Platform | GitHub Actions | Orkestrasi pipeline otomatis |
| Version Control | Git + GitHub | Manajemen versi dan kolaborasi tim |
| Laporan Test | Surefire XML Reports | Laporan hasil test (tersedia di tab Actions) |
| Code Inspection | Checkstyle / SpotBugs | Analisis kualitas kode (tahap Continuous Inspection) |

---

## Panduan Menjalankan Proyek Secara Lokal

## Prasyarat
Pastikan sudah terinstal:
- JDK 17 atau lebih baru ([Download Temurin](https://adoptium.net/))
- Apache Maven 3.8+ ([Download Maven](https://maven.apache.org/download.cgi))
- Git

Verifikasi instalasi:
```bash
java -version      # harus menampilkan Java 17.x.x
mvn -version       # harus menampilkan Apache Maven 3.x.x
```

### Langkah Menjalankan

1. Clone repository
```bash
git clone https://github.com/aprilianice/TaskFlowJavaDesktop.git
cd TaskFlowJavaDesktop
```

2. Pindah ke branch yang diinginkan *(opsional)*
```bash
git checkout taskform     # untuk melihat fitur TaskForm
git checkout dashboard    # untuk melihat fitur Dashboard
git checkout login        # untuk melihat fitur Login
git checkout task         # untuk melihat fitur Task
```

3. Build proyek
```bash
mvn clean install -DskipTests
```

4. Menjalankan aplikasi
```bash
mvn javafx:run
```

Atau jalankan melalui JAR:
```bash
mvn clean package -DskipTests
java -jar target/TaskFlow-1.1-SNAPSHOT.jar
```

5. Menjalankan unit test
```bash
mvn test
```

Laporan hasil test akan tersedia di:
```
target/surefire-reports/
```

---

## Pipeline GitHub Actions

Pipeline CI/CD berjalan otomatis setiap kali terdapat push atau pull request ke branch masing-masing anggota. Konfigurasi workflow tersimpan di direktori:

```
.github/workflows/
├── Dashboard.yml                # Continuous Integration (Deswita)
├── Login.yml                    # Continuous Deployment (Nadya)
├── task.yml                     # Continuous Inspection (Aisyah)
└── taskform.yml                 # Continuous Testing (Apriliani)
```

Hasil pipeline dapat dilihat melalui tab Actions di halaman GitHub repository.

> Pipeline dikonfigurasi untuk otomatis gagal apabila terdapat unit test yang tidak lulus (`mvn test` mengembalikan exit code ≠ 0).

---

## Struktur Proyek

```
TaskFlowJavaDesktop/
├── .github/
│   └── workflows/          # Konfigurasi GitHub Actions
├── src/
│   ├── main/
│   │   ├── java/           # Source code utama
│   │   └── resources/      # FXML, CSS, assets
│   └── test/
│       └── java/           # Unit test
├── target/                 # Hasil build (generated)
├── pom.xml                 # Konfigurasi Maven
└── README.md
```

---

*Proyek ini dibuat untuk memenuhi tugas mata kuliah Manajemen Konfigurasi dan Evaluasi Perangkat Lunak — Universitas Telkom.*

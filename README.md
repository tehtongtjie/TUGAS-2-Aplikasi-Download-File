# Aplikasi Download File

Tugas 2 mata kuliah Pemrograman Internet. Program `DownloadFile` dari Modul 2 (URL & URLConnection) dimodifikasi dengan menambahkan GUI berbasis Java Swing.

## Fitur

- GUI untuk memasukkan URL file dan memilih folder tujuan
- Tombol Download untuk memulai proses download
- Progress bar yang menunjukkan perkembangan download
- Pop-up pemberitahuan saat download selesai
- Pengecekan otomatis apakah file yang didownload adalah ZIP
- Pilihan untuk mengekstrak atau tidak mengekstrak file ZIP
- Ekstrak ZIP ke folder baru bernama sama dengan file ZIP

## Struktur Project

```
src/
├── Main.java             Titik awal program
├── DownloadFileGUI.java  Tampilan aplikasi (Swing)
├── DownloadTask.java     Proses download di thread terpisah (SwingWorker)
└── ZipHelper.java        Cek file ZIP dan ekstrak ZIP
```

## Cara Menjalankan

Butuh JDK 8 atau lebih baru.

```
javac -d out src/*.java
java -cp out Main
```

## Teknologi

- Java Swing (GUI)
- `java.net.URL` dan `HttpURLConnection` (download)
- `SwingWorker` (proses di background)
- `DataInputStream` (cek magic number ZIP)
- `java.util.zip.ZipInputStream` (ekstrak ZIP)

## Anggota Kelompok

| Nama | NIM |
| Ayu Liza Putri Wiwaha |F1D02310003 |
| Zainul Majdi |F1D023100028 |
| Lalu Rifqi Ramadhan |F1D02310071 |

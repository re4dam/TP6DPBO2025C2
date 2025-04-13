# TP6DPBO2025C2
Saya Zaki Adam dengan NIM 2304934 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak akan melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain Program
Program FlappyBird adalah sebuah game sederhana yang dibuat menggunakan Java Swing. Berikut adalah desain dan komponen utama dari program:

#### **1. Komponen Utama:**
- **JPanel**: 
  - `MenuPanel`: Panel untuk menu utama
  - `FlappyBird`: Panel utama game sebagai canvas untuk menggambar elemen game seperti background, burung, dan pipa.
- **Timer**:
    - `gameLoop`: Untuk mengupdate posisi objek dan menggambar ulang layar setiap 1/60 detik (60 FPS).
    - `pipesCooldown`: Untuk menambahkan pipa baru setiap 5 detik.
- **Gambar/Assets**:
    - `background.png`: Gambar latar belakang (digunakan baik di menu maupun game).
    - `bird.png`: Gambar burung (player).
    - `lowerPipe.png` dan `upperPipe.png`: Gambar pipa bawah dan atas.
- **Player**: Objek burung yang dikendalikan pemain.
- **Pipa**: Dua pipa (atas dan bawah) yang membentuk rintangan.
- **Score System**: Menghitung skor ketika pemain berhasil melewati pipa.
- **Menu System**: Sistem untuk navigasi antara menu dan game.

#### **2. Class dan Objek:**
- **`Player`**:  
    - Menyimpan posisi (X, Y), kecepatan, dan gambar burung.     
    - Memiliki metode untuk mengupdate posisi dan kecepatan.      
- **`Pipe`**:
    - Menyimpan posisi (X, Y), kecepatan, dan gambar pipa.
    - Memiliki metode untuk mengupdate posisi.
- **`FlappyBird` (extends JPanel)**:
    - Mengatur logika game, input keyboard, dan rendering.
    - Mengimplementasikan `ActionListener` (untuk game loop) dan `KeyListener` (untuk input).
- **`MenuPanel` (extends JPanel)**:
    - Menampilkan menu utama dengan tombol Start dan Quit.
    - Mengatur transisi antara menu dan game.
    - Mengimplementasikan tampilan menu dengan background dan judul game.

## Penjelasan Alur

#### **1. Inisialisasi Aplikasi:**
- Saat program dijalankan, class `App` membuat JFrame utama.
- `MenuPanel` ditambahkan ke frame sebagai tampilan pertama:
  - Memuat background image yang sama dengan game.
  - Membuat dan menata tombol Start dan Quit.
  - Menampilkan judul game "Flappy Bird".

#### **2. Alur Menu:**
- **Tombol Start Game**:
  - Menghapus `MenuPanel` dari frame.
  - Membuat instance baru `FlappyBird` dan menambahkannya ke frame.
  - Memulai game loop dan pipes timer.
- **Tombol Quit**:
  - Mengakhiri aplikasi dengan `System.exit(0)`.

#### **3. Inisialisasi Game:**
- Saat game dimulai dari menu, konstruktor `FlappyBird` dipanggil:
    - Mengatur ukuran frame (`frameWidth` dan `frameHeight`).
    - Memuat gambar (background, burung, pipa). 
    - Membuat objek `Player` dan `ArrayList<Pipe>` untuk menyimpan pipa. 
    - Menyiapkan `scoreLabel` untuk menampilkan skor. 
    - Memulai `gameLoop` dan `pipesCooldown`.

#### **4. Game Loop (`actionPerformed`):**
- Setiap frame (60 FPS), metode `move()` dipanggil:
    - **Gravitasi**: Kecepatan vertikal burung (`velocityY`) bertambah sebesar `gravity`. 
    - **Pergerakan Pipa**: Pipa bergerak ke kiri dengan kecepatan tertentu.
    - **Deteksi Tabrakan**:
        - Jika burung menyentuh pipa atau jatuh ke bawah layar, `gameOver()` dipanggil.
    - **Skor**: Jika burung melewati pipa, skor bertambah.
    - **Hapus Pipa**: Pipa yang sudah keluar layar dihapus dari `ArrayList`.

#### **5. Input Keyboard (`KeyListener`):**
- **Space**: Menekan tombol space membuat burung melompat.
- **R**: Saat game over, menekan 'R' mereset game.
- **M**: Saat game over, menekan 'M' kembali ke menu utama.

#### **6. Render (`draw`):**
- Menggambar: 
    - Background.    
    - Burung (player).
    - Pipa (dari `ArrayList<Pipe>`).
    - Jika game over, menampilkan:
      - Pesan "Game Over"
      - Skor akhir
      - Instruksi restart ('R')
      - Opsi kembali ke menu ('M')

#### **7. Game Over:**
- Saat `gameOver = true`:
    - Semua gerakan dihentikan.
    - Pesan game over ditampilkan di layar.
    - Pemain bisa:
      - Restart dengan menekan 'R'
      - Kembali ke menu dengan menekan 'M'

#### **8. Transisi Antara Menu dan Game:**
- **Dari Menu ke Game**:
  - Tombol Start memicu pembuatan instance baru `FlappyBird`.
  - `MenuPanel` dihapus dari frame dan diganti dengan `FlappyBird`.
- **Dari Game ke Menu**:
  - Saat game over, tombol 'M' memanggil `returnToMenu()`.
  - `FlappyBird` dihapus dari frame dan diganti dengan `MenuPanel`.

#### **9. Reset Game (`resetGame`):**
- Mengembalikan burung ke posisi awal.
- Menghapus semua pipa.
- Mengatur ulang skor dan status game.

## Dokumentasi
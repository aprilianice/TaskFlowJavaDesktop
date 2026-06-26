package app;

/**
 * Launcher - kelas perantara untuk menjalankan JavaFX di lingkungan
 * yang tidak mendeteksi JavaFX secara otomatis (seperti NetBeans dengan exec-plugin).
 *
 * Kenapa perlu kelas ini?
 * JavaFX Application memerlukan module-path khusus. Jika Main.java (yang extends
 * Application) dijadikan entry point langsung oleh exec-plugin, JVM akan menolak
 * karena tidak menemukan JavaFX runtime di classpath standar.
 *
 * Solusinya: kelas Launcher ini TIDAK extends Application, sehingga JVM
 * bisa memuatnya normal, lalu Launcher memanggil Main.main() yang akan
 * men-trigger JavaFX launcher secara internal.
 */
public class Launcher {

    public static void main(String[] args) {
        // Panggil Main.main() dari sini
        // Cara ini memungkinkan JavaFX berjalan meski tidak ada module-path eksplisit
        // karena JavaFX sudah ada di classpath via Maven dependencies
        Main.main(args);
    }
}

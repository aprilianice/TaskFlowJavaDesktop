package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * SceneManager - utility class untuk mengelola perpindahan halaman (scene).
 * Menggunakan Singleton pattern.
 *
 * Cara pakai: SceneManager.getInstance().showLogin();
 */
public class SceneManager {

    // Singleton instance
    private static SceneManager instance;

    // Primary stage aplikasi
    private Stage primaryStage;

    // Path ke folder FXML
    private static final String FXML_PATH = "/fxml/";

    // Path ke CSS
    private static final String CSS_PATH = "/css/style.css";

    /**
     * Constructor private untuk Singleton pattern.
     */
    private SceneManager() {
    }

    /**
     * Mendapatkan instance singleton SceneManager.
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Set primary stage dari Main.java.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Mendapatkan primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Berpindah ke halaman login.
     */
    public void showLogin() {
        loadScene("login.fxml", 500, 580);
        primaryStage.setTitle("TaskFlow - Login");
        primaryStage.setResizable(false);
    }

    /**
     * Berpindah ke halaman dashboard.
     */
    public void showDashboard() {
        loadScene("dashboard.fxml", 1100, 700);
        primaryStage.setTitle("TaskFlow - Dashboard");
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }

    /**
     * Berpindah ke halaman task management.
     */
    public void showTasks() {
        loadScene("task.fxml", 1100, 700);
        primaryStage.setTitle("TaskFlow - Manajemen Tugas");
        primaryStage.setResizable(true);
    }

    /**
     * Method utama untuk memuat FXML dan menampilkan scene.
     *
     * @param fxmlFile nama file FXML
     * @param width    lebar window
     * @param height   tinggi window
     */
    private void loadScene(String fxmlFile, double width, double height) {
        try {
            // Load FXML file dari resources
            URL fxmlUrl = getClass().getResource(FXML_PATH + fxmlFile);

            if (fxmlUrl == null) {
                System.err.println("ERROR: File FXML tidak ditemukan: " + FXML_PATH + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Buat scene baru
            Scene scene = new Scene(root, width, height);

            // Load CSS jika ada
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            // Tampilkan scene di stage
            primaryStage.setScene(scene);

        } catch (IOException e) {
            System.err.println("ERROR loading scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

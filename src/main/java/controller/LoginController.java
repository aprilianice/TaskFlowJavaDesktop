package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import service.AuthService;
import util.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * LoginController - mengontrol halaman login.
 * Bertanggung jawab menangani input user dan validasi login.
 */
public class LoginController implements Initializable {

    // ==================== FXML COMPONENTS ====================

    @FXML
    private VBox loginCard;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label versionLabel;

    // ==================== SERVICES ====================

    private AuthService authService;

    // ==================== INITIALIZE ====================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authService = AuthService.getInstance();

        // Sembunyikan error label di awal
        errorLabel.setVisible(false);

        // Animasi fade-in untuk login card
        playFadeInAnimation();

        // Allow login dengan tekan Enter di password field
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());
    }

    // ==================== EVENT HANDLERS ====================

    /**
     * Handle tombol login ditekan.
     */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validasi field kosong
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password tidak boleh kosong!");
            return;
        }

        // Proses login
        boolean success = authService.login(username, password);

        if (success) {
            // Login berhasil - pindah ke dashboard
            SceneManager.getInstance().showDashboard();
        } else {
            // Login gagal - tampilkan error
            showError("Username atau password salah. Coba: admin / admin123");

            // Shake animation untuk kartu login
            shakeAnimation();

            // Kosongkan field password
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Menampilkan pesan error.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        // Auto-hide setelah 4 detik
        new Thread(() -> {
            try {
                Thread.sleep(4000);
                javafx.application.Platform.runLater(() -> errorLabel.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Animasi fade-in saat halaman login pertama kali muncul.
     */
    private void playFadeInAnimation() {
        loginCard.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(600), loginCard);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Animasi shake saat login gagal.
     */
    private void shakeAnimation() {
        javafx.animation.TranslateTransition shake =
                new javafx.animation.TranslateTransition(Duration.millis(80), loginCard);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }
}

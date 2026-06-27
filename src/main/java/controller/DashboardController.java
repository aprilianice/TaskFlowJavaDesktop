package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import service.AuthService;
import service.TaskService;
import util.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * DashboardController - mengontrol halaman dashboard.
 * Menampilkan statistik tugas dan informasi pengguna.
 */
public class DashboardController implements Initializable {

    // ==================== FXML COMPONENTS ====================

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label totalTaskLabel;

    @FXML
    private Label completedTaskLabel;

    @FXML
    private Label pendingTaskLabel;

    @FXML
    private Label dateLabel;

    // ==================== SERVICES ====================

    private AuthService authService;
    private TaskService taskService;

    // ==================== INITIALIZE ====================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authService = AuthService.getInstance();
        taskService = TaskService.getInstance();

        // Load data ke UI
        loadUserInfo();
        loadStatistics();
        loadCurrentDate();
    }

    // ==================== LOAD METHODS ====================

    /**
     * Memuat informasi pengguna yang sedang login.
     */
    private void loadUserInfo() {
        if (authService.getCurrentUser() != null) {
            String fullName = authService.getCurrentUser().getFullName();
            welcomeLabel.setText("Selamat datang, " + fullName + "! 👋");
            usernameLabel.setText(fullName);
        }
    }

    /**
     * Memuat statistik tugas ke dalam card-card statistik.
     */
    private void loadStatistics() {
        totalTaskLabel.setText(String.valueOf(taskService.getTotalCount()));
        completedTaskLabel.setText(String.valueOf(taskService.getCompletedCount()));
        pendingTaskLabel.setText(String.valueOf(taskService.getPendingCount()));
    }

    /**
     * Menampilkan tanggal hari ini.
     */
    private void loadCurrentDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy",
                        new java.util.Locale("id", "ID"));
        dateLabel.setText(today.format(formatter));
    }

    // ==================== NAVIGATION ====================

    /**
     * Navigasi ke halaman Task Management.
     */
    @FXML
    public void goToTasks() {
        SceneManager.getInstance().showTasks();
    }

    /**
     * Navigasi ke halaman Dashboard (refresh).
     */
    @FXML
    public void goToDashboard() {
        SceneManager.getInstance().showDashboard();
    }

    /**
     * Logout dari aplikasi.
     */
    @FXML
    public void handleLogout() {
        authService.logout();
        SceneManager.getInstance().showLogin();
    }
}

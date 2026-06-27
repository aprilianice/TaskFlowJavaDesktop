package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Task;
import service.AuthService;
import service.TaskService;
import util.SceneManager;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * TaskController - mengontrol halaman manajemen tugas.
 * Menampilkan tabel tugas dan menangani operasi CRUD.
 */
public class TaskController implements Initializable {

    // ==================== FXML COMPONENTS ====================

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> colId;

    @FXML
    private TableColumn<Task, String> colTitle;

    @FXML
    private TableColumn<Task, String> colDescription;

    @FXML
    private TableColumn<Task, String> colDueDate;

    @FXML
    private TableColumn<Task, String> colStatus;

    @FXML
    private Label totalLabel;

    @FXML
    private Label completedLabel;

    @FXML
    private Label pendingLabel;

    // ==================== SERVICES ====================

    private TaskService taskService;
    private AuthService authService;

    // ==================== INITIALIZE ====================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskService = TaskService.getInstance();
        authService = AuthService.getInstance();

        setupTable();
        setupFilter();
        setupSearch();
        loadUserInfo();
        refreshTable();
    }

    // ==================== SETUP METHODS ====================

    /**
     * Konfigurasi kolom-kolom tabel.
     */
    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Kolom dueDate menggunakan formatted string
        colDueDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDueDateFormatted()));

        // Kolom status menggunakan status text
        colStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatusText()));

        // Style cell untuk kolom status
        colStatus.setCellFactory(column -> new TableCell<Task, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("Selesai")) {
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Placeholder jika tabel kosong
        taskTable.setPlaceholder(new Label("Belum ada tugas. Klik 'Tambah Tugas' untuk memulai."));
    }

    /**
     * Konfigurasi ComboBox filter status.
     */
    private void setupFilter() {
        filterComboBox.getItems().addAll("Semua", "Pending", "Selesai");
        filterComboBox.setValue("Semua");

        filterComboBox.setOnAction(e -> applyFilter());
    }

    /**
     * Konfigurasi search field.
     */
    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    /**
     * Memuat informasi pengguna.
     */
    private void loadUserInfo() {
        if (authService.getCurrentUser() != null) {
            usernameLabel.setText(authService.getCurrentUser().getFullName());
        }
    }

    // ==================== DATA METHODS ====================

    /**
     * Refresh tabel dan statistik.
     */
    private void refreshTable() {
        applyFilter();
        updateStats();
    }

    /**
     * Menerapkan filter dan pencarian ke tabel.
     */
    private void applyFilter() {
        String keyword = searchField.getText();
        String filter = filterComboBox.getValue();

        ObservableList<Task> filtered;

        if (keyword != null && !keyword.trim().isEmpty()) {
            filtered = taskService.searchTasks(keyword);
        } else {
            filtered = taskService.getFilteredTasks(filter);
        }

        taskTable.setItems(filtered);
        updateStats();
    }

    /**
     * Update label statistik.
     */
    private void updateStats() {
        totalLabel.setText("Total: " + taskService.getTotalCount());
        completedLabel.setText("Selesai: " + taskService.getCompletedCount());
        pendingLabel.setText("Pending: " + taskService.getPendingCount());
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Buka form untuk menambahkan tugas baru.
     */
    @FXML
    public void handleAddTask() {
        openTaskForm(null);
    }

    /**
     * Buka form untuk mengedit tugas yang dipilih.
     */
    @FXML
    public void handleEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Tugas",
                    "Silakan pilih tugas yang ingin diedit.");
            return;
        }

        openTaskForm(selectedTask);
    }

    /**
     * Hapus tugas yang dipilih.
     */
    @FXML
    public void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Tugas",
                    "Silakan pilih tugas yang ingin dihapus.");
            return;
        }

        // Konfirmasi sebelum hapus
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText("Hapus Tugas");
        confirm.setContentText("Apakah kamu yakin ingin menghapus tugas:\n\"" + selectedTask.getTitle() + "\"?");

        // Apply stylesheet ke dialog
        applyStyleToDialog(confirm);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            taskService.deleteTask(selectedTask);
            refreshTable();
        }
    }

    /**
     * Tandai tugas yang dipilih sebagai selesai.
     */
    @FXML
    public void handleMarkComplete() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Tugas",
                    "Silakan pilih tugas yang ingin ditandai selesai.");
            return;
        }

        if (selectedTask.isCompleted()) {
            taskService.markAsPending(selectedTask);
        } else {
            taskService.markAsCompleted(selectedTask);
        }

        refreshTable();
    }

    /**
     * Membuka dialog form tambah/edit tugas.
     *
     * @param task null jika tambah baru, Task object jika edit
     */
    private void openTaskForm(Task task) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/task-form.fxml");
            if (fxmlUrl == null) {
                System.err.println("ERROR: task-form.fxml tidak ditemukan");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Kirim data task ke controller form
            TaskFormController formController = loader.getController();
            formController.setTask(task);
            formController.setTaskController(this);

            // Buat dialog window
            Stage dialogStage = new Stage();
            dialogStage.setTitle(task == null ? "Tambah Tugas Baru" : "Edit Tugas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(SceneManager.getInstance().getPrimaryStage());
            dialogStage.setResizable(false);

            Scene scene = new Scene(root, 480, 500);

            // Apply CSS
            URL cssUrl = getClass().getResource("/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            System.err.println("ERROR membuka form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dipanggil setelah form disimpan, untuk refresh tabel.
     */
    public void onFormSaved() {
        refreshTable();
    }

    // ==================== NAVIGATION ====================

    @FXML
    public void goToDashboard() {
        SceneManager.getInstance().showDashboard();
    }

    @FXML
    public void goToTasks() {
        refreshTable();
    }

    @FXML
    public void handleLogout() {
        authService.logout();
        SceneManager.getInstance().showLogin();
    }

    // ==================== HELPER ====================

    /**
     * Menampilkan dialog alert dengan styling.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        applyStyleToDialog(alert);
        alert.showAndWait();
    }

    /**
     * Menambahkan CSS ke dialog alert.
     */
    private void applyStyleToDialog(Alert alert) {
        URL cssUrl = getClass().getResource("/css/style.css");
        if (cssUrl != null) {
            alert.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog-pane");
        }
    }
}

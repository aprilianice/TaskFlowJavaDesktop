package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Task;
import service.TaskService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * TaskFormController - mengontrol form tambah/edit tugas.
 * Digunakan sebagai modal dialog dari TaskController.
 */
public class TaskFormController implements Initializable {

    // ==================== FXML COMPONENTS ====================

    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private Label errorLabel;

    @FXML
    private Button saveButton;

    // ==================== REFERENCES ====================

    private Task editingTask;          // null jika tambah baru
    private TaskController taskController;
    private TaskService taskService;

    // ==================== INITIALIZE ====================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskService = TaskService.getInstance();
        errorLabel.setVisible(false);

        // Set default due date ke hari ini
        dueDatePicker.setValue(LocalDate.now().plusDays(1));
    }

    // ==================== DATA BINDING ====================

    /**
     * Dipanggil dari TaskController untuk set data task yang akan diedit.
     * Jika null, mode tambah baru.
     *
     * @param task Task yang akan diedit, atau null untuk tambah baru
     */
    public void setTask(Task task) {
        this.editingTask = task;

        if (task != null) {
            // Mode edit - isi field dengan data task
            formTitleLabel.setText("Edit Tugas");
            saveButton.setText("Simpan Perubahan");

            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            dueDatePicker.setValue(task.getDueDate());
        } else {
            // Mode tambah baru
            formTitleLabel.setText("Tambah Tugas Baru");
            saveButton.setText("Tambah Tugas");
        }
    }

    /**
     * Set referensi ke TaskController untuk callback setelah save.
     */
    public void setTaskController(TaskController controller) {
        this.taskController = controller;
    }

    // ==================== EVENT HANDLERS ====================

    /**
     * Handle tombol Save ditekan.
     */
    @FXML
    public void handleSave() {
        // Ambil input dari form
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();

        // Validasi
        if (!validateInput(title, dueDate)) {
            return;
        }

        if (editingTask == null) {
            // Mode tambah baru - buat Task baru
            Task newTask = new Task(title, description, dueDate);
            taskService.addTask(newTask);
        } else {
            // Mode edit - update Task yang ada
            taskService.updateTask(editingTask, title, description, dueDate);
        }

        // Callback ke TaskController untuk refresh tabel
        if (taskController != null) {
            taskController.onFormSaved();
        }

        // Tutup dialog
        closeDialog();
    }

    /**
     * Handle tombol Cancel ditekan.
     */
    @FXML
    public void handleCancel() {
        closeDialog();
    }

    // ==================== VALIDATION ====================

    /**
     * Validasi input form.
     *
     * @return true jika valid, false jika ada error
     */
    private boolean validateInput(String title, LocalDate dueDate) {
        if (title == null || title.isEmpty()) {
            showError("Judul tugas tidak boleh kosong!");
            titleField.requestFocus();
            return false;
        }

        if (title.length() < 3) {
            showError("Judul tugas minimal 3 karakter!");
            titleField.requestFocus();
            return false;
        }

        if (dueDate == null) {
            showError("Tanggal jatuh tempo harus diisi!");
            return false;
        }

        return true;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Tampilkan pesan error di form.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Tutup dialog form.
     */
    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}

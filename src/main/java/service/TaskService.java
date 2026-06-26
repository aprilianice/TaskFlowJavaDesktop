package service;

import model.Task;
import model.Task.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * TaskService - menangani semua logika bisnis untuk tugas.
 * Menggunakan ObservableList agar UI otomatis terupdate saat data berubah.
 * Menggunakan pattern Singleton.
 */
public class TaskService {

    // Singleton instance
    private static TaskService instance;

    // ObservableList untuk menyimpan semua tugas (in-memory)
    private ObservableList<Task> taskList;

    /**
     * Constructor private untuk Singleton pattern.
     * Menambahkan beberapa data contoh (sample data).
     */
    private TaskService() {
        taskList = FXCollections.observableArrayList();

        // Tambahkan data contoh agar aplikasi tidak kosong saat pertama dibuka
        taskList.add(new Task(
                "Buat laporan mingguan",
                "Kumpulkan data dan buat laporan progress minggu ini",
                LocalDate.now().plusDays(2)
        ));

        taskList.add(new Task(
                "Review pull request tim",
                "Review dan approve PR dari anggota tim",
                LocalDate.now().plusDays(1)
        ));

        Task completedTask = new Task(
                "Setup environment development",
                "Install dan konfigurasi semua tools yang dibutuhkan",
                LocalDate.now().minusDays(3)
        );
        completedTask.setStatus(Status.COMPLETED);
        taskList.add(completedTask);

        taskList.add(new Task(
                "Presentasi project ke stakeholder",
                "Persiapkan slide dan demo untuk presentasi",
                LocalDate.now().plusDays(7)
        ));

        Task completed2 = new Task(
                "Buat dokumentasi API",
                "Tulis dokumentasi lengkap untuk semua endpoint API",
                LocalDate.now().minusDays(1)
        );
        completed2.setStatus(Status.COMPLETED);
        taskList.add(completed2);
    }

    /**
     * Mendapatkan instance singleton TaskService.
     */
    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    /**
     * Mendapatkan semua tugas (ObservableList).
     */
    public ObservableList<Task> getAllTasks() {
        return taskList;
    }

    /**
     * Mendapatkan daftar tugas berdasarkan filter status.
     *
     * @param filter "ALL", "PENDING", atau "COMPLETED"
     */
    public ObservableList<Task> getFilteredTasks(String filter) {
        if (filter == null || filter.equals("ALL") || filter.equals("Semua")) {
            return taskList;
        }

        Status status = filter.equals("COMPLETED") || filter.equals("Selesai")
                ? Status.COMPLETED : Status.PENDING;

        return FXCollections.observableArrayList(
                taskList.stream()
                        .filter(task -> task.getStatus() == status)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Mencari tugas berdasarkan keyword di judul atau deskripsi.
     */
    public ObservableList<Task> searchTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return taskList;
        }

        String lower = keyword.toLowerCase();
        return FXCollections.observableArrayList(
                taskList.stream()
                        .filter(task ->
                                task.getTitle().toLowerCase().contains(lower) ||
                                task.getDescription().toLowerCase().contains(lower))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Menambahkan tugas baru.
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Memperbarui data tugas yang sudah ada.
     */
    public void updateTask(Task task, String title, String description, LocalDate dueDate) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);

        // Trigger update di ObservableList
        int index = taskList.indexOf(task);
        if (index >= 0) {
            taskList.set(index, task);
        }
    }

    /**
     * Menghapus tugas dari daftar.
     */
    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    /**
     * Mengubah status tugas menjadi COMPLETED.
     */
    public void markAsCompleted(Task task) {
        task.setStatus(Status.COMPLETED);
        // Trigger refresh di ObservableList
        int index = taskList.indexOf(task);
        if (index >= 0) {
            taskList.set(index, task);
        }
    }

    /**
     * Mengubah status tugas menjadi PENDING.
     */
    public void markAsPending(Task task) {
        task.setStatus(Status.PENDING);
        int index = taskList.indexOf(task);
        if (index >= 0) {
            taskList.set(index, task);
        }
    }

    // ==================== STATISTIK ====================

    /**
     * Mendapatkan total jumlah tugas.
     */
    public int getTotalCount() {
        return taskList.size();
    }

    /**
     * Mendapatkan jumlah tugas yang sudah selesai.
     */
    public int getCompletedCount() {
        return (int) taskList.stream()
                .filter(Task::isCompleted)
                .count();
    }

    /**
     * Mendapatkan jumlah tugas yang masih pending.
     */
    public int getPendingCount() {
        return (int) taskList.stream()
                .filter(task -> !task.isCompleted())
                .count();
    }
}

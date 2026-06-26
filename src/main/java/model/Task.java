package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model Task - menyimpan data tugas.
 * Status tugas: PENDING atau COMPLETED.
 */
public class Task {

    /**
     * Enum untuk status tugas.
     */
    public enum Status {
        PENDING, COMPLETED
    }

    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;

    // Counter statis untuk auto-increment ID
    private static int idCounter = 1;

    /**
     * Constructor untuk membuat tugas baru (ID otomatis).
     *
     * @param title       judul tugas
     * @param description deskripsi tugas
     * @param dueDate     tanggal jatuh tempo
     */
    public Task(String title, String description, LocalDate dueDate) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.PENDING; // Default status adalah PENDING
    }

    // ==================== GETTER ====================

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Mengembalikan tanggal dalam format yang mudah dibaca (dd/MM/yyyy).
     */
    public String getDueDateFormatted() {
        if (dueDate == null) return "-";
        return dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Mengembalikan status sebagai String untuk ditampilkan di tabel.
     */
    public String getStatusText() {
        return status == Status.COMPLETED ? "✓ Selesai" : "⏳ Pending";
    }

    /**
     * Mengecek apakah tugas sudah selesai.
     */
    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    // ==================== SETTER ====================

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status=" + status + "}";
    }
}

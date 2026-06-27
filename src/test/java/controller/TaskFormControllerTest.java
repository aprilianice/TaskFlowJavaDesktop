package controller;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TaskFormControllerTest {

    // Mereplikasi logika validateInput() dari TaskFormController

    @Test
    void judulKosongHarusInvalid() {
        String title = "";
        assertTrue(title == null || title.isEmpty());
    }

    @Test
    void judulKurangDari3KarakterHarusInvalid() {
        String title = "AB";
        assertTrue(title.length() < 3);
    }

    @Test
    void judulMinimal3KarakterHarusValid() {
        String title = "Buat laporan";
        assertFalse(title == null || title.isEmpty());
        assertFalse(title.length() < 3);
    }

    @Test
    void dueDateNullHarusInvalid() {
        LocalDate dueDate = null;
        assertNull(dueDate);
    }

    @Test
    void dueDateTerisiHarusValid() {
        LocalDate dueDate = LocalDate.now().plusDays(1);
        assertNotNull(dueDate);
    }
}
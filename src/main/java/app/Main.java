package app;

import javafx.application.Application;
import javafx.stage.Stage;
import util.SceneManager;

/**
 * Main entry point aplikasi TaskFlow.
 * Kelas ini mengextend Application dari JavaFX.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inisialisasi SceneManager dengan primary stage
        SceneManager.getInstance().setPrimaryStage(primaryStage);

        // Set judul dan konfigurasi window
        primaryStage.setTitle("TaskFlow - Task Management");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(true);

        // Tampilkan halaman login pertama kali
        SceneManager.getInstance().showLogin();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

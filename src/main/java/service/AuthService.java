package service;

import model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * AuthService - menangani logika autentikasi pengguna.
 * Menggunakan pattern Singleton agar hanya ada satu instance.
 */
public class AuthService {

    // Singleton instance
    private static AuthService instance;

    // Daftar pengguna (in-memory storage)
    private List<User> userList;

    // Pengguna yang sedang login
    private User currentUser;

    /**
     * Constructor private untuk Singleton pattern.
     * Menambahkan data dummy pengguna.
     */
    private AuthService() {
        userList = new ArrayList<>();

        // Data dummy pengguna
        userList.add(new User("admin", "admin123", "Administrator"));
        userList.add(new User("user", "user123", "Regular User"));
    }

    /**
     * Mendapatkan instance singleton AuthService.
     */
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Melakukan login dengan username dan password.
     *
     * @param username username yang dimasukkan
     * @param password password yang dimasukkan
     * @return true jika login berhasil, false jika gagal
     */
    public boolean login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    /**
     * Melakukan logout pengguna.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Mendapatkan pengguna yang sedang login.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Mengecek apakah ada pengguna yang sedang login.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

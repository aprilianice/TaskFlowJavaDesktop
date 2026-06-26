package model;

/**
 * Model User - menyimpan data pengguna aplikasi.
 * Menggunakan encapsulation dengan private field dan public getter/setter.
 */
public class User {

    private String username;
    private String password;
    private String fullName;

    /**
     * Constructor untuk membuat objek User.
     *
     * @param username username pengguna
     * @param password password pengguna
     * @param fullName nama lengkap pengguna
     */
    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // ==================== GETTER ====================

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    // ==================== SETTER ====================

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', fullName='" + fullName + "'}";
    }
}

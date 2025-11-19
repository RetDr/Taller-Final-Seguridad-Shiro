package uptc.edu.co.model;

public class Usuario {
    private String username;
    private String passwordHash;
    private String rol;

    public Usuario(String username, String passwordHash, String rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRol() { return rol; }
}

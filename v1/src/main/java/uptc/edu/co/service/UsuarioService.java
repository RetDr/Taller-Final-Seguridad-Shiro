package uptc.edu.co.service;

import uptc.edu.co.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UsuarioService {
    private final String URL = "jdbc:mysql://localhost:3306/seguridad_shiro?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "Daniel11052003";

    public UsuarioService() {
        crearTablaUsuarios();
    }

    private void crearTablaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (username VARCHAR(50) PRIMARY KEY, password_hash VARCHAR(255), rol VARCHAR(20))";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean registrarUsuario(String username, String password, String rol) {
        if (buscar(username) != null) return false; // usuario ya existe
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO usuarios(username, password_hash, rol) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hash);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario buscar(String username) {
        String sql = "SELECT username, password_hash, rol FROM usuarios WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getString("username"),
                                   rs.getString("password_hash"),
                                   rs.getString("rol"));
            }
        } catch (SQLException e) { }
        return null;
    }

    public boolean validarPassword(String username, String password) {
        Usuario u = buscar(username);
        if (u != null) {
            return BCrypt.checkpw(password, u.getPasswordHash());
        }
        return false;
    }

    public String getRol(String username) {
        Usuario u = buscar(username);
        return (u != null) ? u.getRol() : null;
    }

    public void listarUsuarios() {
        String sql = "SELECT username, rol FROM usuarios";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("--- Lista de usuarios ---");
            while (rs.next()) {
                System.out.println("Usuario: " + rs.getString("username") + ", Rol: " + rs.getString("rol"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}

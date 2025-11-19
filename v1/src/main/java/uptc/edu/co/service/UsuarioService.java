package uptc.edu.co.service;

import uptc.edu.co.model.Usuario;
import java.util.*;
import java.io.*;
import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class UsuarioService {
    private final String FILE = "usuarios.json";
    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioService() {
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try {
            File file = new File(FILE);
            if (!file.exists()) {
                guardarUsuarios();
            } else {
                String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                usuarios = new Gson().fromJson(json, new TypeToken<List<Usuario>>(){}.getType());
                if (usuarios == null) usuarios = new ArrayList<>();
            }
        } catch (Exception e) {
            usuarios = new ArrayList<>();
        }
    }

    private void guardarUsuarios() {
        try (Writer writer = new FileWriter(FILE)) {
            new Gson().toJson(usuarios, writer);
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios.");
        }
    }

    public boolean registrarUsuario(String username, String password, String rol) {
        if (buscar(username) != null) return false;
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        Usuario nuevo = new Usuario(username, hash, rol);
        usuarios.add(nuevo);
        guardarUsuarios();
        return true;
    }

    public Usuario buscar(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username)) return u;
        }
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

    public void compararHashes(String password) {
    String hashDebil = BCrypt.hashpw(password, BCrypt.gensalt(4)); // Cost bajo: débil
    String hashFuerte = BCrypt.hashpw(password, BCrypt.gensalt(12)); // Cost alto: fuerte
    System.out.println("Hash débil (cost=4): " + hashDebil);
    System.out.println("Hash fuerte (cost=12): " + hashFuerte);
}

}

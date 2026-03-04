package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un usuario del sistema.
 * Contiene su lista privada de notas para garantizar que cada usuario
 * solo vea su propio contenido.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String passwordHash; // Almacenamos el resumen SHA-256, nunca la clave real
    private List<Nota> misNotas;

    public Usuario(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.misNotas = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Nota> getMisNotas() {
        return misNotas;
    }
}
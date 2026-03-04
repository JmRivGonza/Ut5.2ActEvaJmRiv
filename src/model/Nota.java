package model;

import java.io.Serializable;

/**
 * Clase que representa una Nota individual.
 * Implementa Serializable para permitir que los objetos sean convertidos 
 * a flujo de bytes y guardados en el archivo "usuarios.dat".
 */
public class Nota implements Serializable {
    // Identificador único para la serialización (buena práctica)
    private static final long serialVersionUID = 1L;
    private String titulo;
    private String contenido;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    // Métodos de acceso y modificación (Encapsulamiento)
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    /**
     * Sobrescribimos toString para que el JList de Swing 
     * muestre automáticamente el título en la lista visual.
     */
    @Override
    public String toString() {
        return titulo;
    }
}
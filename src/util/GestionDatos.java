package util;

import model.Usuario;
import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane; 

public class GestionDatos {
    // Definición del nombre del archivo como constante
    private static final String ARCHIVO = "usuarios.dat";

    /**
     * Cifrado SHA-256. 
     * Nota: El PDF no explica seguridad, pero se mantiene la lógica funcional.
     */
    public static String cifrar(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) { 
            return base; 
        }
    }

    /**
     * Guarda el mapa de usuarios usando Serialización.
     * Se utiliza try-with-resources para el cierre automático.
     */
    public static void guardar(Map<String, Usuario> usuarios) {
        // Uso de FileOutputStream y ObjectOutputStream para archivos binarios
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(usuarios);
            System.out.println("Datos guardados correctamente."); // Feedback en consola [cite: 913]
        } catch (IOException e) { 
            // En una GUI, se prefiere mostrar el error al usuario 
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage()); 
        }
    }

    /**
     * Carga los datos usando ObjectInputStream.
     * Verifica la existencia del archivo con la clase File
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Usuario> cargar() {
        // Representa el archivo en el sistema
        File file = new File(ARCHIVO);
        
        // Si no existe, se retorna un mapa vacío para evitar errores
        if (!file.exists()) {
            return new HashMap<>();
        }

        // Acceso secuencial para leer el objeto completo
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Manejo de excepciones
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
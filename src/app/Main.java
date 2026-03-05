package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import config.UIConfig;
import view.Login;

public class Main {
    public static void main(String[] args) {
        // Método principal que se ejecuta al iniciar la aplicación
        try {
            // Establece el look and feel de la aplicación
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si hay un error al establecer el look and feel, se muestra un mensaje de error
            System.out.println("Error al establecer el look and feel: " + e.getMessage());
        }
        // Aplica los estilos de la aplicación
        UIConfig.aplicarEstilos();
        // Crea la ventana de login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}

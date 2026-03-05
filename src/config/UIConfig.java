package config;

import javax.swing.*;
import java.awt.*;

public class UIConfig {
    private UIConfig() { }

    public static void aplicarEstilos() {
        try {
            // Establecemos el Look and Feel Nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo aplicar Nimbus. Se usa estilo por defecto.");
        }

        // Configuración de colores para una interfaz más moderna
        UIManager.put("nimbusBase", new Color(52, 85, 122));
        UIManager.put("nimbusBlueGrey", new Color(245, 248, 252)); // Color de fondo de paneles
        UIManager.put("control", new Color(245, 248, 252));
        UIManager.put("nimbusSelectionBackground", new Color(67, 126, 188));

        // Estilos específicos para la JList (donde se ven las notas)
        UIManager.put("List.background", Color.WHITE);
        UIManager.put("List.selectionBackground", new Color(67, 126, 188));
        UIManager.put("List.selectionForeground", Color.WHITE);

        // Fuentes globales para que todo se vea nítido
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 12));
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("TitledBorder.font", new Font("SansSerif", Font.BOLD, 13));
    }
}
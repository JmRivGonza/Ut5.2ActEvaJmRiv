package view;

import model.Usuario;
import util.GestionDatos;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Pantalla de acceso inicial. Gestiona el registro y autenticación de usuarios.
 */
public class Login extends JFrame {
    private JTextField txtUser = new JTextField(15);
    private JPasswordField txtPass = new JPasswordField(15);
    private Map<String, Usuario> baseDatos;

    public Login() {
        super("Acceso al Gestor");
        baseDatos = GestionDatos.cargar(); // Carga previa de datos persistentes

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Configuración de componentes en la rejilla
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        add(txtUser, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        add(txtPass, gbc);

        JButton btnEntrar = new JButton("Entrar");
        JButton btnRegistro = new JButton("Registrar");

        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnRegistro);
        pnlBotones.add(btnEntrar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(pnlBotones, gbc);

        // Lógica de botones
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                registrar();
            }
        });

        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                entrar();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void registrar() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellene todos los campos.");
            return;
        }

        if (baseDatos.containsKey(user)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe.");
        } else {
            // Se guarda el hash de la contraseña por seguridad
            Usuario nuevo = new Usuario(user, GestionDatos.cifrar(pass));
            baseDatos.put(user, nuevo);
            GestionDatos.guardar(baseDatos);
            JOptionPane.showMessageDialog(this, "Registrado con éxito.");
        }
    }

    private void entrar() {
        String user = txtUser.getText();
        String pass = GestionDatos.cifrar(new String(txtPass.getPassword()));

        // Verificación de credenciales comparando hashes
        if (baseDatos.containsKey(user) && baseDatos.get(user).getPasswordHash().equals(pass)) {
            new GestorNotas(baseDatos.get(user), baseDatos).setVisible(true);
            this.dispose(); // Cierra el login al entrar
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
        }
    }
}
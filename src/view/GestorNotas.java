package view;

import model.Nota;
import model.Usuario;
import util.GestionDatos;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Pantalla principal de gestión de notas.
 * Implementa Nivel 1, Nivel 2 y Mejoras Avanzadas.
 */
public class GestorNotas extends JFrame {
    private DefaultListModel<Nota> modeloLista;
    private JList<Nota> listaNotasUI;
    private JTextField txtTitulo, txtBuscar;
    private JTextArea txtContenido;
    private JLabel lblEstado;
    private Usuario usuarioActual;
    private Map<String, Usuario> mapaGlobal;

    public GestorNotas(Usuario usuario, Map<String, Usuario> mapa) {
        super("Gestor de Notas - Usuario: " + usuario.getUsername());
        this.usuarioActual = usuario;
        this.mapaGlobal = mapa;

        initComponents();
        setupLayout();
        setupEvents();

        // Carga inicial de las notas del usuario logueado
        actualizarListaVisual(usuarioActual.getMisNotas());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        modeloLista = new DefaultListModel<>();
        listaNotasUI = new JList<>(modeloLista);
        txtTitulo = new JTextField();
        txtBuscar = new JTextField(15);
        txtContenido = new JTextArea();
        txtContenido.setLineWrap(true);
        lblEstado = new JLabel("Sesión iniciada: " + usuarioActual.getUsername());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // PANEL IZQUIERDO: Búsqueda dinámica y listado
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Mis Notas"));
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(new JLabel(" Buscar: "), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelIzquierdo.add(panelBusqueda, BorderLayout.NORTH);
        panelIzquierdo.add(new JScrollPane(listaNotasUI), BorderLayout.CENTER);
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));

        // PANEL CENTRAL: Editor de contenido
        JPanel panelEditor = new JPanel(new BorderLayout(5, 5));
        panelEditor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JPanel titPanel = new JPanel(new BorderLayout());
        titPanel.add(new JLabel("Título:"), BorderLayout.NORTH);
        titPanel.add(txtTitulo, BorderLayout.CENTER);
        panelEditor.add(titPanel, BorderLayout.NORTH);
        panelEditor.add(new JScrollPane(txtContenido), BorderLayout.CENTER);

        // PANEL INFERIOR: Botones de acción
        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBorrarTodo = new JButton("Borrar Todo"); // Requisito de advertencia
        JButton btnExportar = new JButton("Exportar TXT"); // Mejora avanzada
        JButton btnLogout = new JButton("Cerrar Sesión");

        // Colores para destacar acciones de borrado o logout
        btnBorrarTodo.setBackground(new Color(220, 50, 50));
        btnBorrarTodo.setForeground(Color.WHITE);

        panelBotones.add(btnExportar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBorrarTodo);
        panelBotones.add(btnLogout);

        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(lblEstado, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelEditor, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos de botones
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                guardarYPersistir();
            }
        });

        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eliminarYPersistir();
            }
        });

        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                limpiarCampos();
            }
        });

        btnBorrarTodo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                borrarTodo();
            }
        });

        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                exportarNotas();
            }
        });

        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cerrarSesion();
            }
        });

        // Búsqueda en tiempo real (Mejora avanzada)
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarNotas();
            }
        });
    }

    /**
     * Al seleccionar una nota de la lista, carga sus datos en el editor.
     */
    private void setupEvents() {
        listaNotasUI.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Nota n = listaNotasUI.getSelectedValue();
                    if (n != null) {
                        txtTitulo.setText(n.getTitulo());
                        txtContenido.setText(n.getContenido());
                    }
                }
            }
        });
    }

    /**
     * Crea o edita una nota y guarda los cambios en el archivo automáticamente.
     */
    private void guardarYPersistir() {
        String tit = txtTitulo.getText().trim();
        String cont = txtContenido.getText().trim();
        if (tit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio.");
            return;
        }

        Nota seleccionada = listaNotasUI.getSelectedValue();
        if (seleccionada == null) {
            usuarioActual.getMisNotas().add(new Nota(tit, cont));
        } else {
            seleccionada.setTitulo(tit);
            seleccionada.setContenido(cont);
        }
        actualizarTodo();
        lblEstado.setText("Nota guardada automáticamente.");
    }

    /**
     * Implementa el borrado completo con advertencia obligatoria.
     */
    private void borrarTodo() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres borrar TODAS tus notas?", "Advertencia Crítica", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            usuarioActual.getMisNotas().clear();
            actualizarTodo();
            limpiarCampos();
        }
    }

    /**
     * Mejora Avanzada: Exporta las notas actuales a un archivo legible por humanos.
     */
    private void exportarNotas() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(usuarioActual.getUsername() + "_notas.txt"))) {
            pw.println("--- EXPORTACIÓN DE NOTAS ---");
            for (Nota n : usuarioActual.getMisNotas()) {
                pw.println("TÍTULO: " + n.getTitulo());
                pw.println("CONTENIDO: " + n.getContenido());
                pw.println("-----------------------------");
            }
            JOptionPane.showMessageDialog(this, "Exportado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al exportar.");
        }
    }

    private void eliminarYPersistir() {
        Nota n = listaNotasUI.getSelectedValue();
        if (n != null) {
            usuarioActual.getMisNotas().remove(n);
            actualizarTodo();
            limpiarCampos();
        }
    }

    private void actualizarTodo() {
        actualizarListaVisual(usuarioActual.getMisNotas());
        GestionDatos.guardar(mapaGlobal); // Persistencia total en cada cambio
    }

    /**
     * Filtra la lista según lo que el usuario escribe en el buscador.
     */
    private void filtrarNotas() {
        String q = txtBuscar.getText().toLowerCase();
        java.util.List<Nota> filtradas = new java.util.ArrayList<>();

        for (Nota n : usuarioActual.getMisNotas()) {
            if (n.getTitulo().toLowerCase().contains(q)) {
                filtradas.add(n);
            }
        }
        actualizarListaVisual(filtradas);
    }

    private void actualizarListaVisual(java.util.List<Nota> lista) {
        modeloLista.clear();
        for (Nota n : lista)
            modeloLista.addElement(n);
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtContenido.setText("");
        listaNotasUI.clearSelection();
    }

    private void cerrarSesion() {
        new Login().setVisible(true);
        this.dispose();
    }
}
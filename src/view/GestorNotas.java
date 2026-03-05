package view;

import model.Nota;
import model.Usuario;
import util.GestionDatos;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Pantalla principal de gestión de notas.
 * Implementa Nivel 1, Nivel 2 y Mejoras Avanzadas.
 */
public class GestorNotas extends JFrame {
    // Atributos
    private DefaultListModel<Nota> modeloLista;
    private JList<Nota> listaNotasUI;
    private JTextField txtTitulo, txtBuscar;
    private JTextArea txtContenido;
    private JLabel lblEstado;
    private Usuario usuarioActual;
    private Map<String, Usuario> mapaGlobal;
    private JLabel lblContador;
    private JButton btnGuardar, btnEliminar, btnLimpiar, btnBorrarTodo, btnExportar, btnLogout;

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
        // Inicializamos los componentes
        modeloLista = new DefaultListModel<>();
        listaNotasUI = new JList<>(modeloLista);
        txtTitulo = new JTextField();
        txtBuscar = new JTextField(15);
        txtContenido = new JTextArea();
        txtContenido.setLineWrap(true);
        lblEstado = new JLabel("Sesión iniciada: " + usuarioActual.getUsername());
        lblContador = new JLabel("Notas: 0");

        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar = new JButton("Eliminar");
        btnBorrarTodo = new JButton("Borrar Todo");
        btnExportar = new JButton("Exportar TXT");
        btnLogout = new JButton("Cerrar Sesión");
    }

    private void setupLayout() {
        // Configuración del layout principal
        setLayout(new BorderLayout(10, 10));

        // PANEL IZQUIERDO: Búsqueda dinámica y listado
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Mis Notas"));

        // Panel de búsqueda
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

        // Panel del título
        titPanel.add(new JLabel("Título:"), BorderLayout.NORTH);
        titPanel.add(txtTitulo, BorderLayout.CENTER);
        panelEditor.add(titPanel, BorderLayout.NORTH);
        panelEditor.add(new JScrollPane(txtContenido), BorderLayout.CENTER);

        // PANEL INFERIOR: Botones de acción
        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Colores para destacar acciones de borrado o logout
        btnBorrarTodo.setBackground(new Color(220, 50, 50));
        btnBorrarTodo.setForeground(Color.WHITE);

        // Añadimos los botones al panel de botones
        panelBotones.add(btnExportar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBorrarTodo);
        panelBotones.add(btnLogout);

        // Añadimos los paneles al layout principal
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

        // Buscador LIVE con DocumentListener (Mejora Avanzada) [cite: 451, 471]
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrarNotas();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrarNotas();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrarNotas();
            }
        });
    }

    private void gestionarBotones(boolean notaSeleccionada) {
        // Si hay nota seleccionada, activamos el botón de eliminar y cambiamos el texto del botón de guardar
        btnEliminar.setEnabled(notaSeleccionada);
        btnGuardar.setText(notaSeleccionada ? "Actualizar Nota" : "Crear Nueva");
    }

    /**
     * Al seleccionar una nota de la lista, carga sus datos en el editor.
     */
    private void setupEvents() {
        listaNotasUI.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            // Método que se ejecuta al seleccionar una nota de la lista
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Nota n = listaNotasUI.getSelectedValue();
                    if (n != null) {
                        txtTitulo.setText(n.getTitulo());
                        txtContenido.setText(n.getContenido());
                        gestionarBotones(true);
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

        // 1. Obtenemos la nota seleccionada (si hay alguna)
        Nota seleccionada = listaNotasUI.getSelectedValue();
        if (seleccionada == null) {
            // 2. Si no hay nada seleccionado, creamos una nueva
            usuarioActual.getMisNotas().add(new Nota(tit, cont));
            lblEstado.setText("Nota creada automáticamente.");
        } else {
            // 3. Si hay algo seleccionado, actualizamos esa nota
            seleccionada.setTitulo(tit);
            seleccionada.setContenido(cont);
            lblEstado.setText("Nota actualizada automáticamente.");
        }
        // 4. Refrescamos la vista y guardamos en disco
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
            lblEstado.setText("Todas las notas han sido borradas.");
        }
    }

    /**
     * Mejora Avanzada: Exporta las notas actuales a un archivo legible por humanos.
     */
    private void exportarNotas() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Selecciona dónde exportar tus notas");

        // 1. Abrimos el diálogo para guardar
        int resultado = selector.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            // 2. Obtenemos la ruta elegida por el usuario
            File destino = selector.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(destino + ".txt"))) {
                pw.println("--- EXPORTACIÓN DE NOTAS ---");
                for (Nota n : usuarioActual.getMisNotas()) {
                    pw.println("TÍTULO: " + n.getTitulo());
                    pw.println("CONTENIDO: " + n.getContenido());
                    pw.println("-----------------------------");
                }
                JOptionPane.showMessageDialog(this, "Exportado correctamente en: " + destino.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar.");
            }
        }
    }

    private void eliminarYPersistir() {
        // 1. Obtenemos la nota seleccionada
        Nota n = listaNotasUI.getSelectedValue();
        if (n != null) {
            // 2. La eliminamos de la lista del usuario
            usuarioActual.getMisNotas().remove(n);
            // 3. Refrescamos la vista y guardamos en disco
            actualizarTodo();
            limpiarCampos();
            lblEstado.setText("Nota eliminada correctamente.");
        }
    }

    private void actualizarTodo() {
        // 1. Refrescamos la lista con las notas actuales
        actualizarListaVisual(usuarioActual.getMisNotas());
        // 2. Guardamos todo en el archivo (persistencia)
        GestionDatos.guardar(mapaGlobal);
    }

    /**
     * Filtra la lista según lo que el usuario escribe en el buscador.
     */
    private void filtrarNotas() {
        // 1. Obtenemos el texto buscado (en minúsculas para comparar fácil)
        String q = txtBuscar.getText().toLowerCase();
        java.util.List<Nota> filtradas = new java.util.ArrayList<>();

        // 2. Recorremos TODAS las notas del usuario
        for (Nota n : usuarioActual.getMisNotas()) {
            // 3. Si el título contiene el texto buscado, lo añadimos a la lista filtrada
            if (n.getTitulo().toLowerCase().contains(q)) {
                filtradas.add(n);
            }
        }
        // 4. Actualizamos la lista visual solo con las que coinciden
        actualizarListaVisual(filtradas);
    }

    private void actualizarListaVisual(java.util.List<Nota> lista) {
        // 1. Limpiamos la lista actual
        modeloLista.clear();
        // 2. Añadimos todas las notas de la lista que nos han pasado
        for (Nota n : lista)
            modeloLista.addElement(n);
        // 3. Actualizamos el contador de notas
        lblContador.setText("Notas: " + modeloLista.size());
    }

    private void limpiarCampos() {
        // 1. Limpiamos los campos de texto
        txtTitulo.setText("");
        txtContenido.setText("");
        // 2. Deseleccionamos cualquier nota que estuviera elegida
        listaNotasUI.clearSelection();
        // 3. Desactivamos los botones de editar/eliminar
        gestionarBotones(false);
    }

    private void cerrarSesion() {
        new Login().setVisible(true);
        this.dispose();
    }
}
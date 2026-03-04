package view;

import model.Nota;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Clase que representa la interfaz gráfica del gestor de notas.
 * Utiliza Swing para la visualización y gestión de objetos de tipo Nota.
 * Nivel 1 - Funcionalidad Completa.
 * 
 * @author Jonay Rivero
 */
public class GestorNotas extends JFrame {

    // --- Componentes de la interfaz gráfica ---
    private DefaultListModel<Nota> modeloLista; // Modelo para manejar los datos de la JList
    private JList<Nota> listaNotasUI; // Componente que permite mostrar la lista de notas
    private JTextField txtTitulo, txtBuscar; // Campo de texto para el título de la nota
    private JTextArea txtContenido; // Campo de texto para el contenido de la nota
    private JLabel lblEstado; // Etiqueta para mostrar el estado de la aplicación
    private JButton btnGuardar, btnBorrarTodo, btnEliminar, btnLimpiar; // Botones de la interfaz gráfica

    // --- Almacenamiento de datos ---
    private ArrayList<Nota> listaMemoria; // ArrayList para guardar las notas

    /**
     * Constructor: Configura la ventana y lanza la inicialización de la UI.
     */
    public GestorNotas() {
        super("Gestor de Notas");
        listaMemoria = new ArrayList<>();

        // Intentamos adaptar el diseño al sistema operativo (Windows, Mac, Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, no pasa nada, se queda con el diseño por defecto de Java.
        }

        initComponents(); // Inicializa los componentes
        setupLayout(); // Organiza los componentes en la ventana
        setupEvents(); // Configura el comportamiento de los botones y la lista.

        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setSize(800, 400);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    /**
     * Método que inicializa los componentes de la interfaz gráfica.
     */
    private void initComponents() {
        modeloLista = new DefaultListModel<>();
        listaNotasUI = new JList<>(modeloLista);
        txtTitulo = new JTextField(20);
        txtContenido = new JTextArea(10, 30);
        txtBuscar = new JTextField(15);
        lblEstado = new JLabel("Estado: Listo"); // Para una mejor UX.

        // Botones de la interfaz gráfica
        btnGuardar = new JButton("Guardar");
        btnBorrarTodo = new JButton("Borrar Todo");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
    }

    /**
     * Método que configura el layout de la interfaz gráfica.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Panel Izquierdo: Buscador + Lista
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));

        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(new JLabel("Filtrar por título:"), BorderLayout.NORTH);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

        panelIzquierdo.add(panelBusqueda, BorderLayout.NORTH);
        panelIzquierdo.add(new JScrollPane(listaNotasUI), BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel Central: Edición
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(new JLabel("Título de la nota:"), BorderLayout.NORTH);

        // Agrupamos título y contenido en el centro
        JPanel panelCampos = new JPanel(new BorderLayout(5, 5));
        panelCampos.add(txtTitulo, BorderLayout.NORTH);
        panelCampos.add(new JScrollPane(txtContenido), BorderLayout.CENTER);

        add(panelCampos, BorderLayout.CENTER);

        // Panel Sur: Botones y Logs
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBorrarTodo);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(lblEstado, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Define la lógica de interacción de los botones y la lista.
     */
    private void setupEvents() {

        // Logica de busqueda/Filtrado.
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltro();
            }
        });

        // Botón Guardar: Crea o actualiza una nota
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tit = txtTitulo.getText().trim();

                // Validación: El título es obligatorio
                if (tit.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El título es obligatorio");
                    lblEstado.setText("Estado: Intento de guardado fallido (sin título).");
                    return;
                }

                // Comprobar si estamos editando una nota existente o creando una nueva
                Nota seleccionada = listaNotasUI.getSelectedValue();
                if (seleccionada == null) {
                    // Crear nueva nota en el caso de que no exista una seleccionada
                    Nota nueva = new Nota(tit, txtContenido.getText());
                    listaMemoria.add(nueva);
                    modeloLista.addElement(nueva);
                    lblEstado.setText("Estado: Nota " + tit + " creada correctamente.");
                } else {
                    // Editar nota existente en el caso de que exista una seleccionada
                    seleccionada.setTitulo(tit);
                    seleccionada.setContenido(txtContenido.getText());
                    listaNotasUI.repaint(); // Refresca visualmente
                    lblEstado.setText("Estado: Nota " + tit + " actualizada correctamente.");
                }

                aplicarFiltro(); // Aplica el filtro para actualizar la lista
                limpiar(); // Limpia los campos de texto
            }
        });

        // Botón Eliminar: Elimina la nota seleccionada
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nota seleccionada = listaNotasUI.getSelectedValue();
                if (seleccionada != null) {
                    modeloLista.removeElement(seleccionada);
                    listaMemoria.remove(seleccionada);
                    limpiar();
                    lblEstado.setText("Estado: Nota eliminada correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar una nota para eliminar");
                    lblEstado.setText("Estado: Intento de eliminación fallido (sin selección).");
                }
            }
        });

        // Botón Limpiar: Limpia los campos de texto
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
                lblEstado.setText("Estado: Campos limpios");
            }
        });

        // Botón Borrar Todo: Elimina todas las notas
        btnBorrarTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null,
                        "¿ESTÁ SEGURO? Esta acción eliminará TODAS las notas permanentemente.") == JOptionPane.YES_OPTION) {
                    modeloLista.clear();
                    listaMemoria.clear();
                    aplicarFiltro();
                    limpiar();
                    lblEstado.setText("Estado: Todas las notas eliminadas");
                } else {
                    lblEstado.setText("Estado: Operación cancelada por el usuario.");
                }
            }
        });

        // Evento: Al hacer CLICK en una nota de la lista, cargar sus datos en el
        // formulario
        listaNotasUI.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Nota seleccionada = listaNotasUI.getSelectedValue();
                if (seleccionada != null) {
                    txtTitulo.setText(seleccionada.getTitulo());
                    txtContenido.setText(seleccionada.getContenido());
                    lblEstado.setText("Estado: Viendo/Editando nota: " + seleccionada.getTitulo());
                } else {
                    // Si se deselecciona, limpiamos
                    limpiar();
                    lblEstado.setText("Estado: Listo");
                }
            }
        });
    }

    /**
     * Requisito: Buscar / Filtrar.
     * Este método actualiza la UI basándose en lo que hay en txtBuscar.
     */

    private void aplicarFiltro() {
        String filtro = txtBuscar.getText().toLowerCase();
        modeloLista.clear();
        for (Nota nota : listaMemoria) {
            if (nota.getTitulo().toLowerCase().contains(filtro)) {
                modeloLista.addElement(nota);
            }
        }
    }

    /**
     * Vacía los campos de texto y quita la selección de la lista.
     */
    private void limpiar() {
        txtTitulo.setText("");
        txtContenido.setText("");
        listaNotasUI.clearSelection();
    }

    public static void main(String[] args) {
        // Ejecución en el hilo de despacho de eventos de Swing (Seguridad de hilos)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestorNotas().setVisible(true);
            }
        });
    }
}
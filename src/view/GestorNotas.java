package view;

import model.Nota;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GestorNotas {
    private DefaultListModel<Nota> modeloLista;
    private Jlist<Nota> listaNotasUI;
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JLabel lblEstado;
    private ArrayList<Nota> listaMemoria; // arraylist para guardar las notas
    private JButton btnGuardar, btnBorrarTodo, btnEliminar, btnLimpiar;

    public GestorNotas() {
        super("Gestor de Notas");
        listaMemoria = new ArrayList<>();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        initComponents();
        setupLayout();
        setupEvents();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        modeloLista = new DefaultListModel<>();
        listaNotasUI = new JList<>(modeloLista);
        txtTitulo = new JTextField(20);
        txtContenido = new JTextArea(10, 30);
        lblEstado = new JLabel("Estado: Listo"); // Para una mejor UX.
     
        btnGuardar = new JButton("Guardar");
        btnBorrarTodo = new JButton("Borrar Todo");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(new JLabel("Mis Notas:"), BorderLayout.NORTH);
        panelIzquierdo.add(new JScrollPane(listaNotasUI), BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(txtTitulo, BorderLayout.NORTH);
        panelCentral.add(new JScrollPane(txtContenido), BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

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

    private void setupEvents() {

    }

    public static void main(String[] args) {
    }
}
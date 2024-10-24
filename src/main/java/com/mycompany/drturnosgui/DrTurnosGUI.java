package com.mycompany.drturnosgui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class DrTurnosGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Set<String> turnosSet = new HashSet<>();
    public static Set<Paciente> pacientes = new HashSet<>();
    public static Set<ObraSocial> obrasSociales = new HashSet<>();

    public DrTurnosGUI() {
        CargarHashSets();
        initUI();
    }

    private void initUI() {
        setTitle("Gestor de Turnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 386);
        model = createTableModel();
        createUIComponents();
        loadTableData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(String[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private CustomTableModel createTableModel() {
        String[] columnNames = {"Día", "Hora", "DNI", "Nombre", "Teléfono", "Obra Social", "Motivo"};
        return new CustomTableModel(columnNames, 0);
    }

    private void createUIComponents() {
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton(buttonPanel, "Pacientes", e -> openPacientesGUI());
        addButton(buttonPanel, "Obras Sociales", e -> openObrasSocialesGUI());
        addButton(buttonPanel, "Agregar/Modificar", e -> openModificarTurnoGUI());
        addButton(buttonPanel, "Eliminar", e -> limpiarCamposSeleccionados());
        addButton(buttonPanel, "Cerrar", e -> Cerrar());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addButton(Container container, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        container.add(button);
    }

    private void openPacientesGUI() {
        PacientesGUI pacientesGUI = new PacientesGUI(pacientes, obrasSociales);
        pacientesGUI.setVisible(true);
    }

    private void openObrasSocialesGUI() {
        ObrasSocialesGUI obrasSocialesGUI = new ObrasSocialesGUI(obrasSociales);
        obrasSocialesGUI.setVisible(true);
    }

    private void openModificarTurnoGUI() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ModificarTurnoGUI modificarTurnoGUI = new ModificarTurnoGUI(model, selectedRow, pacientes, obrasSociales);
            modificarTurnoGUI.setVisible(true);
        } else {
            showError("Selecciona un turno para modificar.");
        }
    }

    private void limpiarCamposSeleccionados() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String fecha = model.getValueAt(selectedRow, 0).toString();
            String hora = model.getValueAt(selectedRow, 1).toString();
            String dni = model.getValueAt(selectedRow, 2).toString();

            model.setValueAt("", selectedRow, 2);
            model.setValueAt("", selectedRow, 3);
            model.setValueAt("", selectedRow, 4);
            model.setValueAt("", selectedRow, 5);
            model.setValueAt("", selectedRow, 6);

            eliminarTurnoEnArchivo(fecha, hora, dni);
        } else {
            showError("Selecciona un turno para limpiar los campos.");
        }
    }

    private void eliminarTurnoEnArchivo(String fecha, String hora, String dni) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("turnos.txt"));
            String line;
            StringBuilder fileContent = new StringBuilder();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length < 3 || !fecha.equals(fields[0]) || !hora.equals(fields[1]) || !dni.equals(fields[2])) {
                    fileContent.append(line).append("\n");
                } else {
                    String newLine = fields[0] + ", " + fields[1] + ", , , , , ";
                    fileContent.append(newLine).append("\n");
                }
            }
            br.close();

            Files.write(Paths.get("turnos.txt"), fileContent.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            loadTableData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("turnos.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fecha = LocalDate.parse(fields[0], formatter);
                LocalDate now = LocalDate.now();

                if (!fecha.isBefore(now)) {
                    model.addRow(fields);
                    if (fields.length >= 2) {
                        String hora = fields[1];
                        String turnoKey = fecha + ", " + hora;
                        turnosSet.add(turnoKey);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();

            loginForm.setLoginSuccessListener(() -> {
                DrTurnosGUI mainGUI = new DrTurnosGUI();
                mainGUI.setVisible(true);
            });

            loginForm.setVisible(true);
        });
    }

    private void Cerrar() {
        GuardarHashSets();
        dispose();
    }

    private void guardarHashSet(Set<? extends Serializable> set, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(set);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T extends Serializable> Set<T> cargarHashSet(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Set<T> setDeserializado = (Set<T>) objectInputStream.readObject();
            objectInputStream.close();
            return setDeserializado;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void CargarHashSets() {
        pacientes = cargarHashSet("pacientes.ser");
        if (pacientes == null) {
            pacientes = new HashSet<>();
        }
        obrasSociales = cargarHashSet("obrasSociales.ser");
        if (obrasSociales == null) {
            obrasSociales = new HashSet<>();
        }
    }

    public void GuardarHashSets() {
        guardarHashSet(obrasSociales, "obrasSociales.ser");
        guardarHashSet(pacientes, "pacientes.ser");
    }
}

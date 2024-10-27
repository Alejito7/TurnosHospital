/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class ModificarPacienteGUI extends JFrame {
    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoTelefono;
    private JComboBox<String> obraSocialComboBox;
    private Set<ObraSocial> obrasSociales;
    private Set<Paciente> pacientes;
    private DefaultTableModel model;
    private int selectedRow;

    public ModificarPacienteGUI(Set<ObraSocial> obrasSociales, Set<Paciente> pacientes, DefaultTableModel model, int selectedRow) {
        this.obrasSociales = obrasSociales;
        this.pacientes = pacientes;
        this.model = model;
        this.selectedRow = selectedRow;
        initUI();
    }

    private void initUI() {
        setTitle("Modificar Paciente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);

        campoDni = new JTextField();
        campoNombre = new JTextField();
        campoTelefono = new JTextField();
        obraSocialComboBox = new JComboBox<>();

        // Cargar obras sociales en el combo box
        for (ObraSocial obraSocial : obrasSociales) {
            obraSocialComboBox.addItem(obraSocial.getObraSocial());
        }

        // Pre-cargar los datos del paciente seleccionado en los campos de texto
        campoDni.setText((String) model.getValueAt(selectedRow, 0));
        campoNombre.setText((String) model.getValueAt(selectedRow, 1));
        campoTelefono.setText((String) model.getValueAt(selectedRow, 2));
        obraSocialComboBox.setSelectedItem(model.getValueAt(selectedRow, 3));

        JButton modificarButton = new JButton("Guardar cambios");
        modificarButton.addActionListener(e -> modificarPaciente());

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("DNI:"));
        panel.add(campoDni);
        panel.add(new JLabel("Nombre y Apellido:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Teléfono:"));
        panel.add(campoTelefono);
        panel.add(new JLabel("Obra Social:"));
        panel.add(obraSocialComboBox);
        panel.add(modificarButton);

        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void modificarPaciente() {
        String nuevoDni = campoDni.getText().trim();
        String nombre = campoNombre.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String obraSocial = (String) obraSocialComboBox.getSelectedItem();

        // Validaciones
        if (!nuevoDni.matches("\\d+")) {
            showError("El DNI debe contener solo números.");
            return;
        }

        if (nombre.matches(".*\\d.*")) {
            showError("El Nombre no debe contener números.");
            return;
        }

        if (!telefono.matches("\\d+")) {
            showError("El Teléfono debe contener solo números.");
            return;
        }

        // Obtener el paciente correspondiente a la fila seleccionada
        Paciente paciente = obtenerPacientePorFila(selectedRow);

        if (paciente != null) {
            // Actualizar los datos del paciente
            paciente.setDni(nuevoDni);
            paciente.setNombre(nombre);
            paciente.setTelefono(telefono);
            paciente.setObraSocial(obraSocial);

            // Actualizar la tabla con los nuevos datos
            model.setValueAt(nuevoDni, selectedRow, 0);
            model.setValueAt(nombre, selectedRow, 1);
            model.setValueAt(telefono, selectedRow, 2);
            model.setValueAt(obraSocial, selectedRow, 3);

            dispose();
        } else {
            showError("Paciente no encontrado.");
        }
    }

    private Paciente obtenerPacientePorFila(int fila) {
        String dni = (String) model.getValueAt(fila, 0);
        for (Paciente paciente : pacientes) {
            if (paciente.getDni().equals(dni)) {
                return paciente;
            }
        }
        return null;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isAlphanumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
}

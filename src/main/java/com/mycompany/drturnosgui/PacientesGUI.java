/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PacientesGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Set<Paciente> pacientes;
    private Set<ObraSocial> obrasSociales;

    public PacientesGUI(Set<Paciente> pacientes, Set<ObraSocial> obrasSociales) {
        this.pacientes = pacientes;
        this.obrasSociales = obrasSociales;
        initUI();
    }

    private void initUI() {
        setTitle("Pacientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 386);
        model = createTableModel();
        createUIComponents();
        loadTableData();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hace que las celdas no sean editables
        }
    }

    private CustomTableModel createTableModel() {
        Object[] columnNames = {"DNI", "Nombre", "Telefono", "Obra Social"};
        return new CustomTableModel(columnNames, 0);
    }

    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpia la tabla antes de cargar los datos

        for (Paciente paciente : pacientes) {
            model.addRow(new Object[]{
                paciente.getDni(), paciente.getNombre(), 
                paciente.getTelefono(), paciente.getObraSocial()
            });
        }
    }

    private void createUIComponents() {
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton(buttonPanel, "Agregar", e -> openAgregarPacienteGUI());
        addButton(buttonPanel, "Modificar", e -> openModificarPacienteGUI());
        addButton(buttonPanel, "Eliminar", e -> limpiarCamposSeleccionados());
        addButton(buttonPanel, "Reporte", e -> exportarTablaExcel()); // Botón para exportar el listado a un archivo excel
        addButton(buttonPanel, "Cerrar", e -> Cerrar());
        add(buttonPanel, BorderLayout.SOUTH);
        
    }

    private void addButton(Container container, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        container.add(button);
    }

    private void openAgregarPacienteGUI() {
        AgregarPacienteGUI agregarPacienteGUI = new AgregarPacienteGUI(pacientes, obrasSociales);
        agregarPacienteGUI.setVisible(true);
        dispose();
    }

    private void openModificarPacienteGUI() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ModificarPacienteGUI modificarPacienteGUI = 
                new ModificarPacienteGUI(obrasSociales, pacientes, model, selectedRow);
            modificarPacienteGUI.setVisible(true);
            dispose();
        } else {
            showError("Selecciona un paciente para modificar.");
        }
        loadTableData();
    }

    private void limpiarCamposSeleccionados() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String dni = (String) model.getValueAt(selectedRow, 0);
            model.removeRow(selectedRow);
            eliminarPaciente(dni);
        } else {
            showError("Selecciona un paciente para eliminar.");
        }
    }

    private void eliminarPaciente(String dni) {
        pacientes.removeIf(paciente -> paciente.getDni().equals(dni));
        loadTableData();
    }

    private void Cerrar() {
        GuardarHashSet();
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void GuardarHashSet() {
        guardarHashSet(pacientes, "pacientes.ser");
    }

    private void guardarHashSet(Set<? extends Serializable> set, String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para exportar la tabla a Excel
    private void exportarTablaExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Pacientes");
                Row headerRow = sheet.createRow(0);

                // Exportar encabezados
                for (int i = 0; i < model.getColumnCount(); i++) {
                    headerRow.createCell(i).setCellValue(model.getColumnName(i));
                }

                // Exportar filas
                for (int row = 0; row < model.getRowCount(); row++) {
                    Row excelRow = sheet.createRow(row + 1);
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        excelRow.createCell(col).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Guardar archivo
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx";
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                    JOptionPane.showMessageDialog(this, "Exportación exitosa a: " + filePath);
                }
            } catch (IOException e) {
                showError("Error al exportar: " + e.getMessage());
            }
        }
    }
}

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
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        // Devuelve false para hacer que todas las celdas no sean editables
        return false;
    }
}

   private CustomTableModel createTableModel() {
    Object[] columnNames = {"DNI", "Nombre", "Telefono", "Obra Social"};
    return new CustomTableModel(columnNames, 0);
}

    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }

        for (Paciente paciente : pacientes) {
           model.addRow(new Object[]{paciente.getDni(), paciente.getNombre(), paciente.getTelefono(), paciente.getObraSocial()});
        }
    }

    private void createUIComponents() {
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton(buttonPanel, "Agregar", e -> openAgregarPacienteGUI());
        addButton(buttonPanel, "Modificar", e -> openModificarPacienteGUI());
        addButton(buttonPanel, "Eliminar", e -> limpiarCamposSeleccionados());
        addButton(buttonPanel, "Cerrar", e -> Cerrar());
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addButton(Container container, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        container.add(button);
    }
    
    // Abrir ventanas
    private void openAgregarPacienteGUI(){
        AgregarPacienteGUI agregarPacienteGUI = new AgregarPacienteGUI (pacientes, obrasSociales);
        agregarPacienteGUI.setVisible(true);
        dispose();
    }
    
    private void openModificarPacienteGUI() {
       int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            ModificarPacienteGUI modificarPacienteGUI = new ModificarPacienteGUI(obrasSociales, pacientes, model, selectedRow);
            modificarPacienteGUI.setVisible(true);
             dispose();
        } else {
           showError("Selecciona un paciente para modificar.");
        }
        loadTableData();
    }
    
    private void limpiarCamposSeleccionados(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String dni = model.getValueAt(selectedRow, 0).toString();

            model.setValueAt("", selectedRow, 0);
            model.setValueAt("", selectedRow, 1); 
            model.setValueAt("", selectedRow, 2); 
            model.setValueAt("", selectedRow, 3); 

            eliminarPaciente(dni);

        } else {
            showError("Selecciona un pacientes para limpiar los campos.");
        }
    }
    
    private void eliminarPaciente(String dni) {
        Iterator<Paciente> iterator = pacientes.iterator();
        while (iterator.hasNext()) {
            Paciente paciente = iterator.next();
            if (paciente.getDni().equals(dni)) {
                iterator.remove();
                break;
            }
        }
        loadTableData();
    }
    
    private void Cerrar(){
        GuardarHashSet();
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void GuardarHashSet(){
        guardarHashSet(pacientes, "pacientes.ser");
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
}
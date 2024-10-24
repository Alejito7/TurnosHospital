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

    public ModificarPacienteGUI(Set<ObraSocial> obrasSociales, Set<Paciente> pacientes,DefaultTableModel model, int selectedRow) {
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
        
        for (ObraSocial obraSocial : obrasSociales) {
             obraSocialComboBox.addItem(obraSocial.getObraSocial());
        }
        
        campoDni.setText((String) model.getValueAt(selectedRow, 0));
        campoNombre.setText((String) model.getValueAt(selectedRow, 1));
        campoTelefono.setText((String) model.getValueAt(selectedRow, 2));

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
        String dni = campoDni.getText();
        String nombre = campoNombre.getText();
        String telefono = campoTelefono.getText();
        String obraSocial = (String) obraSocialComboBox.getSelectedItem();

        if (!dni.matches("\\d+")) {
        showError("El DNI debe contener solo números.");
        return;
        }

    // Validación de Nombre: No debe contener números
       if (nombre.matches(".*\\d.*")) {
        showError("El Nombre no debe contener números.");
        return;
        }


    // Validación de Teléfono: Solo permite números
    if (!telefono.matches("\\d+")) {
        showError("El Teléfono debe contener solo números.");
        return;
    }   
        // Realizar la modificación en la lista después de las validaciones
 
        if (isAlphanumeric(nombre) && isAlphanumeric(telefono)) {
            for (Paciente paciente : pacientes) {
                if (paciente.getDni().equals(dni)){
                    paciente.setNombre(nombre);
                    paciente.setTelefono(telefono);
                    paciente.setObraSocial(obraSocial);
                    break;
                }
        }
            
      } else {
         if (!isAlphanumeric(nombre)) {
            showError("El nombre solo puede contener letras");
            return;
        }
        if (!isAlphanumeric(telefono)) {
            showError("El telefono solo puede contener numeros");
            return;
        }
      }
        // Resto del código
        PacientesGUI cli = new PacientesGUI(pacientes, obrasSociales);
        cli.setVisible(true);
        dispose();

}

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }  
    
    public boolean isAlphanumeric(String str){

        char[] charArray = str.toCharArray();
        for(char c:charArray)
        {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
                return false;
        }
        return true;
    }

    
}
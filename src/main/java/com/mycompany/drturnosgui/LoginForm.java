/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.drturnosgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField jTextFieldUsuario;
    private JPasswordField jPasswordFieldContraseña;
    private JButton jButtonLogin;

    private Runnable loginSuccessListener;

    public LoginForm() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(null);

        // Etiqueta para el campo de usuario
        JLabel jLabelUsuario = new JLabel("Usuario:");
        jLabelUsuario.setBounds(30, 20, 80, 25);
        add(jLabelUsuario);

        // Campo de texto para ingresar el nombre de usuario
        jTextFieldUsuario = new JTextField();
        jTextFieldUsuario.setBounds(120, 20, 140, 25);
        add(jTextFieldUsuario);

        // Etiqueta para el campo de contraseña
        JLabel jLabelContraseña = new JLabel("Contraseña:");
        jLabelContraseña.setBounds(30, 50, 80, 25);
        add(jLabelContraseña);

        // Campo de texto para ingresar la contraseña
        jPasswordFieldContraseña = new JPasswordField();
        jPasswordFieldContraseña.setBounds(120, 50, 140, 25);
        add(jPasswordFieldContraseña);

        // Botón de "Iniciar sesión"
        jButtonLogin = new JButton("Iniciar Sesión");
        jButtonLogin.setBounds(90, 90, 120, 25);
        add(jButtonLogin);

        // Agregar acción al botón de iniciar sesión
        jButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarCredenciales();
            }
        });
    }

    // Este método comprueba las credenciales del usuario
    private void validarCredenciales() {
        String usuario = jTextFieldUsuario.getText();
        String contraseña = new String(jPasswordFieldContraseña.getPassword());

        // Verifica si las credenciales son correctas (puedes cambiar las condiciones según tu necesidad)
        if (usuario.equals("admin") && contraseña.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");

            // Cerrar la ventana de login
            dispose();

            // Llamar al listener de éxito si existe
            if (loginSuccessListener != null) {
                loginSuccessListener.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Permite definir qué hacer después de un inicio de sesión exitoso
    public void setLoginSuccessListener(Runnable listener) {
        this.loginSuccessListener = listener;
    }
}


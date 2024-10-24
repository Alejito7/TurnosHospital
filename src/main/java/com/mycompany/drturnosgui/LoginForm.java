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
        // Configuración básica de la ventana
        setTitle("Sistema de Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Usar un layout manager en lugar de null layout
        setLayout(new BorderLayout());
        
        // Panel principal con degradado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(66, 139, 202);
                Color color2 = new Color(219, 238, 244);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Panel para los componentes del formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Personalización de la fuente
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Etiqueta y campo de usuario
        JLabel jLabelUsuario = new JLabel("Usuario:");
        jLabelUsuario.setFont(labelFont);
        jLabelUsuario.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(jLabelUsuario, gbc);
        
        jTextFieldUsuario = new JTextField(15);
        jTextFieldUsuario.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(jTextFieldUsuario, gbc);
        
        // Etiqueta y campo de contraseña
        JLabel jLabelContraseña = new JLabel("Contraseña:");
        jLabelContraseña.setFont(labelFont);
        jLabelContraseña.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(jLabelContraseña, gbc);
        
        jPasswordFieldContraseña = new JPasswordField(15);
        jPasswordFieldContraseña.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(jPasswordFieldContraseña, gbc);
        
        // Botón de login personalizado
        jButtonLogin = new JButton("Iniciar Sesión");
        jButtonLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonLogin.setForeground(Color.WHITE);
        jButtonLogin.setBackground(new Color(46, 109, 164));
        jButtonLogin.setFocusPainted(false);
        jButtonLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(jButtonLogin, gbc);
        
        // Agregar efecto hover al botón
        jButtonLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonLogin.setBackground(new Color(36, 89, 144));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonLogin.setBackground(new Color(46, 109, 164));
            }
        });
        
        // Agregar acción al botón
        jButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarCredenciales();
            }
        });
        
        // Agregar el panel del formulario al panel principal
        mainPanel.add(formPanel);
        
        // Agregar el panel principal a la ventana
        add(mainPanel);
        
        // Hacer la ventana no redimensionable
        setResizable(false);
    }

    private void validarCredenciales() {
        String usuario = jTextFieldUsuario.getText();
        String contraseña = new String(jPasswordFieldContraseña.getPassword());
        
        if (usuario.equals("admin") && contraseña.equals("1234")) {
            JOptionPane.showMessageDialog(this, 
                "Inicio de sesión exitoso", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (loginSuccessListener != null) {
                loginSuccessListener.run();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setLoginSuccessListener(Runnable listener) {
        this.loginSuccessListener = listener;
    }
}


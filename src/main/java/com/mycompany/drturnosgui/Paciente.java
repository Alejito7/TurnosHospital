/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drturnosgui;
// Clase paciente, representa un paciente

import java.io.Serializable;

public class Paciente extends ObraSocial implements Serializable {
   private String dni;
   private String nombre;
   private String telefono;

    public Paciente(String dni, String nombre, String telefono, String obraSocialNombre) {
        super(obraSocialNombre);
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String getObraSocial() {
        return super.getObraSocial();
    }

    @Override
    public void setObraSocial(String obraSocial) {
        super.setObraSocial(obraSocial);
    }
}
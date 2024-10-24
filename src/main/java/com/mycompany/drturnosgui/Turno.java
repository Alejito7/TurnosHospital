package com.mycompany.drturnosgui;
    // Clase turnos representa un turno y sus atributos
public class Turno extends Paciente {
   private String fecha;
   private String hora;
   private String motivo;

    public Turno(String dni, String nombre, String telefono, String obraSocial, String fecha, String hora, String motivo) {
        super(dni, nombre, telefono, obraSocial);
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String getDni() {
        return super.getDni();
    }

    @Override
    public void setDni(String dni) {
        super.setDni(dni);
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    @Override
    public String getTelefono() {
        return super.getTelefono();
    }

    @Override
    public void setTelefono(String telefono) {
        super.setTelefono(telefono);
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

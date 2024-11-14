package model;

import java.util.Date;

public class Recibo {
    private int idRecibo;
    private Trabajador trabajador; // Relación obligatoria con Trabajador
    private Date fecha;
    private double sueldoNeto;
    private int idTrabajador;

    public Recibo() {
    }

    public Recibo(int idRecibo, Trabajador trabajador, Date fecha, double sueldoNeto) {
        this.idRecibo = idRecibo;
        this.trabajador = trabajador; // Trabajador no puede ser nulo
        this.fecha = fecha;
        this.sueldoNeto = sueldoNeto;
    }

    // Getters y Setters
    public int getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(int idRecibo) {
        this.idRecibo = idRecibo;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador; // Trabajador no puede ser nulo
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSueldoNeto() {
        return sueldoNeto;
    }

    public void setSueldoNeto(double sueldoNeto) {
        this.sueldoNeto = sueldoNeto;
    }
}

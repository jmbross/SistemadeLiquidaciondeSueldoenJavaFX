package model;

public class Alicuota {
    private int idAlicuota;
    private String descripcion;
    private double porcentaje;
    private Trabajador trabajador; // Relación obligatoria con Trabajador

    public Alicuota() {
    }

    public Alicuota(int idAlicuota, String descripcion, double porcentaje, Trabajador trabajador) {
        this.idAlicuota = idAlicuota;
        this.descripcion = descripcion;
        this.porcentaje = porcentaje;
        this.trabajador = trabajador; // Trabajador no puede ser nulo
    }

    // Getters y Setters
    public int getIdAlicuota() {
        return idAlicuota;
    }

    public void setIdAlicuota(int idAlicuota) {
        this.idAlicuota = idAlicuota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador; // Trabajador no puede ser nulo
    }
}

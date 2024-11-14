package model;

import javafx.beans.property.*;

public class Alicuota {

    private IntegerProperty idAlicuota;
    private StringProperty descripcion;
    private DoubleProperty porcentaje;
    private IntegerProperty idTrabajador;

    // Constructor vacío
    public Alicuota() {
        this.idAlicuota = new SimpleIntegerProperty();
        this.descripcion = new SimpleStringProperty();
        this.porcentaje = new SimpleDoubleProperty();
        this.idTrabajador = new SimpleIntegerProperty();
    }

    // Constructor completo
    public Alicuota(int idAlicuota, String descripcion, double porcentaje, int idTrabajador) {
        this.idAlicuota = new SimpleIntegerProperty(idAlicuota);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.porcentaje = new SimpleDoubleProperty(porcentaje);
        this.idTrabajador = new SimpleIntegerProperty(idTrabajador);
    }

    // Constructor con tres parámetros: descripcion, porcentaje, idTrabajador
    public Alicuota(String descripcion, double porcentaje, int idTrabajador) {
        this.descripcion = new SimpleStringProperty(descripcion);
        this.porcentaje = new SimpleDoubleProperty(porcentaje);
        this.idTrabajador = new SimpleIntegerProperty(idTrabajador);
    }

    // Getters y Setters para las propiedades
    public int getIdAlicuota() {
        return idAlicuota.get();
    }

    public void setIdAlicuota(int idAlicuota) {
        this.idAlicuota.set(idAlicuota);
    }

    public IntegerProperty idAlicuotaProperty() {
        return idAlicuota;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public double getPorcentaje() {
        return porcentaje.get();
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje.set(porcentaje);
    }

    public DoubleProperty porcentajeProperty() {
        return porcentaje;
    }

    public int getIdTrabajador() {
        return idTrabajador.get();
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador.set(idTrabajador);
    }

    public IntegerProperty idTrabajadorProperty() {
        return idTrabajador;
    }

    // Método para calcular el monto del descuento
    public double calcularDescuento(double sueldoBruto) {
        return sueldoBruto * (porcentaje.get() / 100);
    }

    // Método toString
    @Override
    public String toString() {
        return "Alicuota{" +
                "idAlicuota=" + idAlicuota.get() +
                ", descripcion='" + descripcion.get() + '\'' +
                ", porcentaje=" + porcentaje.get() +
                ", idTrabajador=" + idTrabajador.get() +
                '}';
    }

    // Método equals basado en idAlicuota
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alicuota alicuota = (Alicuota) o;

        return idAlicuota.get() == alicuota.idAlicuota.get();
    }

    // Método hashCode basado en idAlicuota
    @Override
    public int hashCode() {
        return idAlicuota.get();
    }
}

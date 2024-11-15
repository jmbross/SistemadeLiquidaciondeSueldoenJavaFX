package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trabajador {
    private int idTrabajador;
    private SimpleStringProperty nombre;
    private StringProperty apellido;
    private StringProperty dni;
    private DoubleProperty sueldoBruto;
    private StringProperty email;
    private StringProperty telefono;
    private int idUsuario;
    private String nombreUsuario;

    // Constructor para crear un trabajador vacío
    public Trabajador() {
        this.nombre = new SimpleStringProperty();
        this.apellido = new SimpleStringProperty();
        this.dni = new SimpleStringProperty();
        this.sueldoBruto = new SimpleDoubleProperty();
        this.email = new SimpleStringProperty();
        this.telefono = new SimpleStringProperty();
    }

    // Constructor sin ID de trabajador, utilizado para crear un nuevo trabajador
    public Trabajador(String nombre, String apellido, String dni, double sueldoBruto, String email, String telefono) {
        this.nombre = new SimpleStringProperty();
        this.apellido = new SimpleStringProperty();
        this.dni = new SimpleStringProperty();
        this.sueldoBruto = new SimpleDoubleProperty();
        this.email = new SimpleStringProperty();
        this.telefono = new SimpleStringProperty();
    }

    // Constructor completo con todos los campos
    public Trabajador(int idTrabajador, String nombre, String apellido, String dni, double sueldoBruto, String email, String telefono, int idUsuario) {
        this.idTrabajador = idTrabajador;
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.dni = new SimpleStringProperty(dni);
        this.sueldoBruto = new SimpleDoubleProperty(sueldoBruto);
        this.email = new SimpleStringProperty(email);
        this.telefono = new SimpleStringProperty(telefono);
        this.idUsuario = idUsuario;
    }

    // Constructor con solo idTrabajador, nombre y apellido
    public Trabajador(int idTrabajador, String nombre, String apellido) {
        this.idTrabajador = idTrabajador;
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.dni = new SimpleStringProperty(""); // dni vacío por defecto
        this.sueldoBruto = new SimpleDoubleProperty(0.0); // sueldoBruto por defecto
        this.email = new SimpleStringProperty(""); // email vacío por defecto
        this.telefono = new SimpleStringProperty(""); // telefono vacío por defecto
        this.idUsuario = 0; // idUsuario por defecto
    }

    public Trabajador(int idTrabajador, String nombre, String apellido, double sueldoBruto) {
        this.idTrabajador = idTrabajador;
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.sueldoBruto = new SimpleDoubleProperty(0.0); // sueldoBruto por defecto
    }

    // Getters y Setters
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getApellido() { return apellido.get(); }
    public void setApellido(String apellido) { this.apellido.set(apellido); }
    public StringProperty apellidoProperty() { return apellido; }

    public String getDni() { return dni.get(); }
    public void setDni(String dni) { this.dni.set(dni); }
    public StringProperty dniProperty() { return dni; }

    public double getSueldoBruto() { return sueldoBruto.get(); }
    public void setSueldoBruto(double sueldoBruto) { this.sueldoBruto.set(sueldoBruto); }
    public DoubleProperty sueldoBrutoProperty() { return sueldoBruto; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
    public StringProperty telefonoProperty() { return telefono; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}

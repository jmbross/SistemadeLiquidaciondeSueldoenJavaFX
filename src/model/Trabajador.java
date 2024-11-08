package model;

public class Trabajador {
    private int idUsuario;
    private int idTrabajador;
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private double sueldoBruto;
    private String email;
    private String telefono;
    private Usuario usuario; // Relación obligatoria con Usuario


    // Constructor sin argumentos
    public Trabajador() {
    }

    public Trabajador(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Constructor con todos los parámetros
    public Trabajador(String nombre, String apellido, String dni, double sueldoBruto, String email, String telefono, int idUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.sueldoBruto = sueldoBruto;
        this.email = email;
        this.telefono = telefono;
        this.idUsuario = idUsuario;
    }

    // Constructor adicional para asignar un id
    public Trabajador(int idTrabajador, String nombre, String apellido, String dni, double sueldoBruto, String email, String telefono, int idUsuario) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.sueldoBruto = sueldoBruto;
        this.email = email;
        this.telefono = telefono;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters

    public int getId() { return id; }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public double getSueldoBruto() {
        return sueldoBruto;
    }

    public void setSueldoBruto(double sueldoBruto) {
        this.sueldoBruto = sueldoBruto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

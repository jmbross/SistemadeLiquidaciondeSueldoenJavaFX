package model;

public class UsuarioTrabajador {
    private int idUsuarioTrabajador;
    private Usuario usuario; // Relación obligatoria con Usuario
    private Trabajador trabajador; // Relación obligatoria con Trabajador

    public UsuarioTrabajador() {
    }

    public UsuarioTrabajador(int idUsuarioTrabajador, Usuario usuario, Trabajador trabajador) {
        this.idUsuarioTrabajador = idUsuarioTrabajador;
        this.usuario = usuario; // Usuario no puede ser nulo
        this.trabajador = trabajador; // Trabajador no puede ser nulo
    }

    // Getters y Setters
    public int getIdUsuarioTrabajador() {
        return idUsuarioTrabajador;
    }

    public void setIdUsuarioTrabajador(int idUsuarioTrabajador) {
        this.idUsuarioTrabajador = idUsuarioTrabajador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; // Usuario no puede ser nulo
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador; // Trabajador no puede ser nulo
    }
}

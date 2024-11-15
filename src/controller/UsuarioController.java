package controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Trabajador;
import model.Usuario;
import service.UsuarioService;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    @FXML
    private Label roleLabel;
    private UsuarioService usuarioService;
    private Trabajador trabajador;
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public void initialize() {
        // Asegúrate de que roleLabel esté definido correctamente
        roleLabel.setText("Rol: " + LoginController.rolActual);
    }

    // Método para asignar el rol del usuario al label
    public void setCurrentUserRole(String role) {
        if (roleLabel != null) {
            roleLabel.setText("Rol: " + role);  // Se asigna el texto al label
        } else {
            System.err.println("Error: roleLabel is not initialized.");
        }
    }

    // Método para cerrar la ventana actual (ejemplo de redirección a otra vista)
    public void cerrarVentana() {
        Stage stage = (Stage) roleLabel.getScene().getWindow();
        stage.close();
    }

    // Método para agregar un nuevo usuario
    public void agregarUsuario(String nombre, String apellido, String dni, String matricula, String email, String contrasena, String rol) {
        Usuario usuario = new Usuario(trabajador.getIdUsuario(), "", "");
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setMatricula(matricula);
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
        usuario.setRol(rol);

        try {
            usuarioService.addUsuario(usuario);
            System.out.println("Usuario agregado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
        }
    }

    // Método para listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioService.getAllUsuarios();
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            return null;
        }
    }

    // Método para modificar un usuario
    public void modificarUsuario(int idUsuario, String nombre, String apellido, String dni, String matricula, String email, String contrasena, String rol) {
        Usuario usuario = new Usuario(trabajador.getIdUsuario(), "", "");
        usuario.setIdUsuario(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setMatricula(matricula);
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
        usuario.setRol(rol);

        try {
            usuarioService.updateUsuario(usuario);
            System.out.println("Usuario modificado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
        }
    }

    // Método para eliminar un usuario
    public void eliminarUsuario(int idUsuario) {
        try {
            usuarioService.deleteUsuario(idUsuario);
            System.out.println("Usuario eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    // Método para listar nombres completos de usuarios
    public List<String> listarNombresCompletosUsuarios() {
        try {
            return usuarioService.obtenerNombresCompletosUsuarios();
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres completos de usuarios: " + e.getMessage());
            return null;
        }
    }

    // Método para obtener el ID de un usuario a partir de su nombre completo
    public int obtenerIdPorNombreCompleto(String nombreCompleto) {
        try {
            return usuarioService.obtenerIdUsuarioPorNombreCompleto(nombreCompleto);
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario por nombre completo: " + e.getMessage());
            return -1;
        }
    }

    // Método para eliminar un usuario dado su nombre completo
    public void eliminarUsuarioPorNombreCompleto(String nombreCompleto) {
        int idUsuario = obtenerIdPorNombreCompleto(nombreCompleto);
        if (idUsuario != -1) {
            eliminarUsuario(idUsuario);
        } else {
            System.err.println("No se encontró el usuario para eliminar: " + nombreCompleto);
        }
    }

    // Método para modificar un usuario dado su nombre completo
    public void modificarUsuarioPorNombreCompleto(String nombreCompleto, String nombre, String apellido, String dni, String matricula, String email, String contrasena, String rol) {
        int idUsuario = obtenerIdPorNombreCompleto(nombreCompleto);
        if (idUsuario != -1) {
            modificarUsuario(idUsuario, nombre, apellido, dni, matricula, email, contrasena, rol);
        } else {
            System.err.println("No se encontró el usuario para modificar: " + nombreCompleto);
        }
    }

}

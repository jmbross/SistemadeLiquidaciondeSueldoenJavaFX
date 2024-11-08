package service;

import database.DatabaseConnection;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    // Método para agregar un nuevo usuario
    public void addUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (nombre, apellido, dni, matricula, email, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getDni());
            statement.setString(4, usuario.getMatricula());
            statement.setString(5, usuario.getEmail());
            statement.setString(6, usuario.getContrasena());
            statement.setString(7, usuario.getRol());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            throw e;
        }
    }

    // Método para obtener todos los usuarios
    public List<Usuario> getAllUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuario";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultSet.getInt("id_usuario"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                usuario.setDni(resultSet.getString("dni"));
                usuario.setMatricula(resultSet.getString("matricula"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setContrasena(resultSet.getString("contrasena"));
                usuario.setRol(resultSet.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            throw e;
        }
        return usuarios;
    }

    // Método para obtener un usuario por su ID
    public Usuario getUsuarioById(int idUsuario) throws SQLException {
        String query = "SELECT * FROM usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUsuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(resultSet.getInt("id_usuario"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setApellido(resultSet.getString("apellido"));
                    usuario.setDni(resultSet.getString("dni"));
                    usuario.setMatricula(resultSet.getString("matricula"));
                    usuario.setEmail(resultSet.getString("email"));
                    usuario.setContrasena(resultSet.getString("contrasena"));
                    usuario.setRol(resultSet.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            throw e;
        }
        return usuario;
    }

    // Método para actualizar un usuario
    public void updateUsuario(Usuario usuario) throws SQLException {
        String query = "UPDATE usuario SET nombre = ?, apellido = ?, dni = ?, matricula = ?, email = ?, contrasena = ?, rol = ? WHERE id_usuario = ?";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getDni());
            statement.setString(4, usuario.getMatricula());
            statement.setString(5, usuario.getEmail());
            statement.setString(6, usuario.getContrasena());
            statement.setString(7, usuario.getRol());
            statement.setInt(8, usuario.getIdUsuario());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un usuario por su ID
    public void deleteUsuario(int idUsuario) throws SQLException {
        String query = "DELETE FROM usuario WHERE id_usuario = ?";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUsuario);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            throw e;
        }
    }




    // Método para obtener los nombres de todos los usuarios
    public List<String> obtenerNombresUsuarios() throws SQLException {
        List<String> nombres = new ArrayList<>();
        String query = "SELECT nombre FROM usuario";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                nombres.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de usuarios: " + e.getMessage());
            throw e;
        }
        return nombres;
    }

    // Método para obtener el ID de un usuario dado su nombre
    public int obtenerIdUsuarioPorNombre(String nombre) throws SQLException {
        String query = "SELECT id_usuario FROM usuario WHERE nombre = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario por nombre: " + e.getMessage());
            throw e;
        }
        return -1;
    }

    // Método para obtener la lista de nombres completos de los usuarios
    public List<String> obtenerNombresCompletosUsuarios() throws SQLException {
        List<String> nombresCompletos = new ArrayList<>();
        String query = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM usuario";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                nombresCompletos.add(resultSet.getString("nombre_completo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres completos de usuarios: " + e.getMessage());
            throw e;
        }
        return nombresCompletos;
    }

    // Método para obtener el ID de un usuario dado su nombre completo
    public int obtenerIdUsuarioPorNombreCompleto(String nombreCompleto) throws SQLException {
        String query = "SELECT id_usuario FROM usuario WHERE CONCAT(nombre, ' ', apellido) = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombreCompleto);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario por nombre completo: " + e.getMessage());
            throw e;
        }
        return -1;
    }




}

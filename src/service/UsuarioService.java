package service;

import database.DatabaseConnection;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    // Método para agregar un nuevo usuario
    public void addUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (nombre, apellido, dni, matricula, email, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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

    // Método para obtener un usuario por su ID
    public Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException {
        String query = "SELECT * FROM usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
            throw e;  // Rethrow the exception to be handled by the caller
        }
        return usuario;
    }

    // Método para obtener todos los usuarios
    public List<Usuario> getAllUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuario";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
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

    // Método para obtener todos los usuarios asignados a un trabajador
    public List<Usuario> getUsuariosByTrabajador(int idTrabajador) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT u.* FROM usuario u INNER JOIN usuario_trabajador ut ON u.id_usuario = ut.id_usuario WHERE ut.id_trabajador = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTrabajador);

            try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios de trabajador: " + e.getMessage());
            throw e;
        }
        return usuarios;
    }

    // Método para actualizar un usuario
    public void updateUsuario(Usuario usuario) throws SQLException {
        String query = "UPDATE usuario SET nombre = ?, apellido = ?, dni = ?, matricula = ?, email = ?, contrasena = ?, rol = ? WHERE id_usuario = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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

    // Método para eliminar un usuario
    public void deleteUsuario(int idUsuario) throws SQLException {
        String query = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUsuario);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            throw e;
        }
    }

    // Método para obtener los nombres completos de todos los usuarios
    public List<String> obtenerNombresCompletosUsuarios() throws SQLException {
        List<String> nombresCompletos = new ArrayList<>();
        String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM usuario";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                nombresCompletos.add(resultSet.getString("nombre_completo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres completos de usuarios: " + e.getMessage());
            throw e;
        }

        return nombresCompletos;
    }

    // Método para obtener el ID de usuario por nombre completo
    public Integer obtenerIdUsuarioPorNombreCompleto(String nombreCompleto) throws SQLException {
        Integer idUsuario = null;
        String sql = "SELECT id_usuario FROM usuario WHERE CONCAT(nombre, ' ', apellido) = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nombreCompleto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idUsuario = resultSet.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario por nombre completo: " + e.getMessage());
            throw e;
        }
        return idUsuario;
    }

    // Método para obtener el ID del usuario logueado (ejemplo)
    public Integer obtenerIdUsuarioLogueado() {
        // Aquí deberías implementar la lógica de obtener el ID del usuario logueado.
        // Por ejemplo, usando la sesión del usuario o un contexto de seguridad.
        // Este es un ejemplo simple que devuelve un ID estático para fines de demostración.
        return 1;  // Esto debería ser reemplazado por la lógica de autenticación real.
    }
}


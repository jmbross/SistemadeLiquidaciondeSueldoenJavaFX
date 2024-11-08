package service;

import database.DatabaseConnection;
import model.Trabajador;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorService {

    private final UsuarioService usuarioService;

    public TrabajadorService() {
        this.usuarioService = new UsuarioService(); // Asume que el UsuarioService puede usarse directamente
    }

    // Verificar existencia de Trabajador por DNI
    public boolean existeDni(String dni) {
        String query = "SELECT COUNT(*) FROM trabajador WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dni);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de DNI: " + e.getMessage());
        }

        return false;
    }

    // Método para agregar un Trabajador
    public void addTrabajador(Trabajador trabajador) throws SQLException {
        String query = "INSERT INTO trabajador (nombre, apellido, dni, sueldo_bruto, email, telefono, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trabajador.getNombre());
            statement.setString(2, trabajador.getApellido());
            statement.setString(3, trabajador.getDni());
            statement.setDouble(4, trabajador.getSueldoBruto());
            statement.setString(5, trabajador.getEmail());
            statement.setString(6, trabajador.getTelefono());
            statement.setInt(7, trabajador.getUsuario().getIdUsuario());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar trabajador: " + e.getMessage());
            throw e;
        }
    }

    // Nuevo método para guardar un trabajador
    public boolean guardarTrabajador(Trabajador trabajador) {
        try {
            addTrabajador(trabajador);
            return true; // Si el trabajador se guarda correctamente, devuelve true
        } catch (SQLException e) {
            System.err.println("Error al guardar el trabajador: " + e.getMessage());
            return false;
        }
    }

    // Método para obtener todos los Trabajadores
    public List<Trabajador> obtenerTodosLosTrabajadores() throws SQLException {
        List<Trabajador> trabajadores = new ArrayList<>();
        String query = "SELECT * FROM trabajador";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Trabajador trabajador = new Trabajador();
                trabajador.setIdTrabajador(resultSet.getInt("id_trabajador"));
                trabajador.setNombre(resultSet.getString("nombre"));
                trabajador.setApellido(resultSet.getString("apellido"));
                trabajador.setDni(resultSet.getString("dni"));
                trabajador.setSueldoBruto(resultSet.getDouble("sueldo_bruto"));
                trabajador.setEmail(resultSet.getString("email"));
                trabajador.setTelefono(resultSet.getString("telefono"));

                // Obtiene y asigna el Usuario asociado
                int idUsuario = resultSet.getInt("id_usuario");
                Usuario usuario = usuarioService.getUsuarioById(idUsuario);
                trabajador.setUsuario(usuario);

                trabajadores.add(trabajador);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener trabajadores: " + e.getMessage());
            throw e;
        }
        return trabajadores;
    }

    // Método para obtener un Trabajador por su DNI
    public Trabajador obtenerTrabajadorPorDni(String dni) {
        Trabajador trabajador = null;
        String query = "SELECT * FROM trabajador WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dni);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    trabajador = new Trabajador();
                    trabajador.setIdTrabajador(resultSet.getInt("id_trabajador"));
                    trabajador.setNombre(resultSet.getString("nombre"));
                    trabajador.setApellido(resultSet.getString("apellido"));
                    trabajador.setDni(resultSet.getString("dni"));
                    trabajador.setSueldoBruto(resultSet.getDouble("sueldo_bruto"));
                    trabajador.setEmail(resultSet.getString("email"));
                    trabajador.setTelefono(resultSet.getString("telefono"));

                    int idUsuario = resultSet.getInt("id_usuario");
                    Usuario usuario = usuarioService.getUsuarioById(idUsuario);
                    trabajador.setUsuario(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener trabajador por DNI: " + e.getMessage());
        }
        return trabajador;
    }

    // Método para actualizar un Trabajador existente
    public boolean actualizarTrabajador(String dni, String nombre, String apellido, double sueldoBruto) throws SQLException {
        String query = "UPDATE trabajador SET nombre = ?, apellido = ?, sueldo_bruto = ? WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setDouble(3, sueldoBruto);
            statement.setString(4, dni);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar trabajador: " + e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un Trabajador por su ID
    public boolean deleteTrabajadorById(int idTrabajador) throws SQLException {
        String query = "DELETE FROM trabajador WHERE id_trabajador = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTrabajador);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar trabajador: " + e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un Trabajador por su DNI
    public boolean eliminarTrabajadorPorDni(String dni) {
        String sql = "DELETE FROM trabajador WHERE dni = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dni);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar trabajador por DNI: " + e.getMessage());
            return false;
        }
    }
}

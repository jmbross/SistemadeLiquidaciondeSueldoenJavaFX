package service;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Alicuota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AlicuotaService {

    static Connection connection;

    private AlicuotaService alicuotaService;

    public AlicuotaService() {

    }

    public void AlicuotaController() {
        this.alicuotaService = new AlicuotaService();
    }

    // Constructor que recibe la conexión de base de datos
    public AlicuotaService(Connection connection) {
        this.connection = connection;
    }

    public static List<Alicuota> obtenerAlicuotasPorTrabajador(int idTrabajador) {
        List<Alicuota> alicuotas = new ArrayList<>();
        String sql = "SELECT id_alicuota, descripcion, porcentaje, id_trabajador FROM alicuota WHERE id_trabajador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idAlicuota = rs.getInt("id_alicuota");
                String descripcion = rs.getString("descripcion");
                double porcentaje = rs.getDouble("porcentaje");
                int trabajadorId = rs.getInt("id_trabajador");

                Alicuota alicuota = new Alicuota(idAlicuota, descripcion, porcentaje, trabajadorId);
                alicuotas.add(alicuota);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alicuotas;
    }




    public void addAlicuota(Alicuota alicuota) throws SQLException {
        String query = "INSERT INTO alicuota (descripcion, porcentaje, id_trabajador) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, alicuota.getDescripcion());
            statement.setDouble(2, alicuota.getPorcentaje());
            statement.setInt(3, alicuota.getIdTrabajador()); // Usa getIdTrabajador() en lugar de getTrabajador()
            statement.executeUpdate();
        }
    }

    public List<Alicuota> getAllAlicuotas() throws SQLException {
        List<Alicuota> alicuotas = new ArrayList<>();
        String query = "SELECT * FROM alicuota";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Alicuota alicuota = new Alicuota();
                alicuota.setIdAlicuota(resultSet.getInt("id_alicuota"));
                alicuota.setDescripcion(resultSet.getString("descripcion"));
                alicuota.setPorcentaje(resultSet.getDouble("porcentaje"));
                // Se debe cargar el trabajador asociado a esta alícuota
                // Esto puede requerir otro servicio o una consulta adicional
                alicuotas.add(alicuota);
            }
        }
        return alicuotas;
    }

    // Método para actualizar una alícuota
    public boolean updateAlicuota(Alicuota alicuota) {
        String sql = "UPDATE alicuota SET descripcion = ?, porcentaje = ?, id_trabajador = ? WHERE id_alicuota = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Establecer los valores en el PreparedStatement
            stmt.setString(1, alicuota.getDescripcion()); // Establecer descripción
            stmt.setDouble(2, alicuota.getPorcentaje()); // Establecer porcentaje
            stmt.setInt(3, alicuota.getIdTrabajador()); // Establecer id_trabajador
            stmt.setInt(4, alicuota.getIdAlicuota()); // Establecer id_alicuota (para identificar la fila a actualizar)

            // Ejecutar la actualización
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Retorna true si la actualización fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre un error
        }
    }

    // Método para eliminar una alícuota
    public boolean deleteAlicuota(int idAlicuota) {
        String sql = "DELETE FROM alicuota WHERE id_alicuota = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Establecer el valor del idAlicuota
            stmt.setInt(1, idAlicuota);

            // Ejecutar la eliminación
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; // Retorna true si la eliminación fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre un error
        }
    }

    // Método para listar todas las alícuotas
    public ObservableList<Alicuota> listarAlicuotas() {
        ObservableList<Alicuota> alicuotas = FXCollections.observableArrayList();
        String query = "SELECT * FROM alicuota"; // Asegúrate de usar el nombre correcto de la tabla

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idAlicuota = rs.getInt("id_alicuota");
                String descripcion = rs.getString("descripcion");
                double porcentaje = rs.getDouble("porcentaje");
                int idTrabajador = rs.getInt("id_trabajador");

                // Crear la alícuota y agregarla a la lista
                Alicuota alicuota = new Alicuota(idAlicuota, descripcion, porcentaje, idTrabajador);
                alicuotas.add(alicuota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alicuotas;
    }


}
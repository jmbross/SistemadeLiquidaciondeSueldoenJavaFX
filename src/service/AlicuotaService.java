package service;

import database.DatabaseConnection;
import model.Alicuota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlicuotaService {

    public void addAlicuota(Alicuota alicuota) throws SQLException {
        String query = "INSERT INTO alicuota (descripcion, porcentaje, id_trabajador) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, alicuota.getDescripcion());
            statement.setDouble(2, alicuota.getPorcentaje());
            statement.setInt(3, alicuota.getTrabajador().getIdTrabajador()); // Asumiendo que ya tienes un trabajador
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

    // Métodos adicionales como getAlicuotaById, updateAlicuota, deleteAlicuota, etc.
}

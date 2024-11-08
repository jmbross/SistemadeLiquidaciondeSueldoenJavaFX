package service;

import database.DatabaseConnection;
import model.Recibo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReciboService {

    public void addRecibo(Recibo recibo) throws SQLException {
        String query = "INSERT INTO recibo (id_trabajador, fecha, sueldo_neto) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, recibo.getTrabajador().getIdTrabajador()); // Asumiendo que ya tienes un trabajador
            statement.setDate(2, new java.sql.Date(recibo.getFecha().getTime()));
            statement.setDouble(3, recibo.getSueldoNeto());
            statement.executeUpdate();
        }
    }

    public List<Recibo> getAllRecibos() throws SQLException {
        List<Recibo> recibos = new ArrayList<>();
        String query = "SELECT * FROM recibo";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Recibo recibo = new Recibo();
                recibo.setIdRecibo(resultSet.getInt("id_recibo"));
                // Se debe cargar el trabajador asociado a este recibo
                // Esto puede requerir otro servicio o una consulta adicional
                recibo.setFecha(resultSet.getDate("fecha"));
                recibo.setSueldoNeto(resultSet.getDouble("sueldo_neto"));
                recibos.add(recibo);
            }
        }
        return recibos;
    }

    // Métodos adicionales como getReciboById, updateRecibo, deleteRecibo, etc.
}

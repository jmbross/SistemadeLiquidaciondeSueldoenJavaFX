package database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_sueldos";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    // Obtener la conexión
    public static Connection getConnection() {
        Connection connection = connectionThreadLocal.get();

        if (connection == null || !isConnectionValid(connection)) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                logger.info("Conexión exitosa a la base de datos.");
                connectionThreadLocal.set(connection); // Guardamos la conexión en ThreadLocal
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error de conexión a la base de datos.", e);
            }
        }
        return connection;
    }

    // Cerrar la conexión
    public static void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.close();
                connectionThreadLocal.remove(); // Limpiamos el ThreadLocal
                logger.info("Conexión cerrada.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al cerrar la conexión.", e);
            }
        }
    }

    // Verificar si la conexión es válida
    private static boolean isConnectionValid(Connection connection) {
        try {
            return connection != null && connection.isValid(2); // Timeout de 2 segundos para comprobar la validez
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al validar la conexión.", e);
            return false;
        }
    }
}


package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_sueldos";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection connection = connectionThreadLocal.get();

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");
                connectionThreadLocal.set(connection); // Guardamos la conexión en ThreadLocal
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error de conexión a la base de datos.");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.close();
                connectionThreadLocal.remove(); // Limpiamos el ThreadLocal
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar la conexión.");
            }
        }
    }
}

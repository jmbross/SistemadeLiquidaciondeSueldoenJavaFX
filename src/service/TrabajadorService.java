package service;

import database.DatabaseConnection;
import model.Trabajador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Alicuota;
import java.sql.*;

import static service.AlicuotaService.connection;

/**
 * Servicio para realizar operaciones CRUD en la tabla Trabajador.
 */
public class TrabajadorService {


    private Connection connection;

    public TrabajadorService() {
        // Conexión a la base de datos
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_sueldos", "root", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean agregarTrabajador(Trabajador trabajador) {
        String sql = "INSERT INTO trabajador (nombre, apellido, dni, sueldo_bruto, email, telefono, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getApellido());
            stmt.setString(3, trabajador.getDni());
            stmt.setDouble(4, trabajador.getSueldoBruto());
            stmt.setString(5, trabajador.getEmail());
            stmt.setString(6, trabajador.getTelefono());
            stmt.setInt(7, trabajador.getIdUsuario());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Obtener el ID generado para el trabajador
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        trabajador.setIdTrabajador(generatedKeys.getInt(1));
                    }
                }

                // Crear alícuotas por defecto para el trabajador
                crearAlicuotasPorDefecto(connection, trabajador.getIdTrabajador());

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para listar todos los trabajadores
    public ObservableList<Trabajador> listarTrabajadores() {
        ObservableList<Trabajador> trabajadores = FXCollections.observableArrayList();
        String query = "SELECT * FROM trabajador"; // Asegúrate de usar el nombre correcto de la tabla

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idTrabajador = rs.getInt("id_trabajador");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                Trabajador trabajador = new Trabajador(idTrabajador, nombre, apellido);
                trabajadores.add(trabajador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trabajadores;
    }

    // Método para crear las alícuotas por defecto
    private void crearAlicuotasPorDefecto(Connection connection, int idTrabajador) throws SQLException {
        String sqlAlicuota = "INSERT INTO alicuota (descripcion, porcentaje, id_trabajador) VALUES (?, ?, ?)";

        try (PreparedStatement stmtAlicuota = connection.prepareStatement(sqlAlicuota)) {
            // Alícuota de Jubilacion
            stmtAlicuota.setString(1, "Jubilacion");
            stmtAlicuota.setDouble(2, 10.0);
            stmtAlicuota.setInt(3, idTrabajador);
            stmtAlicuota.executeUpdate();

            // Alícuota de Ley 4350
            stmtAlicuota.setString(1, "Ley 4350");
            stmtAlicuota.setDouble(2, 5.0);
            stmtAlicuota.setInt(3, idTrabajador);
            stmtAlicuota.executeUpdate();

            // Alícuota de Descuentos Varios
            stmtAlicuota.setString(1, "Descuentos Varios");
            stmtAlicuota.setDouble(2, 2.0);
            stmtAlicuota.setInt(3, idTrabajador);
            stmtAlicuota.executeUpdate();
        }
    }


    /**
     * Modifica los datos de un trabajador existente en la base de datos.
     * @param trabajador el objeto Trabajador con los datos modificados
     * @return true si el trabajador se actualizó con éxito, false en caso contrario
     */
    public boolean modificarTrabajador(Trabajador trabajador) {
        String sql = "UPDATE trabajador SET nombre = ?, apellido = ?, dni = ?, sueldo_bruto = ?, email = ?, telefono = ?, id_usuario = ? WHERE id_trabajador = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getApellido());
            stmt.setString(3, trabajador.getDni());
            stmt.setDouble(4, trabajador.getSueldoBruto());
            stmt.setString(5, trabajador.getEmail());
            stmt.setString(6, trabajador.getTelefono());
            stmt.setInt(7, trabajador.getIdUsuario());
            stmt.setInt(8, trabajador.getIdTrabajador());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un trabajador de la base de datos por su ID.
     * @param idTrabajador el ID del trabajador a eliminar
     * @return true si el trabajador se eliminó con éxito, false en caso contrario
     */
    public boolean eliminarTrabajador(int idTrabajador) {
        String sql = "DELETE FROM trabajador WHERE id_trabajador = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajador);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene un ObservableList con todos los trabajadores de un usuario específico.
     * @param idUsuario el ID del usuario asociado a los trabajadores
     * @return una lista observable de trabajadores
     */
    public ObservableList<Trabajador> listarTrabajadoresPorUsuario(int idUsuario) {
        ObservableList<Trabajador> listaTrabajadores = FXCollections.observableArrayList();
        String sql = "SELECT * FROM trabajador WHERE id_usuario = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
                    trabajador.setNombre(rs.getString("nombre"));
                    trabajador.setApellido(rs.getString("apellido"));
                    trabajador.setDni(rs.getString("dni"));
                    trabajador.setSueldoBruto(rs.getDouble("sueldo_bruto"));
                    trabajador.setEmail(rs.getString("email"));
                    trabajador.setTelefono(rs.getString("telefono"));
                    trabajador.setIdUsuario(rs.getInt("id_usuario"));

                    listaTrabajadores.add(trabajador);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTrabajadores;
    }

    /**
     * Busca un trabajador en la base de datos por su ID.
     * @param idTrabajador el ID del trabajador a buscar
     * @return el objeto Trabajador si se encuentra, o null si no existe
     */
    public Trabajador buscarTrabajadorPorId(int idTrabajador) {
        String sql = "SELECT * FROM trabajador WHERE id_trabajador = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTrabajador);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
                    trabajador.setNombre(rs.getString("nombre"));
                    trabajador.setApellido(rs.getString("apellido"));
                    trabajador.setDni(rs.getString("dni"));
                    trabajador.setSueldoBruto(rs.getDouble("sueldo_bruto"));
                    trabajador.setEmail(rs.getString("email"));
                    trabajador.setTelefono(rs.getString("telefono"));
                    trabajador.setIdUsuario(rs.getInt("id_usuario"));
                    return trabajador;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Método para obtener todos los trabajadores sin un idUsuario
    public ObservableList<Trabajador> obtenerTodosLosTrabajadores() {
        ObservableList<Trabajador> listaTrabajadores = FXCollections.observableArrayList();
        String sql = "SELECT * FROM trabajador";  // Se elimina el filtro por id_usuario

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trabajador trabajador = new Trabajador();
                trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
                trabajador.setNombre(rs.getString("nombre"));
                trabajador.setApellido(rs.getString("apellido"));
                trabajador.setDni(rs.getString("dni"));
                trabajador.setSueldoBruto(rs.getDouble("sueldo_bruto"));
                trabajador.setEmail(rs.getString("email"));
                trabajador.setTelefono(rs.getString("telefono"));
                trabajador.setIdUsuario(rs.getInt("id_usuario"));

                listaTrabajadores.add(trabajador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaTrabajadores;
    }

    public double calcularSueldoNeto(Trabajador trabajador) {
        double sueldoBruto = trabajador.getSueldoBruto();
        double totalPorcentaje = AlicuotaService.obtenerAlicuotasPorTrabajador(trabajador.getIdTrabajador())
                .stream()
                .mapToDouble(Alicuota::getPorcentaje)
                .sum();
        return sueldoBruto * (1 - totalPorcentaje / 100);
    }
}

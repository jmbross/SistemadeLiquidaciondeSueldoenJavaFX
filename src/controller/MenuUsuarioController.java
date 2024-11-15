package controller;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Trabajador;
import service.TrabajadorService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuUsuarioController {

    @FXML
    private TableView<Trabajador> trabajadorTable;
    @FXML
    private TableColumn<Trabajador, Integer> colIdTrabajador;
    @FXML
    private TableColumn<Trabajador, String> colNombre;
    @FXML
    private TableColumn<Trabajador, String> colApellido;
    @FXML
    private TableColumn<Trabajador, Double> colSueldoBruto;
    @FXML
    private Button cerrarSesionButton;
    @FXML
    private Button agregarTrabajadorButton;
    @FXML
    private Button modificarTrabajadorButton;
    @FXML
    private Button eliminarTrabajadorButton;
    @FXML
    private Label roleLabel;
    private String currentUserRole;

    private ObservableList<Trabajador> trabajadoresList;
    private TrabajadorService trabajadorService;
    private int idUsuarioSesion;

    public void setIdUsuarioSesion(int id) {
        this.idUsuarioSesion = id;
    }

    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }
    @FXML
    public void initialize() {
        trabajadorService = new TrabajadorService();

        // Configuración de las columnas de la tabla
        colIdTrabajador.setCellValueFactory(new PropertyValueFactory<>("idTrabajador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSueldoBruto.setCellValueFactory(new PropertyValueFactory<>("sueldoBruto"));

        trabajadoresList = FXCollections.observableArrayList();
        trabajadorTable.setItems(trabajadoresList);

        cargarTrabajadores(); // Inicializar la tabla con datos
        listarTrabajadores();
    }

    public void cargarTrabajadores() {
        trabajadoresList.clear();
        String query = "SELECT * FROM trabajador WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuarioSesion);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trabajador trabajador = new Trabajador(
                        rs.getInt("id_trabajador"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDouble("sueldo_bruto")
                );
                trabajadoresList.add(trabajador);
            }
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los trabajadores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void agregarTrabajador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TrabajadorView.fxml"));
            Parent root = loader.load();
            TrabajadorController controller = loader.getController();
            controller.setModoEdicion(true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Trabajador");
            stage.showAndWait();

            cargarTrabajadores(); // Recargar la lista después de agregar
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la vista para agregar trabajador: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modificarTrabajador() {
        Trabajador trabajadorSeleccionado = trabajadorTable.getSelectionModel().getSelectedItem();
        if (trabajadorSeleccionado == null) {
            showAlert("Error", "Por favor, seleccione un trabajador para modificar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TrabajadorView.fxml"));
            Parent root = loader.load();

            TrabajadorController controller = loader.getController();
            controller.setModoEdicion(true); // Establecer el modo de edición
            controller.setTrabajador(trabajadorSeleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Trabajador");
            stage.showAndWait();

            cargarTrabajadores(); // Recargar la lista después de modificar
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la vista para modificar trabajador: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarTrabajador() {
        Trabajador trabajadorSeleccionado = trabajadorTable.getSelectionModel().getSelectedItem();
        if (trabajadorSeleccionado == null) {
            showAlert("Advertencia", "Seleccione un trabajador para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Desea eliminar este trabajador?");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            trabajadorService.eliminarTrabajador(trabajadorSeleccionado.getIdTrabajador());
            trabajadoresList.remove(trabajadorSeleccionado); // Actualizar la lista local
            showAlert("Éxito", "Trabajador eliminado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    public MenuUsuarioController() {
        this.trabajadorService = new TrabajadorService();  // Crear el servicio de trabajadores
    }

    public void listarTrabajadores() {
        // Obtener la lista de trabajadores desde el servicio o repositorio
        ObservableList<Trabajador> trabajadores = FXCollections.observableArrayList(trabajadorService.obtenerTodosLosTrabajadores());

        // Mostrar la lista en el TableView
        trabajadorTable.setItems(trabajadores);
    }

    @FXML
    private void cerrarSesion() throws IOException {
        // Verificar si el botón está inicializado
        if (cerrarSesionButton != null) {
            Stage stage = (Stage) cerrarSesionButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // Si el botón no está inicializado, mostrar un mensaje de error
            System.out.println("Error: El botón 'cerrarSesionButton' no está inicializado.");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



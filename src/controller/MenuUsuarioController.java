package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import model.Trabajador;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuUsuarioController {

    @FXML
    private TableView<Trabajador> trabajadorTable;
    @FXML
    private TableColumn<Trabajador, String> nombreColumn;
    @FXML
    private TableColumn<Trabajador, String> apellidoColumn;
    @FXML
    private TableColumn<Trabajador, String> dniColumn;
    @FXML
    private TableColumn<Trabajador, Double> sueldoColumn;
    @FXML
    private TableColumn<Trabajador, String> emailColumn;
    @FXML
    private TableColumn<Trabajador, String> telefonoColumn;

    @FXML
    private Button cerrarSesionButton;
    @FXML
    private Button agregarTrabajadorButton;
    @FXML
    private Button modificarTrabajadorButton;
    @FXML
    private Button eliminarTrabajadorButton;

    private int idUsuarioSesion;

    public void setIdUsuarioSesion(int id) {
        this.idUsuarioSesion = id;
    }

    // Método para listar trabajadores
    @FXML
    private void listarTrabajadores() {
        List<Trabajador> trabajadores = obtenerTrabajadoresPorUsuario(idUsuarioSesion);
        trabajadorTable.getItems().setAll(trabajadores);
        configurarColumnas();
    }

    private void configurarColumnas() {
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        apellidoColumn.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        dniColumn.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
        sueldoColumn.setCellValueFactory(cellData -> cellData.getValue().sueldoBrutoProperty().asObject());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
    }

    private List<Trabajador> obtenerTrabajadoresPorUsuario(int idUsuario) {
        List<Trabajador> trabajadores = new ArrayList<>();
        String query = "SELECT id_trabajador, nombre, apellido, dni, sueldo_bruto, email, telefono FROM trabajador WHERE id_usuario = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trabajador trabajador = new Trabajador(
                        rs.getInt("id_trabajador"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getDouble("sueldo_bruto"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        idUsuario
                );
                trabajadores.add(trabajador);
            }
        } catch (SQLException e) {
            Platform.runLater(() -> showAlert("Error", "Error al obtener los trabajadores: " + e.getMessage(), Alert.AlertType.ERROR));
        }
        return trabajadores;
    }

    // Método para agregar un trabajador
    @FXML
    private void agregarTrabajador() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TrabajadorView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Agregar Trabajador");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Imprime el stack trace para obtener más detalles
            Platform.runLater(() -> showAlert("Error", "No se pudo cargar la vista para agregar trabajador: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    // Método para modificar un trabajador
    @FXML
    private void modificarTrabajador() {
        Trabajador trabajadorSeleccionado = trabajadorTable.getSelectionModel().getSelectedItem();
        if (trabajadorSeleccionado == null) {
            showAlert("Advertencia", "Por favor, seleccione un trabajador para modificar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TrabajadorView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Modificar Trabajador");

            TrabajadorController controller = fxmlLoader.getController();
            controller.cargarDatosTrabajador(trabajadorSeleccionado);
            stage.show();
        } catch (IOException e) {
            Platform.runLater(() -> showAlert("Error", "No se pudo cargar la vista para modificar trabajador: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    // Método para eliminar un trabajador
    @FXML
    private void eliminarTrabajador() {
        Trabajador trabajadorSeleccionado = trabajadorTable.getSelectionModel().getSelectedItem();
        if (trabajadorSeleccionado == null) {
            showAlert("Advertencia", "Por favor, seleccione un trabajador para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Eliminar trabajador");
        alert.setContentText("¿Estás seguro de que deseas eliminar este trabajador?");
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            eliminarTrabajadorDeBaseDeDatos(trabajadorSeleccionado.getIdTrabajador());
            trabajadorTable.getItems().remove(trabajadorSeleccionado);
            showAlert("Éxito", "Trabajador eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void eliminarTrabajadorDeBaseDeDatos(int idTrabajador) {
        String query = "DELETE FROM trabajador WHERE id_trabajador = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTrabajador);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Platform.runLater(() -> showAlert("Error", "No se pudo eliminar el trabajador: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Método para cerrar sesión
    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Login");
            stage.show();

            Stage currentStage = (Stage) cerrarSesionButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            Platform.runLater(() -> showAlert("Error", "No se pudo cargar la vista de Login: " + e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    // Método que se ejecuta al presionar el botón de "Gestionar Alícuotas"
    @FXML
    public void gestionarAlicuotas(javafx.event.ActionEvent actionEvent) {
        System.out.println("Gestionar Alícuotas button clicked");
        openAlicuotasManagementWindow(); // Llama al método para abrir la nueva ventana
    }

    // Método para abrir una nueva ventana para gestionar las alícuotas
    private void openAlicuotasManagementWindow() {
        try {
            // Cargar el archivo FXML de la vista de gestión de alícuotas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlicuotaView.fxml"));
            Parent alicuotasPane = loader.load();
            AlicuotaController alicuotaController = loader.getController();  // Si es necesario, puedes usar este controlador más adelante

            // Configurar la nueva ventana para mostrar la vista de alícuotas
            Stage stage = new Stage();
            stage.setTitle("Gestión de Alícuotas");
            stage.setScene(new Scene(alicuotasPane));

            // Mostrar la ventana
            stage.show();
        }
        catch (IOException e) {
            System.err.println("Error al abrir la ventana de gestión de alícuotas: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana de alícuotas");
            alert.setContentText("Hubo un error al intentar abrir la ventana de gestión de alícuotas.");
            alert.showAndWait();
        }
    }



}


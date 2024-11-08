package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Trabajador;
import service.TrabajadorService;
import service.UsuarioService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AgregarTrabajadorController {
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField dniField;
    @FXML
    private TextField sueldoField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telefonoField;
    @FXML
    private ComboBox<String> usuarioComboBox;
    @FXML
    private Button volverButton;

    @FXML
    public void initialize() {
        try {
            cargarUsuarios();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void cargarUsuarios() throws SQLException {
        List<String> usuarios = usuarioService.obtenerNombresUsuarios();
        usuarioComboBox.getItems().addAll(usuarios);
    }

    @FXML
    private void guardarTrabajador() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String dni = dniField.getText().trim();
        String sueldoBruto = sueldoField.getText().trim();
        String email = emailField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String usuarioSeleccionado = usuarioComboBox.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || sueldoBruto.isEmpty() ||
                email.isEmpty() || telefono.isEmpty() || usuarioSeleccionado == null) {
            mostrarAlerta("Error", "Todos los campos deben ser completados.");
            return;
        }

        if (trabajadorService.existeDni(dni)) {
            mostrarAlerta("Error", "El DNI ya existe en la base de datos.");
            return;
        }

        try {
            int idUsuario = usuarioService.obtenerIdUsuarioPorNombre(usuarioSeleccionado);
            double sueldo = Double.parseDouble(sueldoBruto);
            Trabajador trabajador = new Trabajador(nombre, apellido, dni, sueldo, email, telefono, idUsuario);

            boolean guardado = trabajadorService.guardarTrabajador(trabajador);

            if (guardado) {
                mostrarAlerta("Éxito", "Trabajador agregado correctamente.");
                clearForm();
            } else {
                mostrarAlerta("Error", "Error al guardar el trabajador.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al guardar el trabajador: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El sueldo bruto debe ser un número válido.");
        }
    }

    private void clearForm() {
        nombreField.clear();
        apellidoField.clear();
        dniField.clear();
        sueldoField.clear();
        emailField.clear();
        telefonoField.clear();
        usuarioComboBox.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volverMenu() {
        try {
            FXMLLoader loader;
            String rol = LoginController.rolActual;

            if ("admin".equals(rol)) {
                loader = new FXMLLoader(getClass().getResource("/view/MenuAdminView.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/view/MenuUsuarioView.fxml"));
            }

            Parent root = loader.load();
            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el menú: " + e.getMessage());
        }
    }
}

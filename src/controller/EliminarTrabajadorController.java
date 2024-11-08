package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.TrabajadorService;

import java.io.IOException;

public class EliminarTrabajadorController {
    private final TrabajadorService trabajadorService = new TrabajadorService();

    @FXML
    private TextField dniField;
    @FXML
    private Button volverButton;

    @FXML
    private void eliminarTrabajador() {
        String dni = dniField.getText().trim();

        if (dni.isEmpty()) {
            mostrarAlerta("Error", "Por favor, ingrese un DNI.");
            return;
        }

        boolean eliminado = trabajadorService.eliminarTrabajadorPorDni(dni);
        if (eliminado) {
            mostrarAlerta("Éxito", "El trabajador ha sido eliminado correctamente.");
            dniField.clear();
        } else {
            mostrarAlerta("Error", "No se encontró un trabajador con el DNI ingresado.");
        }
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuUsuarioController {

    @FXML
    private Button agregarTrabajadorButton;
    @FXML
    private Button modificarTrabajadorButton;
    @FXML
    private Button eliminarTrabajadorButton;
    @FXML
    private Button listarTrabajadoresButton;
    @FXML
    private Button cerrarSesionButton;

    @FXML
    private void agregarTrabajador() {
        cambiarVista("/view/AgregarTrabajadorView.fxml");
    }

    @FXML
    private void modificarTrabajador() {
        cambiarVista("/view/ModificarTrabajadorView.fxml");
    }

    @FXML
    private void eliminarTrabajador() {
        cambiarVista("/view/EliminarTrabajadorView.fxml");
    }

    @FXML
    private void listarTrabajadores() {
        cambiarVista("/view/ListarTrabajadorView.fxml");
    }

    @FXML
    private void cerrarSesion() {
        // Obtener la ventana actual (escenario)
        Button source = (Button) cerrarSesionButton;
        Stage currentStage = (Stage) source.getScene().getWindow();

        // Cerrar la ventana actual
        currentStage.close();

        // Cargar y mostrar la ventana de inicio de sesión
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent loginRoot = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Inicio de Sesión");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio de sesión.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cambiarVista(String ruta) {
        try {
            // Cambié la ruta para asegurarnos de que está en el lugar correcto
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load(); // Cargar la vista FXML
            Stage stage = (Stage) agregarTrabajadorButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Establecer la nueva escena
            stage.show(); // Mostrar la nueva vista
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista.");
        }
    }
}
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

public class MenuPrincipalController {

    @FXML
    private Button botonIniciarSesionAdmin;
    @FXML
    private Button botonIniciarSesionUsuario;
    @FXML
    private Button botonSalir;

    @FXML
    private void iniciarSesionAdmin() {
        mostrarAlerta("Inicio de Sesión de Administrador", "Dirigiéndose al menú de administrador.");
        // Agregar código para cargar el menú de administrador
    }

    @FXML
    private void iniciarSesionUsuario() {
        mostrarAlerta("Inicio de Sesión de Usuario", "Dirigiéndose al menú de usuario.");
        // Agregar código para cargar el menú de usuario
    }

    @FXML
    private void salirAplicacion() {
        mostrarAlerta("Salir de la Aplicación", "Cerrando la aplicación.");
        // Agregar código para cerrar la aplicación
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

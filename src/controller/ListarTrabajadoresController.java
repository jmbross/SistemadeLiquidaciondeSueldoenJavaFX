package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Trabajador;
import service.TrabajadorService;
import java.io.IOException;
import java.util.List;

public class ListarTrabajadoresController {

    @FXML
    private ListView<String> listViewTrabajadores;
    @FXML
    private Button volverButton;

    private TrabajadorService trabajadorService = new TrabajadorService();

    @FXML
    private void initialize() {
        listarTrabajadores();
    }

    private void listarTrabajadores() {
        try {
            listViewTrabajadores.getItems().clear();  // Limpiar la lista antes de agregar nuevos elementos

            List<Trabajador> trabajadores = trabajadorService.obtenerTodosLosTrabajadores();

            if (trabajadores.isEmpty()) {
                mostrarAlerta("Información", "No hay trabajadores registrados.");
            }

            for (Trabajador trabajador : trabajadores) {
                String infoTrabajador = "ID: " + trabajador.getId() +
                        ", Nombre: " + trabajador.getNombre() +
                        ", Apellido: " + trabajador.getApellido() +
                        ", DNI: " + trabajador.getDni() +
                        ", Sueldo: " + trabajador.getSueldoBruto();
                listViewTrabajadores.getItems().add(infoTrabajador);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al listar los trabajadores: " + e.getMessage());
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

    private void mostrarAlerta(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Trabajador;
import service.TrabajadorService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificarTrabajadorController {

    @FXML
    private TextField campoDNI;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoApellido;
    @FXML
    private TextField campoSueldoBruto;

    private TrabajadorService trabajadorService;

    public ModificarTrabajadorController() {
        this.trabajadorService = new TrabajadorService();
    }

    @FXML
    private void buscarTrabajador() {
        String dni = campoDNI.getText();

        Trabajador trabajador = trabajadorService.obtenerTrabajadorPorDni(dni);
        if (trabajador != null) {
            campoNombre.setText(trabajador.getNombre());
            campoApellido.setText(trabajador.getApellido());
            campoSueldoBruto.setText(String.valueOf(trabajador.getSueldoBruto()));
        } else {
            mostrarAlerta("No encontrado", "No se encontró un trabajador con ese DNI.");
        }
    }

    @FXML
    private void actualizarTrabajador() {
        String dni = campoDNI.getText();
        String nombre = campoNombre.getText();
        String apellido = campoApellido.getText();
        double sueldoBruto = 0;

        // Validar sueldo bruto
        try {
            sueldoBruto = Double.parseDouble(campoSueldoBruto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El sueldo bruto debe ser un número válido.");
            return;
        }

        try {
            boolean actualizado = trabajadorService.actualizarTrabajador(dni, nombre, apellido, sueldoBruto);

            if (actualizado) {
                mostrarAlerta("Actualización exitosa", "Datos del trabajador actualizados correctamente.");
                limpiarCampos();
            } else {
                mostrarAlerta("No encontrado", "No se encontró un trabajador con ese DNI.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al actualizar el trabajador.");
        }
    }

    private void limpiarCampos() {
        campoDNI.clear();
        campoNombre.clear();
        campoApellido.clear();
        campoSueldoBruto.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public static String rolActual;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private Connection connection;

    public LoginController() {
        this.connection = DatabaseConnection.getConnection();
    }

    @FXML
    private void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            String query = "SELECT * FROM usuario WHERE email = ? AND contrasena = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                rolActual = resultSet.getString("rol");
                showAlert("Inicio de Sesión", "Bienvenido " + rolActual, Alert.AlertType.INFORMATION);

                if ("admin".equals(rolActual)) {
                    // Cargar vista de administrador
                    loadAdminMenu();
                } else if ("usuario".equals(rolActual)) {
                    // Cargar vista de usuario
                    loadUserMenu();
                }
            } else {
                showAlert("Error de autenticación", "Email o contraseña incorrectos", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MenuAdminView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Menú de Administrador");
            stage.show();

            // Cerrar ventana de login actual
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar el menú de administrador", Alert.AlertType.ERROR);
        }
    }

    private void loadUserMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MenuUsuarioView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Menú de Usuario");
            stage.show();

            // Cerrar ventana de login actual
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar el menú de usuario", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

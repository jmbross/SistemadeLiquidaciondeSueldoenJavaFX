package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdminController {

    @FXML
    private Label etiquetaBienvenida;
    @FXML
    private ComboBox<String> usuarioComboBox; // ComboBox para mostrar nombres completos de usuarios
    @FXML
    private Button agregarUsuarioButton;
    @FXML
    private Button modificarUsuarioButton;
    @FXML
    private Button eliminarUsuarioButton;
    @FXML
    private Button listarUsuariosButton;
    @FXML
    private Button cerrarSesionButton;

    private UsuarioService usuarioService;
    private Map<String, Integer> usuarioIdMap; // Mapa para relacionar nombres completos con IDs


    @FXML
    private void initialize() {
        etiquetaBienvenida.setText("Bienvenido, Administrador");

        usuarioService = new UsuarioService();
        usuarioIdMap = new HashMap<>();

        // Llenar el ComboBox con los nombres completos de los usuarios
        llenarUsuarioComboBox();
    }

    private void llenarUsuarioComboBox() {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            usuarioComboBox.getItems().clear(); // Limpiar ComboBox antes de agregar elementos

            for (Usuario usuario : usuarios) {
                String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
                usuarioComboBox.getItems().add(nombreCompleto);
                usuarioIdMap.put(nombreCompleto, usuario.getIdUsuario()); // Asociar nombre completo con ID
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al obtener usuarios: " + e.getMessage());
        }
    }

    private Integer obtenerIdUsuarioSeleccionado() {
        String nombreCompletoSeleccionado = usuarioComboBox.getValue();
        return usuarioIdMap.get(nombreCompletoSeleccionado); // Obtener ID usando el nombre completo seleccionado
    }

    @FXML
    private void agregarUsuario() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Agregar Usuario");

        // Configurar el contenido del diálogo
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();

        Label apellidoLabel = new Label("Apellido:");
        TextField apellidoField = new TextField();

        Label dniLabel = new Label("DNI:");
        TextField dniField = new TextField();

        Label matriculaLabel = new Label("Matrícula:");
        TextField matriculaField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label contrasenaLabel = new Label("Contraseña:");
        PasswordField contrasenaField = new PasswordField();

        Label rolLabel = new Label("Rol:");
        ChoiceBox<String> rolChoiceBox = new ChoiceBox<>();
        rolChoiceBox.getItems().addAll("admin", "usuario");

        // Layout
        GridPane grid = new GridPane();
        grid.add(nombreLabel, 0, 0); grid.add(nombreField, 1, 0);
        grid.add(apellidoLabel, 0, 1); grid.add(apellidoField, 1, 1);
        grid.add(dniLabel, 0, 2); grid.add(dniField, 1, 2);
        grid.add(matriculaLabel, 0, 3); grid.add(matriculaField, 1, 3);
        grid.add(emailLabel, 0, 4); grid.add(emailField, 1, 4);
        grid.add(contrasenaLabel, 0, 5); grid.add(contrasenaField, 1, 5);
        grid.add(rolLabel, 0, 6); grid.add(rolChoiceBox, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Botón confirmar
        ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarButtonType) {
                Usuario usuario = new Usuario();
                usuario.setNombre(nombreField.getText());
                usuario.setApellido(apellidoField.getText());
                usuario.setDni(dniField.getText());
                usuario.setMatricula(matriculaField.getText());
                usuario.setEmail(emailField.getText());
                usuario.setContrasena(contrasenaField.getText());
                usuario.setRol(rolChoiceBox.getValue());
                return usuario;
            }
            return null;
        });

        // Mostrar el diálogo y capturar el resultado
        dialog.showAndWait().ifPresent(usuario -> {
            try {
                UsuarioService usuarioService = new UsuarioService();
                usuarioService.addUsuario(usuario);
                mostrarAlerta("Éxito", "Usuario agregado correctamente.");
            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al agregar el usuario: " + e.getMessage());
            }
        });
    }

    @FXML
    private void modificarUsuario() {
        try {
            UsuarioService usuarioService = new UsuarioService();
            List<Usuario> usuarios = usuarioService.getAllUsuarios();

            if (usuarios.isEmpty()) {
                mostrarAlerta("Error", "No hay usuarios disponibles para modificar.");
                return;
            }

            ChoiceDialog<Usuario> dialog = new ChoiceDialog<>(usuarios.get(0), usuarios);
            dialog.setTitle("Modificar Usuario");
            dialog.setHeaderText("Seleccione un usuario para modificar");

            dialog.showAndWait().ifPresent(usuario -> {
                // Crear un diálogo de modificación con los datos actuales del usuario seleccionado
                Dialog<Usuario> modificarDialog = new Dialog<>();
                modificarDialog.setTitle("Modificar Usuario");

                // Labels y campos de texto
                Label nombreLabel = new Label("Nombre:");
                TextField nombreField = new TextField(usuario.getNombre());

                Label apellidoLabel = new Label("Apellido:");
                TextField apellidoField = new TextField(usuario.getApellido());

                Label dniLabel = new Label("DNI:");
                TextField dniField = new TextField(usuario.getDni());

                Label matriculaLabel = new Label("Matrícula:");
                TextField matriculaField = new TextField(usuario.getMatricula());

                Label emailLabel = new Label("Email:");
                TextField emailField = new TextField(usuario.getEmail());

                Label contrasenaLabel = new Label("Contraseña:");
                PasswordField contrasenaField = new PasswordField();

                Label rolLabel = new Label("Rol:");
                ChoiceBox<String> rolChoiceBox = new ChoiceBox<>();
                rolChoiceBox.getItems().addAll("admin", "usuario");
                rolChoiceBox.setValue(usuario.getRol());

                // Configuración de diseño del GridPane
                GridPane grid = new GridPane();
                grid.add(nombreLabel, 0, 0); grid.add(nombreField, 1, 0);
                grid.add(apellidoLabel, 0, 1); grid.add(apellidoField, 1, 1);
                grid.add(dniLabel, 0, 2); grid.add(dniField, 1, 2);
                grid.add(matriculaLabel, 0, 3); grid.add(matriculaField, 1, 3);
                grid.add(emailLabel, 0, 4); grid.add(emailField, 1, 4);
                grid.add(contrasenaLabel, 0, 5); grid.add(contrasenaField, 1, 5);
                grid.add(rolLabel, 0, 6); grid.add(rolChoiceBox, 1, 6);

                modificarDialog.getDialogPane().setContent(grid);

                // Botones de diálogo
                ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
                modificarDialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

                modificarDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == confirmarButtonType) {
                        usuario.setNombre(nombreField.getText());
                        usuario.setApellido(apellidoField.getText());
                        usuario.setDni(dniField.getText());
                        usuario.setMatricula(matriculaField.getText());
                        usuario.setEmail(emailField.getText());
                        usuario.setContrasena(contrasenaField.getText()); // Asegúrate de aplicar hashing si es necesario
                        usuario.setRol(rolChoiceBox.getValue());
                        return usuario;
                    }
                    return null;
                });

                modificarDialog.showAndWait().ifPresent(modifiedUsuario -> {
                    try {
                        usuarioService.updateUsuario(modifiedUsuario);
                        mostrarAlerta("Éxito", "Usuario modificado correctamente.");
                    } catch (SQLException e) {
                        mostrarAlerta("Error", "Error al modificar el usuario: " + e.getMessage());
                    }
                });
            });

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al obtener usuarios: " + e.getMessage());
        }
    }


    @FXML
    private void eliminarUsuario() {
        Integer idUsuario = obtenerIdUsuarioSeleccionado();
        if (idUsuario == null) {
            mostrarAlerta("Error", "Seleccione un usuario para eliminar.");
            return;
        }

        try {
            usuarioService.deleteUsuario(idUsuario);
            mostrarAlerta("Éxito", "Usuario eliminado correctamente.");
            llenarUsuarioComboBox();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar el usuario: " + e.getMessage());
        }
    }

    @FXML
    private void listarUsuarios() {
        try {
            UsuarioService usuarioService = new UsuarioService();
            List<Usuario> usuarios = usuarioService.getAllUsuarios();

            if (usuarios.isEmpty()) {
                mostrarAlerta("Información", "No hay usuarios disponibles.");
                return;
            }

            StringBuilder listaUsuarios = new StringBuilder("Lista de Usuarios:\n");
            for (Usuario usuario : usuarios) {
                listaUsuarios.append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lista de Usuarios");
            alert.setHeaderText(null);
            alert.setContentText(listaUsuarios.toString());
            alert.showAndWait();

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al obtener los usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarSesion() {
        // Obtener la ventana actual (escenario)
        Button source = (Button) cerrarSesionButton; // `cerrarSesionButton` debe estar definido en ambas pantallas
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
}

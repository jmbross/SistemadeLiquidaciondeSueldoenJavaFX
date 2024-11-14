package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Alicuota;
import model.Trabajador;
import service.AlicuotaService;
import service.TrabajadorService;

import java.sql.SQLException;

public class AlicuotaController {

    // Campos de la interfaz
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPorcentaje;
    @FXML
    private ComboBox<Trabajador> comboTrabajadores;
    @FXML
    private TableView<Alicuota> tableAlicuotas;
    @FXML
    private TableColumn<Alicuota, String> colDescripcion;
    @FXML
    private TableColumn<Alicuota, Double> colPorcentaje;
    @FXML
    private TableColumn<Alicuota, String> colTrabajador;

    // Lista para almacenar las alícuotas
    private ObservableList<Alicuota> listaAlicuotas = FXCollections.observableArrayList();

    // Servicio para manejar alícuotas
    private AlicuotaService alicuotaService = new AlicuotaService();
    // Servicio para manejar trabajadores
    private TrabajadorService trabajadorService = new TrabajadorService();

    // Método para inicializar el controlador
    @FXML
    public void initialize() {
        // Cargar la lista de trabajadores
        ObservableList<Trabajador> trabajadores = trabajadorService.listarTrabajadores();
        comboTrabajadores.setItems(trabajadores);

        // Configurar las columnas de la tabla de alícuotas
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        colPorcentaje.setCellValueFactory(cellData -> cellData.getValue().porcentajeProperty().asObject());
        colTrabajador.setCellValueFactory(cellData -> cellData.getValue().idTrabajadorProperty().asString());

        // Cargar las alícuotas desde la base de datos
        listaAlicuotas = alicuotaService.listarAlicuotas();
        tableAlicuotas.setItems(listaAlicuotas);
    }

    // Método para agregar una nueva alícuota
    @FXML
    public void agregarAlicuota() throws SQLException {
        // Validar que todos los campos estén llenos
        if (txtDescripcion.getText().isEmpty() || txtPorcentaje.getText().isEmpty() || comboTrabajadores.getSelectionModel().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben estar completos.");
            return;
        }

        // Obtener los valores de los campos
        String descripcion = txtDescripcion.getText();
        double porcentaje;
        try {
            porcentaje = Double.parseDouble(txtPorcentaje.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El porcentaje debe ser un número válido.");
            return;
        }
        Trabajador trabajadorSeleccionado = comboTrabajadores.getSelectionModel().getSelectedItem();
        int idTrabajador = trabajadorSeleccionado.getIdTrabajador();

        // Crear la nueva alícuota usando el constructor adecuado
        Alicuota nuevaAlicuota = new Alicuota(descripcion, porcentaje, idTrabajador);

        // Guardar la alícuota en la base de datos
        try {
            alicuotaService.addAlicuota(nuevaAlicuota);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Actualizar la lista de alícuotas
        listaAlicuotas.add(nuevaAlicuota);
        limpiarCampos();
    }

    // Método para modificar una alícuota seleccionada
    @FXML
    public void modificarAlicuota() throws SQLException {
        // Obtener la alícuota seleccionada de la tabla
        Alicuota alicuotaSeleccionada = tableAlicuotas.getSelectionModel().getSelectedItem();
        if (alicuotaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una alícuota para modificar.");
            return;
        }

        // Validar que todos los campos estén llenos
        if (txtDescripcion.getText().isEmpty() || txtPorcentaje.getText().isEmpty() || comboTrabajadores.getSelectionModel().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben estar completos.");
            return;
        }

        // Obtener los valores de los campos
        String descripcion = txtDescripcion.getText();
        double porcentaje;
        try {
            porcentaje = Double.parseDouble(txtPorcentaje.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El porcentaje debe ser un número válido.");
            return;
        }
        Trabajador trabajadorSeleccionado = comboTrabajadores.getSelectionModel().getSelectedItem();
        int idTrabajador = trabajadorSeleccionado.getIdTrabajador();

        // Modificar la alícuota
        alicuotaSeleccionada.setDescripcion(descripcion);
        alicuotaSeleccionada.setPorcentaje(porcentaje);
        alicuotaSeleccionada.setIdTrabajador(idTrabajador);

        // Actualizar la alícuota en la base de datos
        alicuotaService.updateAlicuota(alicuotaSeleccionada);

        // Actualizar la lista de alícuotas en la tabla
        tableAlicuotas.refresh();
        limpiarCampos();
    }

    // Método para eliminar una alícuota seleccionada
    @FXML
    public void eliminarAlicuota() throws SQLException {
        // Obtener la alícuota seleccionada de la tabla
        Alicuota alicuotaSeleccionada = tableAlicuotas.getSelectionModel().getSelectedItem();
        if (alicuotaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una alícuota para eliminar.");
            return;
        }

        // Confirmar la eliminación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de que desea eliminar esta alícuota?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Eliminar la alícuota de la base de datos
                alicuotaService.deleteAlicuota(alicuotaSeleccionada.getIdAlicuota());
                listaAlicuotas.remove(alicuotaSeleccionada);
            }
        });
    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        txtDescripcion.clear();
        txtPorcentaje.clear();
        comboTrabajadores.getSelectionModel().clearSelection();
    }

    // Método para mostrar una alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para listar las alícuotas
    @FXML
    public void listarAlicuotas() {
        listaAlicuotas.clear();
        listaAlicuotas.addAll(alicuotaService.listarAlicuotas());
    }

    // Método para volver al menú principal
    @FXML
    public void volverMenu() {
        // Aquí puedes agregar el código para regresar a la pantalla de inicio o menú principal
        System.out.println("Volviendo al menú principal...");
    }
}

package controller;

import model.Trabajador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Recibo;
import model.Trabajador;
import model.Usuario;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import service.ReciboService;
import service.TrabajadorService;
import service.UsuarioService;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;



// Controlador para gestionar las operaciones CRUD de Trabajador en JavaFX
public class TrabajadorController {

    private TrabajadorService trabajadorService;
    private final UsuarioService usuarioService = new UsuarioService();
    private ReciboService reciboService;
    private ObservableList<Trabajador> listaTrabajadores;

    @FXML
    private TableView<Trabajador> tablaTrabajadores;
    @FXML
    private TableColumn<Trabajador, String> colNombre;
    @FXML
    private TableColumn<Trabajador, String> colApellido;
    @FXML
    private TableColumn<Trabajador, String> colDni;
    @FXML
    private TableColumn<Trabajador, Double> colSueldoBruto;
    @FXML
    private TableColumn<Trabajador, String> colEmail;
    @FXML
    private TableColumn<Trabajador, String> colTelefono;
    @FXML
    private TableColumn<Trabajador, String> colUsuario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDNI;
    @FXML
    private TextField txtSueldoBruto;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<Usuario> cmbUsuario;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnListar;
    @FXML
    private Button btnVolver;
    @FXML
    private boolean modoEdicion;
    private Trabajador trabajador;
    public TrabajadorController() {
        this.trabajadorService = new TrabajadorService();
        this.reciboService = new ReciboService(this.trabajadorService);
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }
    public void initialize() {
        cargarUsuarios();
        configurarColumnasTabla();
        cargarListaTrabajadores();
    }

    private void configurarColumnasTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colSueldoBruto.setCellValueFactory(new PropertyValueFactory<>("sueldoBruto"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
    }

    private void cargarListaTrabajadores() {
        listaTrabajadores = FXCollections.observableArrayList(trabajadorService.obtenerTodosLosTrabajadores());
        tablaTrabajadores.setItems(listaTrabajadores);
    }

    private Trabajador obtenerTrabajadorSeleccionado() {
        return tablaTrabajadores.getSelectionModel().getSelectedItem();
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            cmbUsuario.setItems(FXCollections.observableArrayList(usuarios));
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar usuarios", e.getMessage());
        }
    }

    @FXML
    private void agregarTrabajador(ActionEvent event) {
        Trabajador trabajador = obtenerTrabajadorDesdeFormulario();
        if (trabajador != null) {
            trabajadorService.agregarTrabajador(trabajador);
            mostrarAlerta("Trabajador agregado", "El trabajador ha sido agregado correctamente.");
            limpiarFormulario();
            cargarListaTrabajadores();
        }
    }

    @FXML
    private void modificarTrabajador(ActionEvent event) {
        Trabajador trabajadorSeleccionado = obtenerTrabajadorSeleccionado();
        if (trabajadorSeleccionado != null) {
            Trabajador trabajadorModificado = obtenerTrabajadorDesdeFormulario();
            if (trabajadorModificado != null) {
                trabajadorModificado.setIdTrabajador(trabajadorSeleccionado.getIdTrabajador());
                trabajadorService.modificarTrabajador(trabajadorModificado);
                mostrarAlerta("Trabajador modificado", "El trabajador ha sido modificado correctamente.");
                limpiarFormulario();
                cargarListaTrabajadores();
            }
        } else {
            mostrarAlerta("Error", "Seleccione un trabajador para modificar.");
        }
    }

    @FXML
    private void eliminarTrabajador(ActionEvent event) {
        Trabajador trabajadorSeleccionado = obtenerTrabajadorSeleccionado();
        if (trabajadorSeleccionado != null) {
            trabajadorService.eliminarTrabajador(trabajadorSeleccionado.getIdTrabajador());
            mostrarAlerta("Trabajador eliminado", "El trabajador ha sido eliminado correctamente.");
            limpiarFormulario();
            cargarListaTrabajadores();
        } else {
            mostrarAlerta("Error", "Seleccione un trabajador para eliminar.");
        }
    }

    @FXML
    private void listarTrabajadores(ActionEvent event) {
        Usuario usuarioSeleccionado = cmbUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            listaTrabajadores = FXCollections.observableArrayList(
                    trabajadorService.listarTrabajadoresPorUsuario(usuarioSeleccionado.getIdUsuario()));
            tablaTrabajadores.setItems(listaTrabajadores);
        } else {
            mostrarAlerta("Error", "Seleccione un usuario para listar trabajadores.");
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtDNI.clear();
        txtSueldoBruto.clear();
        txtEmail.clear();
        txtTelefono.clear();
        cmbUsuario.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Trabajador obtenerTrabajadorDesdeFormulario() {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String dni = txtDNI.getText();
            double sueldoBruto = Double.parseDouble(txtSueldoBruto.getText());
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();
            Usuario usuario = cmbUsuario.getSelectionModel().getSelectedItem();

            if (usuario == null) throw new IllegalArgumentException("Usuario no seleccionado.");
            return new Trabajador(0, nombre, apellido, dni, sueldoBruto, email, telefono, usuario.getIdUsuario());
        } catch (Exception e) {
            mostrarAlerta("Error en formulario", e.getMessage());
            return null;
        }
    }

//    @FXML
//    private void generarReciboSueldo() {
//        Trabajador trabajadorSeleccionado = obtenerTrabajadorSeleccionado();
//        if (trabajadorSeleccionado != null) {
//            Recibo recibo = reciboService.generarRecibo(trabajadorSeleccionado);
//            mostrarReciboEnPDF(recibo);
//        }
//    }

//    private void mostrarReciboEnPDF(Recibo recibo) {
//        try (PDDocument document = new PDDocument()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD, 16);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 750);
//                contentStream.showText("Recibo de Sueldo");
//                contentStream.endText();
//
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD, 12);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 700);
//                contentStream.showText("ID Trabajador: " + recibo.getIdTrabajador());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Fecha: " + recibo.getFecha());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Sueldo Neto: " + recibo.getSueldoNeto());
//                contentStream.endText();
//            }
//            document.save("recibo_" + recibo.getIdTrabajador() + ".pdf");
//        } catch (IOException e) {
//            mostrarAlerta("Error al generar PDF", e.getMessage());
//        }
//    }

    @FXML
    private void volverMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuUsuarioView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar la vista del menú principal.");
        }
    }
}

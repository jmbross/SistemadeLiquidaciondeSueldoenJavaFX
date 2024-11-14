package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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

    private TrabajadorService trabajadorService = new TrabajadorService();
    private final UsuarioService usuarioService = new UsuarioService();
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

    private ReciboService reciboService;

    public TrabajadorController() {
        // Inicializa trabajadorService si no está ya inicializado
        this.trabajadorService = new TrabajadorService();

        // Ahora puedes inicializar ReciboService con trabajadorService
        this.reciboService = new ReciboService(this.trabajadorService);
    }

    // Método para inicializar el controlador, cargando los usuarios en el ComboBox y la lista de trabajadores
    public void initialize() {
        cargarUsuarios();

        // Configurar las columnas para que muestren los atributos de Trabajador
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colSueldoBruto.setCellValueFactory(new PropertyValueFactory<>("sueldoBruto"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        // Cargar los datos en la tabla (usa un método para obtener la lista de trabajadores)
        tablaTrabajadores.setItems(listaTrabajadores);
        cargarListaTrabajadores();
    }


    // Método para obtener la lista de trabajadores desde el servicio
    private void cargarListaTrabajadores() {
        // Llamada al nuevo método en el servicio
        listaTrabajadores = FXCollections.observableArrayList(trabajadorService.obtenerTodosLosTrabajadores());
        tablaTrabajadores.setItems(listaTrabajadores);
    }

        // Método para obtener un trabajador desde la selección de la tabla
        private Trabajador obtenerTrabajadorSeleccionado() {
            return tablaTrabajadores.getSelectionModel().getSelectedItem();
        }

        /// Carga la lista de usuarios en el ComboBox para asignar el trabajador a un usuario específico
        private void cargarUsuarios() {
            try {
                List<Usuario> usuarios = usuarioService.getAllUsuarios();  // Cambié obtenerUsuarios() por getAllUsuarios()
                cmbUsuario.setItems(FXCollections.observableArrayList(usuarios));
            } catch (SQLException e) {
                mostrarAlerta("Error al cargar usuarios", e.getMessage());
            }
        }

        // Método para agregar un trabajador
        @FXML
        private void agregarTrabajador(ActionEvent event) {
            Trabajador trabajador = obtenerTrabajadorDesdeFormulario();
            trabajadorService.agregarTrabajador(trabajador);
            mostrarAlerta("Trabajador agregado", "El trabajador ha sido agregado correctamente.");
            limpiarFormulario();
            cargarListaTrabajadores();
        }

        // Método para modificar un trabajador seleccionado
        @FXML
        private void modificarTrabajador(ActionEvent event) {
            Trabajador trabajadorSeleccionado = obtenerTrabajadorSeleccionado();
            if (trabajadorSeleccionado != null) {
                Trabajador trabajadorModificado = obtenerTrabajadorDesdeFormulario();
                trabajadorModificado.setIdTrabajador(trabajadorSeleccionado.getIdTrabajador());
                trabajadorService.modificarTrabajador(trabajadorModificado);
                mostrarAlerta("Trabajador modificado", "El trabajador ha sido modificado correctamente.");
                limpiarFormulario();
                cargarListaTrabajadores();
            } else {
                mostrarAlerta("Error", "Seleccione un trabajador para modificar.");
            }
        }

        // Método para eliminar un trabajador seleccionado
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

        // Método para listar trabajadores en función del usuario seleccionado en el ComboBox
        @FXML
        private void listarTrabajadores(ActionEvent event) {
            Usuario usuarioSeleccionado = cmbUsuario.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                listaTrabajadores = FXCollections.observableArrayList(
                        trabajadorService.listarTrabajadoresPorUsuario(usuarioSeleccionado.getIdUsuario()));  // Cambiado aquí
                tablaTrabajadores.setItems(listaTrabajadores);
            } else {
                mostrarAlerta("Error", "Seleccione un usuario para listar trabajadores.");
            }
        }

        // Método para limpiar los campos del formulario
        private void limpiarFormulario() {
            txtNombre.clear();
            txtApellido.clear();
            txtDNI.clear();
            txtSueldoBruto.clear();
            txtEmail.clear();
            txtTelefono.clear();
            cmbUsuario.getSelectionModel().clearSelection();
        }

        // Muestra una alerta para indicar el resultado de una operación
        private void mostrarAlerta(String titulo, String mensaje) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }

        // Obtiene un trabajador desde los datos del formulario
        private Trabajador obtenerTrabajadorDesdeFormulario() {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String dni = txtDNI.getText();

            // Verificación del DNI
            if (dni == null || dni.trim().isEmpty()) {
                System.out.println("Error: El DNI está vacío.");
                return null;
            }

            // Obtener el texto del campo sueldo bruto
            String sueldoBrutoStr = txtSueldoBruto.getText();
            double sueldoBruto = 0.0;

            // Verificar si el campo sueldo bruto no está vacío y es un número válido
            if (sueldoBrutoStr != null && !sueldoBrutoStr.trim().isEmpty()) {
                try {
                    sueldoBruto = Double.parseDouble(sueldoBrutoStr);
                } catch (NumberFormatException e) {
                    // Manejo de la excepción si la cadena no es un número válido
                    System.out.println("Error: El sueldo bruto ingresado no es válido.");
                    return null;
                }
            } else {
                System.out.println("Error: El campo de sueldo bruto está vacío.");
                return null;
            }

            String email = txtEmail.getText();
            if (email == null || email.trim().isEmpty()) {
                System.out.println("Error: El correo electrónico está vacío.");
                return null;
            }

            String telefono = txtTelefono.getText();
            if (telefono == null || telefono.trim().isEmpty()) {
                System.out.println("Error: El campo teléfono está vacío.");
                return null;
            }

            // Obtener el usuario seleccionado del ComboBox
            Usuario usuario = cmbUsuario.getSelectionModel().getSelectedItem();
            if (usuario == null) {
                System.out.println("Error: No se ha seleccionado un usuario.");
                return null;
            }

            // Crear y devolver el objeto Trabajador con los datos obtenidos
            return new Trabajador(0, nombre, apellido, dni, sueldoBruto, email, telefono, usuario.getIdUsuario());
        }



    public void volverMenu() {
            try {
                // Cargar la vista del menú principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuUsuarioView.fxml"));
                Parent root = loader.load();

                // Obtener la escena actual y cambiar a la nueva escena
                Stage stage = (Stage) btnVolver.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Menú Principal");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar la vista del menú principal.");
            }
        }

    public void cargarDatosTrabajador(Trabajador trabajador) {
        // Cargar los datos del trabajador en los campos del formulario
        txtNombre.setText(trabajador.getNombre());
        txtApellido.setText(trabajador.getApellido());
        txtDNI.setText(trabajador.getDni());
        txtSueldoBruto.setText(String.valueOf(trabajador.getSueldoBruto()));
        txtEmail.setText(trabajador.getEmail());
        txtTelefono.setText(trabajador.getTelefono());

        // Verificar si el trabajador tiene un id_usuario válido
        if (trabajador.getIdUsuario() > 0) {
            try {
                // Obtener el usuario asociado al trabajador
                Usuario usuario = usuarioService.obtenerUsuarioPorId(trabajador.getIdUsuario());

                // Si el usuario existe, seleccionarlo en el ComboBox
                if (usuario != null) {
                    cmbUsuario.getSelectionModel().select(usuario);
                } else {
                    // Si no se encuentra el usuario, mostrar mensaje
                    mostrarAlerta("Error", "No se encontró el usuario asociado al trabajador.");
                }
            } catch (SQLException e) {
                // Manejar errores al obtener el usuario
                mostrarAlerta("Error", "Error al obtener el usuario: " + e.getMessage());
            }
        } else {
            // Si el id_usuario no es válido, mostrar mensaje de error
            mostrarAlerta("Error", "El trabajador no tiene un usuario asociado.");
        }
    }

    @FXML
    private void generarReciboSueldo() {
        Trabajador trabajadorSeleccionado = obtenerTrabajadorSeleccionado();
        if (trabajadorSeleccionado != null) {
            // Generar el recibo sin necesidad de guardarlo en base de datos
            Recibo recibo = reciboService.generarRecibo(trabajadorSeleccionado);

            // Generar el PDF con el recibo
            mostrarReciboEnPDF(recibo);
        }
    }
    // Este método genera un archivo PDF con el recibo de sueldo
    private void mostrarReciboEnPDF(Recibo recibo) {
        PDDocument document = new PDDocument();   // Crear un documento PDF
        PDPage page = new PDPage();                // Crear una nueva página en el documento
        document.addPage(page);                    // Agregar la página al documento

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Establecer el tamaño de la fuente y la posición del texto
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750);  // Posición de la primera línea de texto
            contentStream.showText("Recibo de Sueldo");
            contentStream.endText();

            // Establecer el contenido del recibo con la información del objeto Recibo
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700); // Posición del siguiente texto
            contentStream.showText("ID Trabajador: " + recibo.getIdTrabajador());
            contentStream.newLineAtOffset(0, -20);    // Moverse a la siguiente línea
            contentStream.showText("Fecha: " + recibo.getFecha());
            contentStream.newLineAtOffset(0, -20);    // Moverse a la siguiente línea
            contentStream.showText("Sueldo Neto: " + recibo.getSueldoNeto());
            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();  // Captura cualquier excepción relacionada con la creación del PDF
        }

        // Guardar el documento PDF en una ubicación determinada
        try {
            document.save("recibo_" + recibo.getIdTrabajador() + ".pdf");  // Guardar el archivo PDF
            document.close();  // Cerrar el documento
        } catch (IOException e) {
            e.printStackTrace();  // Captura cualquier excepción durante el guardado del PDF
        }
    }

    public class ReciboSueldoPDF {

        public void generarRecibo(Recibo recibo) {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Establecer el tamaño de la fuente
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Recibo de Sueldo");
                contentStream.endText();

                // Establecer el contenido del recibo
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("ID Trabajador: " + recibo.getIdTrabajador());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Fecha: " + recibo.getFecha());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Sueldo Neto: " + recibo.getSueldoNeto());
                contentStream.endText();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Guardar el documento PDF
            try {
                document.save("recibo_" + recibo.getIdTrabajador() + ".pdf");
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

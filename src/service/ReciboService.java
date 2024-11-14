package service;

import database.DatabaseConnection;
import model.Recibo;
import model.Trabajador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class ReciboService {

    private TrabajadorService trabajadorService;

    public ReciboService(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    public void addRecibo(Recibo recibo) throws SQLException {
        String query = "INSERT INTO recibo (id_trabajador, fecha, sueldo_neto) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, recibo.getTrabajador().getIdTrabajador());
            statement.setDate(2, new java.sql.Date(recibo.getFecha().getTime()));
            statement.setDouble(3, recibo.getSueldoNeto());
            statement.executeUpdate();
        }
    }

    public Recibo generarRecibo(Trabajador trabajador) {
        double sueldoNeto = trabajadorService.calcularSueldoNeto(trabajador);
        return new Recibo(0, trabajador, new Date(), sueldoNeto);
    }

    public class ReciboPDFService {

        // Método para generar el PDF
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

        // Método para guardar el recibo
        public void guardarRecibo(Recibo recibo) {
            // En este ejemplo, solo estamos generando el archivo PDF,
            // pero puedes guardar la información del recibo en una base de datos, por ejemplo.
            System.out.println("Recibo de sueldo guardado: " + recibo.getIdTrabajador());
            generarRecibo(recibo);  // Llamamos a generarRecibo para crear el archivo PDF
        }
    }

}


package mx.dgtic.unam.simin.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGeneratorUtil {

    public ByteArrayInputStream generarReporteSimulacion(SimulacionDto simulacion) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.DARK_GRAY);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.BLACK);
            Font fontPequena = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);

            Paragraph titulo = new Paragraph("Reporte de Simulación Financiera", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Sistema SIMIN - UNAM", fontPequena);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(15);
            document.add(subtitulo);

            PdfPTable tablaGeneral = new PdfPTable(2);
            tablaGeneral.setWidthPercentage(100);
            tablaGeneral.setSpacingBefore(10);
            tablaGeneral.setSpacingAfter(20);
            tablaGeneral.setWidths(new int[]{1, 2});

            tablaGeneral.addCell(celdaLabel("ID Simulación:"));
            tablaGeneral.addCell(celdaValor(String.valueOf(simulacion.getIdSimulacion())));

            tablaGeneral.addCell(celdaLabel("Nombre completo:"));
            tablaGeneral.addCell(celdaValor(simulacion.getNombreUsuario()));

            tablaGeneral.addCell(celdaLabel("Correo electrónico:"));
            tablaGeneral.addCell(celdaValor(simulacion.getCorreoUsuario()));

            tablaGeneral.addCell(celdaLabel("Origen:"));
            tablaGeneral.addCell(celdaValor(simulacion.getIdCartera() != null ?
                    "Cartera - " + simulacion.getNombreCartera() :
                    "Instrumento - " + simulacion.getNombreInstrumento()));

            tablaGeneral.addCell(celdaLabel("Fecha Simulación:"));
            tablaGeneral.addCell(celdaValor(simulacion.getFechaSimulacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

            tablaGeneral.addCell(celdaLabel("Fecha Finalización:"));
            tablaGeneral.addCell(celdaValor(simulacion.getFechaFinalizacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

            tablaGeneral.addCell(celdaLabel("Plazo:"));
            tablaGeneral.addCell(celdaValor(simulacion.getPlazoTexto()));

            tablaGeneral.addCell(celdaLabel("Monto Invertido:"));
            tablaGeneral.addCell(celdaValor(formatoMoneda(simulacion.getMontoInvertido())));

            tablaGeneral.addCell(celdaLabel("Interés Bruto:"));
            tablaGeneral.addCell(celdaValor(formatoMoneda(simulacion.getInteresBruto())));

            tablaGeneral.addCell(celdaLabel("ISR:"));
            tablaGeneral.addCell(celdaValor(formatoMoneda(simulacion.getIsr())));

            tablaGeneral.addCell(celdaLabel("Valor Final:"));
            tablaGeneral.addCell(celdaValor(formatoMoneda(simulacion.getValorFinal())));

            document.add(tablaGeneral);

            List<DetalleSimulacionDto> detalles = simulacion.getDetalles();
            if (detalles != null && !detalles.isEmpty()) {
                Paragraph seccionDetalles = new Paragraph("Detalle por Instrumento", fontSubtitulo);
                seccionDetalles.setSpacingAfter(10);
                document.add(seccionDetalles);

                PdfPTable tablaDetalles = new PdfPTable(8);
                tablaDetalles.setWidthPercentage(100);
                tablaDetalles.setWidths(new int[]{2, 2, 2, 1, 2, 2, 2, 2});

                String[] headers = {"Instrumento", "Tipo", "Plazo", "Títulos", "Tasa", "Inversión", "Interés", "Valor Final"};
                for (String h : headers) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(h, fontSubtitulo));
                    headerCell.setBackgroundColor(Color.LIGHT_GRAY);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setPadding(5);
                    tablaDetalles.addCell(headerCell);
                }

                for (DetalleSimulacionDto d : detalles) {
                    tablaDetalles.addCell(celdaValor(d.getNombreInstrumento()));
                    tablaDetalles.addCell(celdaValor(d.getTipoInstrumento()));
                    tablaDetalles.addCell(celdaValor(d.getPlazoTexto()));
                    tablaDetalles.addCell(celdaValor(String.valueOf(d.getTitulos())));
                    tablaDetalles.addCell(celdaValor(formatoPorcentaje(d.getTasaBruta())));
                    tablaDetalles.addCell(celdaValor(formatoMoneda(d.getInversion())));
                    tablaDetalles.addCell(celdaValor(formatoMoneda(d.getInteres())));
                    tablaDetalles.addCell(celdaValor(formatoMoneda(d.getValorFinal())));
                }

                document.add(tablaDetalles);
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell celdaLabel(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell celdaValor(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setPadding(6);
        return cell;
    }

    private String formatoMoneda(BigDecimal valor) {
        return String.format("$%,.2f MXN", valor);
    }

    private String formatoPorcentaje(BigDecimal valor) {
        return String.format("%.2f %%", valor);
    }
}
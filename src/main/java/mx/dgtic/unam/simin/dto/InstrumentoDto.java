package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.exception.ValidarCamposObligatorios;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ValidarCamposObligatorios
public class InstrumentoDto {

    private Integer idInstrumento;

    @NotBlank(message = "El nombre del instrumento es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    private String nombre;

    @NotNull(message = "El tipo de instrumento es obligatorio.")
    private Integer idTipoInstrumento;

    @Min(value = 0, message = "El plazo en meses no puede ser negativo.")
    @Max(value = 12, message = "El plazo en meses no puede exceder 12.")
    private Integer plazoMeses;

    @Min(value = 0, message = "El plazo en años no puede ser negativo.")
    private Integer plazoAnos;

    @DecimalMin(value = "0.01", inclusive = true, message = "La tasa de interés debe ser mayor a 0.")
    private BigDecimal tasaInteres;

    @DecimalMin(value = "0.01", inclusive = true, message = "El valor nominal debe ser mayor a 0.")
    private BigDecimal valorNominal;

    @NotNull(message = "La frecuencia de pagos es obligatoria.")
    private String frecuenciaPagos;

    @NotNull(message = "El tipo de rendimiento es obligatorio.")
    private Instrumento.TipoRendimiento tipoRendimiento;

    @DecimalMin(value = "0.0001", message = "El precio de mercado debe ser mayor a 0.")
    private BigDecimal precioMercado;

    @Size(max = 250, message = "La descripción adicional no debe exceder los 250 caracteres.")
    private String descripcionAdicional;

    private String tipoInstrumentoDescripcion;

    private LocalDateTime fechaEmision;
    private LocalDateTime fechaVencimiento;
    private BigDecimal tasaCupon;


}

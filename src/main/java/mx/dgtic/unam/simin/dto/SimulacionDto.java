package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimulacionDto {

    private Integer idSimulacion;

    @NotNull(message = "El usuario es obligatorio.")
    private Integer idUsuario;
    private String nombreUsuario;
    private String correoUsuario;

    @NotNull(message = "El instrumento es obligatorio.")
    private Integer idInstrumento;
    private String nombreInstrumento;
    private String tipoInstrumentoDescripcion;

    @DecimalMin(value = "0.01", message = "El monto invertido debe ser mayor a 0.")
    @NotNull(message = "El monto invertido es obligatorio.")
    private BigDecimal montoInvertido;

    private BigDecimal rendimiento;

    @Future(message = "La fecha de finalización debe ser futura.")
    @NotNull(message = "La fecha de finalización es obligatoria.")
    private LocalDateTime fechaFinalizacion;

    private LocalDateTime fechaSimulacion;

    private BigDecimal precioMercado;
    private BigDecimal tasaInteres;
    private BigDecimal valorNominal;

    private Integer idCartera;
    private String nombreCartera;

    private BigDecimal interesBruto;
    private BigDecimal isr;
    private BigDecimal valorFinal;
    private String plazoTexto;
    private BigDecimal interesTotal;
    private BigDecimal isrTotal;
    private BigDecimal valorFinalTotal;
    private List<DetalleSimulacionDto> detalles;
    private BigDecimal remanente;
    private Boolean compartidaAnalista;
    private boolean esPropietario;

    @Min(value = 1, message = "El plazo en días debe ser al menos 1.")
    private Integer plazoDiasManual;

}
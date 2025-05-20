package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstrumentoCarteraDto {

    private Integer idInstrumentosCartera;

    private Integer idCartera;
    private String nombreCartera;

    private Integer idInstrumento;
    private String nombreInstrumento;
    private String tipoInstrumentoDescripcion;

    private Integer titulos;
    private BigDecimal tasaInteres;
    private BigDecimal precioMercado;

    private BigDecimal montoInvertido;
    private Integer plazoDiasBonddia;
    private String plazoTexto;
}
package mx.dgtic.unam.simin.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetalleSimulacionDto {

    private Integer idDetalle;

    private Integer idSimulacion;

    private String nombreInstrumento;

    private String tipoInstrumento;

    private BigDecimal tasaBruta;

    private Integer titulos;

    private BigDecimal inversion;

    private BigDecimal interes;

    private BigDecimal isrCalculado;

    private BigDecimal remanente;

    private Boolean esPrincipal;

    private BigDecimal inversionBonddia;
    private BigDecimal interesBonddia;
    private Integer titulosBonddia;
    private BigDecimal valorFinal;
    private BigDecimal montoInvertido;
    private String plazoTexto;

    public DetalleSimulacionDto(String nombreInstrumento, BigDecimal inversion, BigDecimal interes, int titulos, BigDecimal tasaBruta) {
        this.nombreInstrumento = nombreInstrumento;
        this.inversion = inversion;
        this.interes = interes;
        this.titulos = titulos;
        this.tasaBruta = tasaBruta;
    }


}
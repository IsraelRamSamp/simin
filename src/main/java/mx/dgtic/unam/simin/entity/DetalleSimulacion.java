package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_simulacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleSimulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @Column(name = "nombre_instrumento")
    private String nombreInstrumento;

    @Column(name = "tipo_instrumento")
    private String tipoInstrumento;

    @Column(name = "tasa_bruta", precision = 10, scale = 4)
    private BigDecimal tasaBruta;

    @Column(name = "cantidad_titulos")
    private Integer Titulos;

    @Column(name = "inversion_asignada", precision = 15, scale = 2)
    private BigDecimal inversion;

    @Column(name = "interes_generado", precision = 15, scale = 2)
    private BigDecimal interes;

    @Column(name = "isr_calculado", precision = 15, scale = 2)
    private BigDecimal isrCalculado;

    @Column(name = "remanente", precision = 15, scale = 2)
    private BigDecimal remanente;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    @Column(name = "monto_invertido")
    private BigDecimal montoInvertido;

    @Column(name = "plazo", length = 50)
    private String plazo;

    @Column(name = "inversion_bonddia", precision = 18, scale = 4)
    private BigDecimal inversionBonddia;

    @Column(name = "interes_bonddia", precision = 18, scale = 4)
    private BigDecimal interesBonddia;

    @Column(name = "titulos_bonddia")
    private Integer titulosBonddia;

    @Column(name = "valor_final", precision = 18, scale = 4)
    private BigDecimal valorFinal;

}
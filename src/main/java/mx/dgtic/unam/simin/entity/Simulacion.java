package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulaciones")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Simulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_simulacion")
    private Integer idSimulacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_instrumento")
    private Instrumento instrumento;

    @ManyToOne
    @JoinColumn(name = "id_cartera")
    private Cartera cartera;

    @DecimalMin(value = "0.01", inclusive = true, message = "El monto invertido debe ser mayor a 0.")
    @Column(name = "monto_invertido", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoInvertido;

    @Column(name = "fecha_simulacion", nullable = false)
    private LocalDateTime fechaSimulacion = LocalDateTime.now();

    @Future(message = "La fecha de finalización debe ser futura.")
    @Column(name = "fecha_finalizacion", nullable = false)
    private LocalDateTime fechaFinalizacion;

    @Column(name = "interes_bruto", precision = 15, scale = 2)
    private BigDecimal interesBruto;

    @Column(name = "isr", precision = 15, scale = 2)
    private BigDecimal isr;

    @Column(name = "valor_final", precision = 15, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "plazo",  length = 50)
    private String plazoTexto;

    @Column(name = "compartida_analista")
    private Boolean compartidaAnalista = false;

}
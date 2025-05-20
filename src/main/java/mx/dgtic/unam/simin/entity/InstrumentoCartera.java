package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "instrumentos_cartera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentoCartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instrumentos_cartera")
    private Integer idInstrumentosCartera;

    @ManyToOne
    @JoinColumn(name = "id_cartera", nullable = false)
    private Cartera cartera;

    @ManyToOne
    @JoinColumn(name = "id_instrumento", nullable = false)
    private Instrumento instrumento;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El monto invertido es obligatorio.")
    @Column(name = "monto_invertido", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoInvertido;

    @Column(name = "plazo_dias_bonddia")
    private Integer plazoDiasBonddia;

}
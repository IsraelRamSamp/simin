package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_rendimientos_cartera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialRendimientoCartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_cartera", nullable = false)
    private Cartera cartera;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "valor_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @DecimalMin("0.00")
    @Column(name = "rendimiento_obtenido", precision = 10, scale = 2)
    private BigDecimal rendimientoObtenido;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
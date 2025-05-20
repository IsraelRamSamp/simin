package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_simulaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialSimulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_simulacion", nullable = false)
    private Simulacion simulacion;

    @Size(max = 250, message = "La descripción no debe exceder los 250 caracteres.")
    @Column(name = "descripcion", length = 250)
    private String descripcion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
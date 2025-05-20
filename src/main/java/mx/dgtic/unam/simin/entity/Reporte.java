package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @ManyToOne
    @JoinColumn(name = "id_cartera", nullable = true)
    private Cartera cartera;

    @ManyToOne
    @JoinColumn(name = "id_tipo_reporte", nullable = false)
    private TipoReporte tipoReporte;

    @Column(name = "id_simulacion", nullable = false)
    private Integer idSimulacion;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "correo_usuario", nullable = false)
    private String correoUsuario;

    @Column(name = "tipo_origen", nullable = false)
    private String tipoOrigen; // "CARTERA" o "INSTRUMENTO"

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte = LocalDateTime.now();

}
package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "tipos_reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_reporte")
    private Integer idTipoReporte;

    @NotBlank
    @Size(max = 50)
    @Column(name = "descripcion", length = 50, unique = true, nullable = false)
    private String descripcion;
}
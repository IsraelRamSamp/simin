package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contenido_educativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenido")
    private Integer idContenido;

    @NotBlank
    @Size(max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotBlank
    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion = LocalDateTime.now();
}
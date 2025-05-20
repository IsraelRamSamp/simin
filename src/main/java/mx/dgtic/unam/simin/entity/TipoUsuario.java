package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "tipos_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario")
    private Integer idTipoUsuario;

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 50, message = "Máximo 50 caracteres.")
    @Column(name = "descripcion", length = 50, unique = true, nullable = false)
    private String descripcion;
}
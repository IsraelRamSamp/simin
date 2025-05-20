package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "perfiles_riesgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilRiesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_riesgo")
    private Integer idPerfilRiesgo;

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 50, message = "Máximo 50 caracteres.")
    @Column(name = "descripcion", length = 50, unique = true, nullable = false)
    private String descripcion;
}
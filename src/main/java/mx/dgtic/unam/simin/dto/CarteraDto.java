package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarteraDto {

    private Integer idCartera;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    private String descripcion;

    private BigDecimal valorTotal;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    private String nombreUsuario;

    private String correoUsuario;

    private Integer idPerfilRiesgo;

    private String perfilRiesgoDescripcion;

    private boolean puedeEditar;
    private boolean puedeEliminar;
}

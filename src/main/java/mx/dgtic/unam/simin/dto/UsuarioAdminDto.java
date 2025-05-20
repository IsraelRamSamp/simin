package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioAdminDto {

    private Integer idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String apellidoPaterno;

    @Size(max = 50, message = "Máximo 50 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    private String contrasena;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private Integer idTipoUsuario;

    @NotNull(message = "El rol es obligatorio")
    private Integer idRol;

    private String tipoUsuarioDescripcion;

    private String rolDescripcion;

    private LocalDateTime fechaRegistro;
}
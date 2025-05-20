package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDto {
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

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private Integer idTipoUsuario;

    @NotNull(message = "El rol del usuario es obligatorio")
    private Integer idRol;
}


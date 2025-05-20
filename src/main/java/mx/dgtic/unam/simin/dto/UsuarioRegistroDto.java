package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class UsuarioRegistroDto {
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
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "Debe tener entre 8 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Debe contener letras y números")
    private String contrasena;

    @NotBlank(message = "Debe confirmar la contraseña.")
    @Size(min = 6, max = 100)
    private String confirmarContrasena;


}
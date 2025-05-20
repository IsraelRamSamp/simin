package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioPerfilDto {

    private Integer idUsuario;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido paterno no puede estar vacío.")
    @Size(max = 100, message = "El apellido paterno no debe exceder los 100 caracteres.")
    private String apellidoPaterno;

    @Size(max = 100, message = "El apellido materno no debe exceder los 100 caracteres.")
    private String apellidoMaterno;

    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "Debe ingresar un correo válido.")
    @Size(max = 100, message = "El correo no debe exceder los 100 caracteres.")
    private String correo;

    private String nuevaContrasena;

    private String fechaRegistro;

    private String tipoUsuarioDescripcion;

    private String avatarUrl;

}
package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "Máximo 50 caracteres.")
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 50, message = "Máximo 50 caracteres.")
    @Column(name = "apellido_paterno", length = 50, nullable = false)
    private String apellidoPaterno;

    @Size(max = 50, message = "Máximo 50 caracteres.")
    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ser un correo válido.")
    @Size(max = 100, message = "Máximo 100 caracteres.")
    @Column(name = "correo", length = 100, unique = true, nullable = false)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 255, message = "Máximo 255 caracteres.")
    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @NotNull(message = "El tipo de usuario es obligatorio.")
    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}

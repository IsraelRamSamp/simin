package mx.dgtic.unam.simin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carteras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cartera")
    private Integer idCartera;

    @NotBlank(message = "El nombre de la cartera es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    @Column(name = "nombre_cartera", length = 100, nullable = false)
    private String nombreCartera;

    @DecimalMin(value = "0.00", inclusive = true, message = "El valor total no puede ser negativo.")
    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_perfil_riesgo")
    private PerfilRiesgo perfilRiesgo;

    @OneToMany(mappedBy = "cartera", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InstrumentoCartera> instrumentos = new ArrayList<>();

}

package mx.dgtic.unam.simin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialSimulacionDto {

    private Integer idHistorial;

    @NotBlank(message = "El ID de la simulación es obligatorio.")
    private Integer idSimulacion;

    private String descripcion;

    private LocalDateTime fechaRegistro;
}
package mx.dgtic.unam.simin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReporteDto {
    private Integer idReporte;
    private Integer idCartera;
    private String nombreCartera;
    private String correoUsuario;
    private String nombreUsuario;
    private Integer idTipoReporte;
    private String tipoReporteDescripcion;
    private LocalDateTime fechaReporte;
    private Integer idSimulacion;
    private String tipoOrigen;

}
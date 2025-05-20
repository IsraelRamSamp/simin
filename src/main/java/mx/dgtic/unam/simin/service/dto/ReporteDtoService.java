package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.ReporteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReporteDtoService {
    ReporteDto save(ReporteDto dto);
    List<ReporteDto> findByCarteraId(Integer idCartera);
    List<ReporteDto> findAll();
    Page<ReporteDto> findByFiltrosAdmin(LocalDateTime fechaInicio, LocalDateTime fechaFin, String correo, String nombre, Pageable pageable);
    Page<ReporteDto> findByCorreoUsuario(String correo, Pageable pageable);
    Page<ReporteDto> findReportesParaAnalista(String correo, Pageable pageable);
    void deleteByIdSimulacion(Integer idSimulacion);
    void deleteByIdCartera(Integer idCartera);
    boolean existePorSimulacionId(Integer idSimulacion);
    void deleteById(Integer idReporte);
    ReporteDto findById(Integer id);
    Optional<ReporteDto> findBySimulacionId(Integer idSimulacion);
}
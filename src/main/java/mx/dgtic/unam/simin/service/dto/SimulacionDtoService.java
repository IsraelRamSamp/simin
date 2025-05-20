package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SimulacionDtoService {
    Page<SimulacionDto> findByFilter(String keyword, String origen, Pageable pageable);
    SimulacionDto findById(Integer id);
    SimulacionDto save(SimulacionDto dto);
    SimulacionDto update(Integer id, SimulacionDto dto);
    void delete(Integer id);
    SimulacionDto buildFromCartera(Integer idCartera);
    SimulacionDto buildFromInstrumento(Integer idInstrumento);
    Page<SimulacionDto> findByUsuarioId(Integer usuarioId, Pageable pageable);
    Page<SimulacionDto> findAll(Pageable pageable);
    SimulacionDto calcularSimulacion(SimulacionDto dto);
    Map<String, List<DetalleSimulacionDto>> agruparDetallesPorTipo(List<DetalleSimulacionDto> detalles);
    Integer buscarIdUsuarioPorCorreo(String correo);
    Page<SimulacionDto> findCompartidasParaAnalista(Pageable pageable);
}
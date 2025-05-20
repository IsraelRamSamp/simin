package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.HistorialSimulacionDto;
import java.util.List;

public interface HistorialSimulacionDtoService {
    List<HistorialSimulacionDto> findBySimulacionId(Integer simulacionId);
    HistorialSimulacionDto save(HistorialSimulacionDto dto);
    void delete(Integer id);
}
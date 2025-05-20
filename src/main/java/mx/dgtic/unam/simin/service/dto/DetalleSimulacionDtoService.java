package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;

import java.util.List;

public interface DetalleSimulacionDtoService {
    List<DetalleSimulacionDto> obtenerPorIdSimulacion(Integer idSimulacion);
}
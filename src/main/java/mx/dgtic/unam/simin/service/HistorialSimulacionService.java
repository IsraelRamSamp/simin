package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.HistorialSimulacion;
import java.util.List;
import java.util.Optional;

public interface HistorialSimulacionService {
    List<HistorialSimulacion> findAll();
    Optional<HistorialSimulacion> findById(Integer id);
    HistorialSimulacion save(HistorialSimulacion historialSimulacion);
    void deleteById(Integer id);
    List<HistorialSimulacion> findBySimulacionId(Integer simulacionId);
}
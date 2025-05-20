package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.HistorialRendimientoCartera;
import java.util.List;
import java.util.Optional;

public interface HistorialRendimientoCarteraService {
    List<HistorialRendimientoCartera> findAll();
    Optional<HistorialRendimientoCartera> findById(Integer id);
    HistorialRendimientoCartera save(HistorialRendimientoCartera historial);
    void deleteById(Integer id);
    List<HistorialRendimientoCartera> findByCarteraId(Integer carteraId);
}
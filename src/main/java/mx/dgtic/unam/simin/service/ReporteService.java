package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.Reporte;
import java.util.List;
import java.util.Optional;

public interface ReporteService {
    List<Reporte> findAll();
    Optional<Reporte> findById(Integer id);
    Reporte save(Reporte reporte);
    void deleteById(Integer id);
    List<Reporte> findByCarteraId(Integer carteraId);
}

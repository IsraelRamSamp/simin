package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.TipoReporte;
import java.util.List;
import java.util.Optional;

public interface TipoReporteService {
    List<TipoReporte> findAll();
    Optional<TipoReporte> findById(Integer id);
    TipoReporte save(TipoReporte tipo);
    void deleteById(Integer id);
}
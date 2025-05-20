package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.TipoInstrumento;

import java.util.List;
import java.util.Optional;

public interface TipoInstrumentoService {
    List<TipoInstrumento> findAll();
    Optional<TipoInstrumento> findById(Integer id);
    TipoInstrumento save(TipoInstrumento tipoInstrumento);
    void deleteById(Integer id);
}
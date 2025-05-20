package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.InstrumentoCartera;
import java.util.List;
import java.util.Optional;

public interface InstrumentoCarteraService {
    List<InstrumentoCartera> findAll();
    Optional<InstrumentoCartera> findById(Integer id);
    InstrumentoCartera save(InstrumentoCartera instrumentoCartera);
    void deleteById(Integer id);
    List<InstrumentoCartera> findByCarteraId(Integer carteraId);
}
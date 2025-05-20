package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.Instrumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InstrumentoService {
    List<Instrumento> findAll();
    Optional<Instrumento> findById(Integer id);
    Instrumento save(Instrumento instrumento);
    void deleteById(Integer id);
    List<Instrumento> findByNombreContaining(String nombre);
    List<Instrumento> findByTipoInstrumento(Integer idTipoInstrumento);
    List<Instrumento> findByNombreAndTipo(String nombre, Integer idTipoInstrumento);
    List<Instrumento> buscarYFiltrar(String nombre, Integer tipoInstrumentoId, String sortField, String sortDir);
    Page<Instrumento> buscarInstrumentos(String nombre, Integer idTipoInstrumento, String sortField, String sortDir, Pageable pageable);
}
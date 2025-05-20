package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.Instrumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentoRepository extends JpaRepository<Instrumento, Integer> {

    List<Instrumento> findByNombreContainingIgnoreCase(String nombre);
    List<Instrumento> findByTipoInstrumento_IdTipoInstrumento(Integer idTipoInstrumento);
    List<Instrumento> findByNombreContainingIgnoreCaseAndTipoInstrumento_IdTipoInstrumento(String nombre, Integer idTipoInstrumento);

    List<Instrumento> findByNombreContainingIgnoreCase(String nombre, Sort sort);
    List<Instrumento> findByTipoInstrumento_IdTipoInstrumento(Integer idTipoInstrumento, Sort sort);
    List<Instrumento> findAll(Sort sort);

    Page<Instrumento> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Instrumento> findByTipoInstrumento_IdTipoInstrumento(Integer idTipoInstrumento, Pageable pageable);
    Page<Instrumento> findByNombreContainingIgnoreCaseAndTipoInstrumento_IdTipoInstrumento(String nombre, Integer idTipoInstrumento, Pageable pageable);

    Optional<Instrumento> findByNombreIgnoreCase(String nombre);
}
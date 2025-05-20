package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.InstrumentoCartera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstrumentoCarteraRepository extends JpaRepository<InstrumentoCartera, Integer> {
    List<InstrumentoCartera> findByCartera_IdCartera(Integer idCartera);
    void deleteAllByCartera_IdCartera(Integer idCartera);
}
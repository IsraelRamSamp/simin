package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.HistorialRendimientoCartera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialRendimientoCarteraRepository extends JpaRepository<HistorialRendimientoCartera, Integer> {
    List<HistorialRendimientoCartera> findByCartera_IdCartera(Integer idCartera);
}
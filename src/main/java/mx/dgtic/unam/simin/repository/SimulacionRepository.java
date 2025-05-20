package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.Simulacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulacionRepository extends JpaRepository<Simulacion, Integer> {
    Page<Simulacion> findByUsuario_IdUsuario(Integer idUsuario, Pageable pageable);
    List<Simulacion> findByUsuario_IdUsuario(Integer userId);
    Page<Simulacion> findByInstrumentoIsNotNull(Pageable pageable);
    Page<Simulacion> findByCarteraIsNotNull(Pageable pageable);
    Page<Simulacion> findByCompartidaAnalistaTrue(Pageable pageable);
}
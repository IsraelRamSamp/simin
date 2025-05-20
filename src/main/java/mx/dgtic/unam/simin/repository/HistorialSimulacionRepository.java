package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.HistorialSimulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialSimulacionRepository extends JpaRepository<HistorialSimulacion, Integer> {
    List<HistorialSimulacion> findBySimulacion_IdSimulacion(Integer idSimulacion);
}
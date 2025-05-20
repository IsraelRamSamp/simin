package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.DetalleSimulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleSimulacionRepository extends JpaRepository<DetalleSimulacion, Integer> {
    List<DetalleSimulacion> findBySimulacion_IdSimulacion(Integer idSimulacion);
    void deleteAllBySimulacion_IdSimulacion(Integer idSimulacion);
}
package mx.dgtic.unam.simin.repository;

import jakarta.transaction.Transactional;
import mx.dgtic.unam.simin.dto.ReporteDto;
import mx.dgtic.unam.simin.entity.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer>, JpaSpecificationExecutor<Reporte> {

    List<Reporte> findByCartera_IdCartera(Integer idCartera);
    boolean existsByIdSimulacion(Integer idSimulacion);
    Page<Reporte> findByCorreoUsuario(String correo, Pageable pageable);
    Optional<Reporte> findByIdSimulacion(Integer idSimulacion);

    @Modifying
    @Transactional
    void deleteByIdSimulacion(Integer idSimulacion);

    @Modifying
    @Transactional
    void deleteAllByIdSimulacion(Integer idSimulacion);

    @Modifying
    @Transactional
    void deleteAllByCartera_IdCartera(Integer idCartera);
}
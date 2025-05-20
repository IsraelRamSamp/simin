package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.Cartera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarteraRepository extends JpaRepository<Cartera, Integer> {

    Page<Cartera> findByUsuario_IdUsuario(Integer idUsuario, Pageable pageable);

    Page<Cartera> findByNombreCarteraContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Cartera> findByNombreCarteraContainingIgnoreCaseAndPerfilRiesgo_IdPerfilRiesgo(String nombre, Integer idPerfil, Pageable pageable);

    Page<Cartera> findByPerfilRiesgo_IdPerfilRiesgo(Integer idPerfil, Pageable pageable);

    List<Cartera> findByUsuario_IdUsuario(Integer idUsuario);

    @Query("""
        SELECT c FROM Cartera c
        WHERE c.usuario.correo = :correo
          AND (:keyword IS NULL OR LOWER(c.nombreCartera) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:perfilId IS NULL OR c.perfilRiesgo.idPerfilRiesgo = :perfilId)
    """)
    Page<Cartera> findByFilterAndCorreo(@Param("keyword") String keyword,
                                        @Param("perfilId") Integer perfilId,
                                        @Param("correo") String correo,
                                        Pageable pageable);
}
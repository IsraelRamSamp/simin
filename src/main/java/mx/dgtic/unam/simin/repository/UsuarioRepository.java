package mx.dgtic.unam.simin.repository;

import mx.dgtic.unam.simin.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByCorreoContainingIgnoreCase(String correo);
    List<Usuario> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);
    Page<Usuario> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo, Pageable pageable);
    Page<Usuario> findByTipoUsuarioIdTipoUsuario(Integer tipoUsuarioId, Pageable pageable);
    Page<Usuario> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCaseAndTipoUsuarioIdTipoUsuario(String nombre, String correo, Integer tipoId, Pageable pageable);
    boolean existsByCorreo(String correo);
}

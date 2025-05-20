package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.Usuario;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    void deleteById(Integer id);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByNameOrEmail(String keyword);
    Page<Usuario> buscarPorFiltro(String keyword, Integer tipoId, int page);
}
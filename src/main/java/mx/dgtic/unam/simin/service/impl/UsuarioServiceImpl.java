package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.repository.UsuarioRepository;
import mx.dgtic.unam.simin.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByCorreo(email);
    }

    @Override
    public List<Usuario> findByNameOrEmail(String keyword) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Page<Usuario> buscarPorFiltro(String keyword, Integer tipoId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        if ((keyword == null || keyword.isBlank()) && tipoId == null) {
            return usuarioRepository.findAll(pageable);
        }
        if (keyword != null && !keyword.isBlank() && tipoId != null) {
            return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCaseAndTipoUsuarioIdTipoUsuario(
                    keyword, keyword, tipoId, pageable);
        }
        if (keyword != null && !keyword.isBlank()) {
            return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    keyword, keyword, pageable);
        }
        return usuarioRepository.findByTipoUsuarioIdTipoUsuario(tipoId, pageable);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

}
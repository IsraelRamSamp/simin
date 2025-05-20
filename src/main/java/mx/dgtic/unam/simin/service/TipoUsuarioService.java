package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.TipoUsuario;
import java.util.List;
import java.util.Optional;

public interface TipoUsuarioService {
    List<TipoUsuario> findAll();
    Optional<TipoUsuario> findById(Integer id);
    TipoUsuario save(TipoUsuario tipoUsuario);
    void deleteById(Integer id);
}
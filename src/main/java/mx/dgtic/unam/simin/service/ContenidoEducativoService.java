package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.ContenidoEducativo;
import java.util.List;
import java.util.Optional;

public interface ContenidoEducativoService {
    List<ContenidoEducativo> findAll();
    Optional<ContenidoEducativo> findById(Integer id);
    ContenidoEducativo save(ContenidoEducativo contenidoEducativo);
    void deleteById(Integer id);
}
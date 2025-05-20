package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.TipoUsuario;
import mx.dgtic.unam.simin.repository.TipoUsuarioRepository;
import mx.dgtic.unam.simin.service.TipoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Override
    public List<TipoUsuario> findAll() {
        return tipoUsuarioRepository.findAll();
    }

    @Override
    public Optional<TipoUsuario> findById(Integer id) {
        return tipoUsuarioRepository.findById(id);
    }

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    @Override
    public void deleteById(Integer id) {
        tipoUsuarioRepository.deleteById(id);
    }
}
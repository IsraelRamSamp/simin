package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.ContenidoEducativo;
import mx.dgtic.unam.simin.repository.ContenidoEducativoRepository;
import mx.dgtic.unam.simin.service.ContenidoEducativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContenidoEducativoServiceImpl implements ContenidoEducativoService {

    @Autowired
    private ContenidoEducativoRepository contenidoEducativoRepository;

    @Override
    public List<ContenidoEducativo> findAll() {
        return contenidoEducativoRepository.findAll();
    }

    @Override
    public Optional<ContenidoEducativo> findById(Integer id) {
        return contenidoEducativoRepository.findById(id);
    }

    @Override
    public ContenidoEducativo save(ContenidoEducativo contenidoEducativo) {
        return contenidoEducativoRepository.save(contenidoEducativo);
    }

    @Override
    public void deleteById(Integer id) {
        contenidoEducativoRepository.deleteById(id);
    }
}

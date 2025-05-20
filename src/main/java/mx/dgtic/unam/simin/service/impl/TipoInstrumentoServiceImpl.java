package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.TipoInstrumento;
import mx.dgtic.unam.simin.repository.TipoInstrumentoRepository;
import mx.dgtic.unam.simin.service.TipoInstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoInstrumentoServiceImpl implements TipoInstrumentoService {

    @Autowired
    private TipoInstrumentoRepository tipoInstrumentoRepository;

    @Override
    public List<TipoInstrumento> findAll() {
        return tipoInstrumentoRepository.findAll();
    }

    @Override
    public Optional<TipoInstrumento> findById(Integer id) {
        return tipoInstrumentoRepository.findById(id);
    }

    @Override
    public TipoInstrumento save(TipoInstrumento tipoInstrumento) {
        return tipoInstrumentoRepository.save(tipoInstrumento);
    }

    @Override
    public void deleteById(Integer id) {
        tipoInstrumentoRepository.deleteById(id);
    }
}
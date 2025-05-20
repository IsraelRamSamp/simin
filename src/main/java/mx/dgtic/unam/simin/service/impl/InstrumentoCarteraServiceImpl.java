package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.InstrumentoCartera;
import mx.dgtic.unam.simin.repository.InstrumentoCarteraRepository;
import mx.dgtic.unam.simin.service.InstrumentoCarteraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoCarteraServiceImpl implements InstrumentoCarteraService {

    @Autowired
    private InstrumentoCarteraRepository instrumentoCarteraRepository;

    @Override
    public List<InstrumentoCartera> findAll() {
        return instrumentoCarteraRepository.findAll();
    }

    @Override
    public Optional<InstrumentoCartera> findById(Integer id) {
        return instrumentoCarteraRepository.findById(id);
    }

    @Override
    public InstrumentoCartera save(InstrumentoCartera instrumentoCartera) {
        return instrumentoCarteraRepository.save(instrumentoCartera);
    }

    @Override
    public void deleteById(Integer id) {
        instrumentoCarteraRepository.deleteById(id);
    }

    @Override
    public List<InstrumentoCartera> findByCarteraId(Integer carteraId) {
        return instrumentoCarteraRepository.findByCartera_IdCartera(carteraId);
    }
}

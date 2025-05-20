package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.HistorialRendimientoCartera;
import mx.dgtic.unam.simin.repository.HistorialRendimientoCarteraRepository;
import mx.dgtic.unam.simin.service.HistorialRendimientoCarteraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialRendimientoCarteraServiceImpl implements HistorialRendimientoCarteraService {

    @Autowired
    private HistorialRendimientoCarteraRepository historialRepository;

    @Override
    public List<HistorialRendimientoCartera> findAll() {
        return historialRepository.findAll();
    }

    @Override
    public Optional<HistorialRendimientoCartera> findById(Integer id) {
        return historialRepository.findById(id);
    }

    @Override
    public HistorialRendimientoCartera save(HistorialRendimientoCartera historial) {
        return historialRepository.save(historial);
    }

    @Override
    public void deleteById(Integer id) {
        historialRepository.deleteById(id);
    }

    @Override
    public List<HistorialRendimientoCartera> findByCarteraId(Integer carteraId) {
        return historialRepository.findByCartera_IdCartera(carteraId);
    }
}
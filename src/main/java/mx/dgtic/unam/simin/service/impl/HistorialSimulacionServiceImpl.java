package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.HistorialSimulacion;
import mx.dgtic.unam.simin.repository.HistorialSimulacionRepository;
import mx.dgtic.unam.simin.service.HistorialSimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialSimulacionServiceImpl implements HistorialSimulacionService {

    @Autowired
    private HistorialSimulacionRepository historialSimulacionRepository;

    @Override
    public List<HistorialSimulacion> findAll() {
        return historialSimulacionRepository.findAll();
    }

    @Override
    public Optional<HistorialSimulacion> findById(Integer id) {
        return historialSimulacionRepository.findById(id);
    }

    @Override
    public HistorialSimulacion save(HistorialSimulacion historialSimulacion) {
        return historialSimulacionRepository.save(historialSimulacion);
    }

    @Override
    public void deleteById(Integer id) {
        historialSimulacionRepository.deleteById(id);
    }

    @Override
    public List<HistorialSimulacion> findBySimulacionId(Integer simulacionId) {
        return historialSimulacionRepository.findBySimulacion_IdSimulacion(simulacionId);
    }
}
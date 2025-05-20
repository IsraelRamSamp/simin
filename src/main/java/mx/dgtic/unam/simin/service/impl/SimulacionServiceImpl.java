package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.Simulacion;
import mx.dgtic.unam.simin.repository.SimulacionRepository;
import mx.dgtic.unam.simin.service.SimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimulacionServiceImpl implements SimulacionService {

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Override
    public List<Simulacion> findAll() {
        return simulacionRepository.findAll();
    }

    @Override
    public Optional<Simulacion> findById(Integer id) {
        return simulacionRepository.findById(id);
    }

    @Override
    public Simulacion save(Simulacion simulacion) {
        return simulacionRepository.save(simulacion);
    }

    @Override
    public void deleteById(Integer id) {
        simulacionRepository.deleteById(id);
    }

    @Override
    public List<Simulacion> findByUserId(Integer userId) {
        return simulacionRepository.findByUsuario_IdUsuario(userId);
    }
}

package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.Simulacion;
import java.util.List;
import java.util.Optional;

public interface SimulacionService {
    List<Simulacion> findAll();
    Optional<Simulacion> findById(Integer id);
    Simulacion save(Simulacion simulacion);
    void deleteById(Integer id);
    List<Simulacion> findByUserId(Integer userId);
}
package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.Reporte;
import mx.dgtic.unam.simin.repository.ReporteRepository;
import mx.dgtic.unam.simin.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Override
    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    @Override
    public Optional<Reporte> findById(Integer id) {
        return reporteRepository.findById(id);
    }

    @Override
    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    @Override
    public void deleteById(Integer id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public List<Reporte> findByCarteraId(Integer carteraId) {
        return reporteRepository.findByCartera_IdCartera(carteraId);
    }
}
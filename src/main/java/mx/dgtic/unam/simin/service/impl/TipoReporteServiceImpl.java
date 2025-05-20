package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.TipoReporte;
import mx.dgtic.unam.simin.repository.TipoReporteRepository;
import mx.dgtic.unam.simin.service.TipoReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoReporteServiceImpl implements TipoReporteService {

    @Autowired
    private TipoReporteRepository tipoReporteRepository;

    @Override
    public List<TipoReporte> findAll() {
        return tipoReporteRepository.findAll();
    }

    @Override
    public Optional<TipoReporte> findById(Integer id) {
        return tipoReporteRepository.findById(id);
    }

    @Override
    public TipoReporte save(TipoReporte tipo) {
        return tipoReporteRepository.save(tipo);
    }

    @Override
    public void deleteById(Integer id) {
        tipoReporteRepository.deleteById(id);
    }
}

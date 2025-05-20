package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.Cartera;
import mx.dgtic.unam.simin.repository.CarteraRepository;
import mx.dgtic.unam.simin.service.CarteraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarteraServiceImpl implements CarteraService {

    @Autowired
    private CarteraRepository carteraRepository;

    @Override
    public List<Cartera> findAll() {
        return carteraRepository.findAll();
    }

    @Override
    public Optional<Cartera> findById(Integer id) {
        return carteraRepository.findById(id);
    }

    @Override
    public Cartera save(Cartera cartera) {
        return carteraRepository.save(cartera);
    }

    @Override
    public void deleteById(Integer id) {
        carteraRepository.deleteById(id);
    }

    @Override
    public Page<Cartera> findByUserId(Integer userId, Pageable pageable) {
        return carteraRepository.findByUsuario_IdUsuario(userId, pageable);
    }
}
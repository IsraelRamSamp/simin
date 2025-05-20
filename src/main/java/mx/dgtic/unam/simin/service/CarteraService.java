package mx.dgtic.unam.simin.service;

import mx.dgtic.unam.simin.entity.Cartera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CarteraService {
    List<Cartera> findAll();
    Optional<Cartera> findById(Integer id);
    Cartera save(Cartera cartera);
    void deleteById(Integer id);
    Page<Cartera> findByUserId(Integer userId, Pageable pageable);
}
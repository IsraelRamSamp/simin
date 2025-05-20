package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.CarteraDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarteraDtoService {

    Page<CarteraDto> findByFilter(String keyword, Integer perfilId, Pageable pageable);
    CarteraDto findById(Integer id);
    CarteraDto save(CarteraDto carteraDto);
    CarteraDto update(Integer id, CarteraDto carteraDto);
    void delete(Integer id);
    Page<CarteraDto> findByFilterAndCorreo(String keyword, Integer perfilId, String correo, Pageable pageable);
    List<CarteraDto> findByUsuarioId(Integer idUsuario);
}

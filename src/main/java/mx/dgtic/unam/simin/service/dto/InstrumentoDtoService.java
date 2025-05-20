package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.InstrumentoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstrumentoDtoService {

    Page<InstrumentoDto> findByFilter(String keyword, Integer tipoId, Pageable pageable);

    InstrumentoDto findById(Integer id);

    InstrumentoDto save(InstrumentoDto instrumentoDto);

    InstrumentoDto update(Integer id, InstrumentoDto instrumentoDto);

    void delete(Integer id);
}
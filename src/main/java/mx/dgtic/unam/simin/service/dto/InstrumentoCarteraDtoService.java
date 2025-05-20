package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.InstrumentoCarteraDto;

import java.util.List;

public interface InstrumentoCarteraDtoService {

    List<InstrumentoCarteraDto> findByCarteraId(Integer carteraId);
    InstrumentoCarteraDto save(InstrumentoCarteraDto instrumentoCarteraDto);
    void delete(Integer idInstrumentoCartera);
    void actualizarValorTotalCartera(Integer idCartera);
    InstrumentoCarteraDto findById(Integer id);
    InstrumentoCarteraDto update(Integer id,  InstrumentoCarteraDto instrumentoCarteraDto);
}
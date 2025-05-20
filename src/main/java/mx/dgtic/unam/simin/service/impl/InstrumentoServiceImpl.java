package mx.dgtic.unam.simin.service.impl;

import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.repository.InstrumentoRepository;
import mx.dgtic.unam.simin.service.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoServiceImpl implements InstrumentoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Override
    public List<Instrumento> findAll() {
        return instrumentoRepository.findAll();
    }

    @Override
    public Optional<Instrumento> findById(Integer id) {
        return instrumentoRepository.findById(id);
    }

    @Override
    public Instrumento save(Instrumento instrumento) {
        return instrumentoRepository.save(instrumento);
    }

    @Override
    public void deleteById(Integer id) {
        instrumentoRepository.deleteById(id);
    }

    @Override
    public List<Instrumento> findByNombreContaining(String nombre) {
        return instrumentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Instrumento> findByTipoInstrumento(Integer idTipoInstrumento) {
        return instrumentoRepository.findByTipoInstrumento_IdTipoInstrumento(idTipoInstrumento);
    }

    @Override
    public List<Instrumento> findByNombreAndTipo(String nombre, Integer idTipoInstrumento) {
        return instrumentoRepository.findByNombreContainingIgnoreCaseAndTipoInstrumento_IdTipoInstrumento(nombre, idTipoInstrumento);
    }

    @Override
    public List<Instrumento> buscarYFiltrar(String nombre, Integer tipoInstrumentoId, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        if (nombre != null && !nombre.isBlank()) {
            return instrumentoRepository.findByNombreContainingIgnoreCase(nombre, sort);
        } else if (tipoInstrumentoId != null) {
            return instrumentoRepository.findByTipoInstrumento_IdTipoInstrumento(tipoInstrumentoId, sort);
        } else {
            return instrumentoRepository.findAll(sort);
        }
    }

    @Override
    public Page<Instrumento> buscarInstrumentos(String nombre, Integer idTipoInstrumento, String sortField, String sortDir, Pageable pageable) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (nombre != null && !nombre.isBlank() && idTipoInstrumento != null) {
            return instrumentoRepository.findByNombreContainingIgnoreCaseAndTipoInstrumento_IdTipoInstrumento(nombre, idTipoInstrumento, sortedPageable);
        } else if (nombre != null && !nombre.isBlank()) {
            return instrumentoRepository.findByNombreContainingIgnoreCase(nombre, sortedPageable);
        } else if (idTipoInstrumento != null) {
            return instrumentoRepository.findByTipoInstrumento_IdTipoInstrumento(idTipoInstrumento, sortedPageable);
        } else {
            return instrumentoRepository.findAll(sortedPageable);
        }
    }

}
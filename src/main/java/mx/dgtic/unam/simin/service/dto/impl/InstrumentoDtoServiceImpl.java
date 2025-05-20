package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.InstrumentoDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.entity.TipoInstrumento;
import mx.dgtic.unam.simin.repository.InstrumentoRepository;
import mx.dgtic.unam.simin.repository.TipoInstrumentoRepository;
import mx.dgtic.unam.simin.service.dto.InstrumentoDtoService;
import mx.dgtic.unam.simin.util.InstrumentoUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstrumentoDtoServiceImpl implements InstrumentoDtoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private TipoInstrumentoRepository tipoInstrumentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void initMapper() {
        modelMapper.addMappings(new PropertyMap<Instrumento, InstrumentoDto>() {
            @Override
            protected void configure() {
                map().setIdTipoInstrumento(source.getTipoInstrumento().getIdTipoInstrumento());
                map().setTipoInstrumentoDescripcion(source.getTipoInstrumento().getDescripcion());
            }
        });

        modelMapper.addMappings(new PropertyMap<InstrumentoDto, Instrumento>() {
            @Override
            protected void configure() {
                skip(destination.getTipoInstrumento());
            }
        });
    }

    @Override
    public Page<InstrumentoDto> findByFilter(String keyword, Integer tipoInstrumentoId, Pageable pageable) {
        log.info("Buscando instrumentos - keyword: {}, tipoInstrumentoId: {}", keyword, tipoInstrumentoId);
        Page<Instrumento> instrumentos;

        if (keyword != null && !keyword.isBlank() && tipoInstrumentoId != null) {
            instrumentos = instrumentoRepository.findByNombreContainingIgnoreCaseAndTipoInstrumento_IdTipoInstrumento(
                    keyword, tipoInstrumentoId, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            instrumentos = instrumentoRepository.findByNombreContainingIgnoreCase(keyword, pageable);
        } else if (tipoInstrumentoId != null) {
            instrumentos = instrumentoRepository.findByTipoInstrumento_IdTipoInstrumento(tipoInstrumentoId, pageable);
        } else {
            instrumentos = instrumentoRepository.findAll(pageable);
        }

        List<InstrumentoDto> dtoList = instrumentos.stream()
                .map(instr -> modelMapper.map(instr, InstrumentoDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, instrumentos.getTotalElements());
    }

    @Override
    public InstrumentoDto findById(Integer id) {
        log.info("Buscando instrumento por ID: {}", id);
        Instrumento instrumento = instrumentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Instrumento no encontrado con ID: {}", id);
                    return new EntityNotFoundException("Instrumento no encontrado");
                });
        return modelMapper.map(instrumento, InstrumentoDto.class);
    }

    @Override
    public InstrumentoDto save(InstrumentoDto instrumentoDto) {
        log.info("Guardando nuevo instrumento: {}", instrumentoDto.getNombre());
        TipoInstrumento tipoInstrumento = tipoInstrumentoRepository.findById(instrumentoDto.getIdTipoInstrumento())
                .orElseThrow(() -> {
                    log.error("Tipo de instrumento no encontrado con ID: {}", instrumentoDto.getIdTipoInstrumento());
                    return new EntityNotFoundException("Tipo de instrumento no encontrado");
                });

        Instrumento instrumento = modelMapper.map(instrumentoDto, Instrumento.class);
        instrumento.setTipoInstrumento(tipoInstrumento);
        instrumento.setFechaEmision(instrumentoDto.getFechaEmision());
        instrumento.setFechaVencimiento(instrumentoDto.getFechaVencimiento());
        instrumento.setTasaCupon(instrumentoDto.getTasaCupon());

        String tipo = tipoInstrumento.getDescripcion().toUpperCase();

        if (tipo.equals("BONO") || tipo.equals("UDIBONOS") || tipo.equals("BONDES")) {
            instrumento.setPrecioMercado(instrumentoDto.getPrecioMercado());
        } else {
            instrumento.setPrecioMercado(InstrumentoUtils.calcularPrecioMercado(instrumento));
        }

        Instrumento saved = instrumentoRepository.save(instrumento);
        log.info("Instrumento guardado con ID: {}", saved.getIdInstrumento());
        return modelMapper.map(saved, InstrumentoDto.class);
    }

    @Override
    public InstrumentoDto update(Integer id, InstrumentoDto instrumentoDto) {
        log.info("Actualizando instrumento con ID: {}", id);
        Instrumento instrumento = instrumentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Instrumento no encontrado con ID: {}", id);
                    return new EntityNotFoundException("Instrumento no encontrado");
                });

        TipoInstrumento tipoInstrumento = tipoInstrumentoRepository.findById(instrumentoDto.getIdTipoInstrumento())
                .orElseThrow(() -> {
                    log.error("Tipo de instrumento no encontrado con ID: {}", instrumentoDto.getIdTipoInstrumento());
                    return new EntityNotFoundException("Tipo de instrumento no encontrado");
                });

        instrumento.setNombre(instrumentoDto.getNombre());
        instrumento.setDescripcionAdicional(instrumentoDto.getDescripcionAdicional());
        instrumento.setTipoRendimiento(instrumentoDto.getTipoRendimiento());
        instrumento.setPlazoMeses(instrumentoDto.getPlazoMeses());
        instrumento.setPlazoAnos(instrumentoDto.getPlazoAnos());
        instrumento.setFrecuenciaPagos(Enum.valueOf(Instrumento.FrecuenciaPagos.class, instrumentoDto.getFrecuenciaPagos()));
        instrumento.setTasaInteres(instrumentoDto.getTasaInteres());
        instrumento.setValorNominal(instrumentoDto.getValorNominal());
        instrumento.setTipoInstrumento(tipoInstrumento);
        instrumento.setFechaEmision(instrumentoDto.getFechaEmision());
        instrumento.setFechaVencimiento(instrumentoDto.getFechaVencimiento());
        instrumento.setTasaCupon(instrumentoDto.getTasaCupon());

        String tipo = tipoInstrumento.getDescripcion().toUpperCase();

        if (tipo.equals("BONO") || tipo.equals("UDIBONOS") || tipo.equals("BONDES")) {
            instrumento.setPrecioMercado(instrumentoDto.getPrecioMercado());
        } else {
            instrumento.setPrecioMercado(InstrumentoUtils.calcularPrecioMercado(instrumento));
        }

        Instrumento updated = instrumentoRepository.save(instrumento);
        log.info("Instrumento actualizado correctamente con ID: {}", updated.getIdInstrumento());
        return modelMapper.map(updated, InstrumentoDto.class);
    }

    @Override
    public void delete(Integer id) {
        log.info("Eliminando instrumento con ID: {}", id);
        instrumentoRepository.deleteById(id);
    }
}
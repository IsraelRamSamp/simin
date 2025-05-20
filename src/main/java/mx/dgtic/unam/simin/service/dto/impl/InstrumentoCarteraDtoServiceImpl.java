package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import mx.dgtic.unam.simin.dto.InstrumentoCarteraDto;
import mx.dgtic.unam.simin.entity.Cartera;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.entity.InstrumentoCartera;
import mx.dgtic.unam.simin.repository.CarteraRepository;
import mx.dgtic.unam.simin.repository.InstrumentoCarteraRepository;
import mx.dgtic.unam.simin.repository.InstrumentoRepository;
import mx.dgtic.unam.simin.service.dto.InstrumentoCarteraDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentoCarteraDtoServiceImpl implements InstrumentoCarteraDtoService {

    @Autowired
    private InstrumentoCarteraRepository instrumentoCarteraRepository;

    @Autowired
    private CarteraRepository carteraRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    private String formatearPlazoTexto(Integer dias) {
        if (dias == null || dias <= 0) return "N/D";

        int anios = dias / 365;
        int restantes = dias % 365;

        if (anios > 0 && restantes == 0) {
            return anios + " año" + (anios > 1 ? "s" : "");
        } else if (anios > 0) {
            return anios + " año" + (anios > 1 ? "s" : "") + " " + restantes + " día" + (restantes > 1 ? "s" : "");
        } else {
            return dias + " día" + (dias > 1 ? "s" : "");
        }
    }

    @Override
    public List<InstrumentoCarteraDto> findByCarteraId(Integer carteraId) {
        return instrumentoCarteraRepository.findByCartera_IdCartera(carteraId)
                .stream()
                .map(ic -> {
                    Instrumento instrumento = ic.getInstrumento();
                    Cartera cartera = ic.getCartera();

                    InstrumentoCarteraDto dto = new InstrumentoCarteraDto();
                    dto.setIdInstrumentosCartera(ic.getIdInstrumentosCartera());
                    dto.setIdCartera(cartera.getIdCartera());
                    dto.setNombreCartera(cartera.getNombreCartera());
                    dto.setIdInstrumento(instrumento.getIdInstrumento());
                    dto.setNombreInstrumento(instrumento.getNombre());
                    dto.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());
                    dto.setTitulos(ic.getCantidad());
                    dto.setTasaInteres(instrumento.getTasaInteres());
                    dto.setPrecioMercado(instrumento.getPrecioMercado());
                    dto.setMontoInvertido(ic.getMontoInvertido());
                    dto.setPlazoDiasBonddia(ic.getPlazoDiasBonddia());

                    Integer dias = "BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())
                            ? ic.getPlazoDiasBonddia()
                            : instrumento.getDiasPlazo();
                    dto.setPlazoTexto(formatearPlazoTexto(dias));

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public InstrumentoCarteraDto save(InstrumentoCarteraDto dto) {
        Cartera cartera = carteraRepository.findById(dto.getIdCartera())
                .orElseThrow(() -> new EntityNotFoundException("Cartera no encontrada"));

        Instrumento instrumento = instrumentoRepository.findById(dto.getIdInstrumento())
                .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));

        BigDecimal montoInvertido = dto.getMontoInvertido();
        if (montoInvertido == null || montoInvertido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a invertir debe ser mayor a cero.");
        }

        if ("BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())) {
            if (dto.getPlazoDiasBonddia() == null || dto.getPlazoDiasBonddia() <= 0) {
                throw new IllegalArgumentException("Debe establecer un plazo válido en días para BONDDIA.");
            }
        }

        BigDecimal precioMercado = instrumento.getPrecioMercado();
        int titulos = montoInvertido.divide(precioMercado, 0, RoundingMode.DOWN).intValue();

        InstrumentoCartera entidad = new InstrumentoCartera();
        entidad.setCartera(cartera);
        entidad.setInstrumento(instrumento);
        entidad.setCantidad(titulos);
        entidad.setMontoInvertido(montoInvertido);
        entidad.setPlazoDiasBonddia(dto.getPlazoDiasBonddia());

        instrumentoCarteraRepository.save(entidad);
        actualizarValorTotalCartera(cartera.getIdCartera());

        dto.setIdInstrumentosCartera(entidad.getIdInstrumentosCartera());
        dto.setNombreCartera(cartera.getNombreCartera());
        dto.setNombreInstrumento(instrumento.getNombre());
        dto.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());
        dto.setPrecioMercado(precioMercado);
        dto.setTasaInteres(instrumento.getTasaInteres());
        dto.setTitulos(titulos);

        Integer dias = "BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())
                ? dto.getPlazoDiasBonddia()
                : instrumento.getDiasPlazo();
        dto.setPlazoTexto(formatearPlazoTexto(dias));

        return dto;
    }

    @Override
    public InstrumentoCarteraDto update(Integer id, InstrumentoCarteraDto dto) {
        InstrumentoCartera entidad = instrumentoCarteraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento en cartera no encontrado"));

        Instrumento instrumento = instrumentoRepository.findById(dto.getIdInstrumento())
                .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));

        BigDecimal monto = dto.getMontoInvertido();
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a invertir debe ser mayor a cero.");
        }

        if ("BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())) {
            if (dto.getPlazoDiasBonddia() == null || dto.getPlazoDiasBonddia() <= 0) {
                throw new IllegalArgumentException("Debe establecer un plazo válido en días para BONDDIA.");
            }
            entidad.setPlazoDiasBonddia(dto.getPlazoDiasBonddia());
        } else {
            entidad.setPlazoDiasBonddia(null);
        }

        BigDecimal precio = instrumento.getPrecioMercado();
        int titulos = monto.divide(precio, 0, RoundingMode.DOWN).intValue();

        entidad.setInstrumento(instrumento);
        entidad.setCantidad(titulos);
        entidad.setMontoInvertido(monto);
        instrumentoCarteraRepository.save(entidad);

        actualizarValorTotalCartera(entidad.getCartera().getIdCartera());

        dto.setNombreInstrumento(instrumento.getNombre());
        dto.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());
        dto.setPrecioMercado(precio);
        dto.setTasaInteres(instrumento.getTasaInteres());
        dto.setTitulos(titulos);

        Integer dias = "BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())
                ? dto.getPlazoDiasBonddia()
                : instrumento.getDiasPlazo();
        dto.setPlazoTexto(formatearPlazoTexto(dias));

        return dto;
    }

    @Override
    public InstrumentoCarteraDto findById(Integer id) {
        InstrumentoCartera ic = instrumentoCarteraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento en cartera no encontrado"));

        Instrumento instrumento = ic.getInstrumento();

        InstrumentoCarteraDto dto = new InstrumentoCarteraDto();
        dto.setIdInstrumentosCartera(ic.getIdInstrumentosCartera());
        dto.setIdCartera(ic.getCartera().getIdCartera());
        dto.setNombreCartera(ic.getCartera().getNombreCartera());
        dto.setIdInstrumento(instrumento.getIdInstrumento());
        dto.setNombreInstrumento(instrumento.getNombre());
        dto.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());
        dto.setTitulos(ic.getCantidad());
        dto.setTasaInteres(instrumento.getTasaInteres());
        dto.setPrecioMercado(instrumento.getPrecioMercado());
        dto.setMontoInvertido(ic.getMontoInvertido());
        dto.setPlazoDiasBonddia(ic.getPlazoDiasBonddia());

        Integer dias = "BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())
                ? ic.getPlazoDiasBonddia()
                : instrumento.getDiasPlazo();
        dto.setPlazoTexto(formatearPlazoTexto(dias));

        return dto;
    }

    @Override
    public void delete(Integer id) {
        InstrumentoCartera entidad = instrumentoCarteraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento en cartera no encontrado"));
        Integer idCartera = entidad.getCartera().getIdCartera();
        instrumentoCarteraRepository.deleteById(id);
        actualizarValorTotalCartera(idCartera);
    }

    @Override
    public void actualizarValorTotalCartera(Integer idCartera) {
        List<InstrumentoCartera> instrumentos = instrumentoCarteraRepository.findByCartera_IdCartera(idCartera);

        BigDecimal total = instrumentos.stream()
                .map(InstrumentoCartera::getMontoInvertido)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        carteraRepository.findById(idCartera).ifPresent(c -> {
            c.setValorTotal(total);
            carteraRepository.save(c);
        });
    }
}
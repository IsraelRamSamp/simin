package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.*;
import mx.dgtic.unam.simin.repository.*;
import mx.dgtic.unam.simin.service.dto.DetalleSimulacionDtoService;
import mx.dgtic.unam.simin.simulation.InstrumentoCalculadorFactory;
import mx.dgtic.unam.simin.service.dto.SimulacionDtoService;
import mx.dgtic.unam.simin.simulation.strategy.InstrumentoCalculador;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SimulacionDtoServiceImpl implements SimulacionDtoService {

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteraRepository carteraRepository;

    @Autowired
    private DetalleSimulacionRepository detalleSimulacionRepository;

    @Autowired
    private DetalleSimulacionDtoService detalleSimulacionDtoService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<SimulacionDto> findByFilter(String keyword, String origen, Pageable pageable) {
        Page<Simulacion> simulaciones;

        if ("instrumento".equalsIgnoreCase(origen)) {
            simulaciones = simulacionRepository.findByInstrumentoIsNotNull(pageable);
        } else if ("cartera".equalsIgnoreCase(origen)) {
            simulaciones = simulacionRepository.findByCarteraIsNotNull(pageable);
        } else {
            simulaciones = simulacionRepository.findAll(pageable);
        }

        List<SimulacionDto> dtoList = simulaciones.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, simulaciones.getTotalElements());
    }

    @Override
    public SimulacionDto findById(Integer id) {
        Simulacion simulacion = simulacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulación no encontrada"));

        SimulacionDto dto = mapToDto(simulacion);

        // 🔧 Asegurarse de que estén siempre los datos del usuario
        if ((dto.getCorreoUsuario() == null || dto.getNombreUsuario() == null)
                && simulacion.getCartera() != null
                && simulacion.getCartera().getUsuario() != null) {

            Usuario usuario = simulacion.getCartera().getUsuario();
            dto.setCorreoUsuario(usuario.getCorreo());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno());
        }

        // Recuperar y mapear detalles
        List<DetalleSimulacion> detalles = detalleSimulacionRepository.findBySimulacion_IdSimulacion(id);
        List<DetalleSimulacionDto> detalleDtos = detalles.stream()
                .map(d -> {
                    DetalleSimulacionDto mapped = modelMapper.map(d, DetalleSimulacionDto.class);
                    if (mapped.getInversionBonddia() == null) mapped.setInversionBonddia(BigDecimal.ZERO);
                    if (mapped.getInteresBonddia() == null) mapped.setInteresBonddia(BigDecimal.ZERO);
                    if (mapped.getRemanente() == null) mapped.setRemanente(BigDecimal.ZERO);
                    if (mapped.getIsrCalculado() == null) mapped.setIsrCalculado(BigDecimal.ZERO);
                    if (mapped.getValorFinal() == null) {
                        BigDecimal inversion = Optional.ofNullable(mapped.getInversion()).orElse(BigDecimal.ZERO);
                        BigDecimal interes = Optional.ofNullable(mapped.getInteres()).orElse(BigDecimal.ZERO);
                        BigDecimal isr = Optional.ofNullable(mapped.getIsrCalculado()).orElse(BigDecimal.ZERO);
                        BigDecimal remanente = Optional.ofNullable(mapped.getRemanente()).orElse(BigDecimal.ZERO);
                        mapped.setValorFinal(inversion.add(interes).add(remanente).subtract(isr));
                    }
                    return mapped;
                }).collect(Collectors.toList());

        dto.setDetalles(detalleDtos);

        return dto;
    }

    @Override
    @Transactional
    public SimulacionDto save(SimulacionDto dto) {
        log.info("Guardando simulación para usuario ID: {}", dto.getIdUsuario());

        if (dto.getIdUsuario() == null) throw new IllegalArgumentException("Debe establecer el usuario.");
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty())
            throw new IllegalArgumentException("La simulación debe tener al menos un detalle.");

        Simulacion simulacion = mapToEntity(dto);
        simulacion.setFechaSimulacion(LocalDateTime.now());
        simulacion = simulacionRepository.save(simulacion);

        List<DetalleSimulacion> detallesGuardados = new ArrayList<>();
        for (DetalleSimulacionDto detDto : dto.getDetalles()) {
            DetalleSimulacion detalle = modelMapper.map(detDto, DetalleSimulacion.class);
            detalle.setSimulacion(simulacion);
            detallesGuardados.add(detalleSimulacionRepository.save(detalle));
        }

        SimulacionDto dtoGuardado = mapToDto(simulacion);
        dtoGuardado.setDetalles(detallesGuardados.stream().map(d -> modelMapper.map(d, DetalleSimulacionDto.class)).toList());

        log.info("Simulación guardada exitosamente con ID: {}", dtoGuardado.getIdSimulacion());
        return dtoGuardado;
    }

    @Override
    public SimulacionDto update(Integer id, SimulacionDto dto) {
        log.info("Actualizando simulación ID: {}", id);
        Simulacion simulacion = simulacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulación no encontrada"));
        simulacion.setMontoInvertido(dto.getMontoInvertido());
        simulacion.setFechaFinalizacion(dto.getFechaFinalizacion());

        Simulacion updated = simulacionRepository.save(simulacion);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando simulación ID: {}", id);
        Simulacion simulacion = simulacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulación no encontrada"));

        detalleSimulacionRepository.deleteAllBySimulacion_IdSimulacion(id);
        reporteRepository.deleteByIdSimulacion(id);
        simulacionRepository.delete(simulacion);
        log.info("Simulación eliminada correctamente");
    }

    @Override
    public Page<SimulacionDto> findByUsuarioId(Integer usuarioId, Pageable pageable) {
        Page<Simulacion> simulaciones = simulacionRepository.findByUsuario_IdUsuario(usuarioId, pageable);
        List<SimulacionDto> dtoList = simulaciones.getContent().stream().map(this::mapToDto).collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, simulaciones.getTotalElements());
    }

    @Override
    public Page<SimulacionDto> findAll(Pageable pageable) {
        Page<Simulacion> simulaciones = simulacionRepository.findAll(pageable);
        List<SimulacionDto> dtoList = simulaciones.getContent().stream().map(this::mapToDto).collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, simulaciones.getTotalElements());
    }

    @Override
    public SimulacionDto buildFromInstrumento(Integer idInstrumento) {
        Instrumento instrumento = instrumentoRepository.findById(idInstrumento)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));

        Usuario usuario = usuarioRepository.findByCorreo(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new EntityNotFoundException("Usuario autenticado no encontrado"));

        SimulacionDto dto = new SimulacionDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellidoPaterno());
        dto.setCorreoUsuario(usuario.getCorreo());

        dto.setIdInstrumento(instrumento.getIdInstrumento());
        dto.setNombreInstrumento(instrumento.getNombre());
        dto.setPrecioMercado(instrumento.getPrecioMercado());
        dto.setTasaInteres(instrumento.getTasaInteres());
        dto.setValorNominal(instrumento.getValorNominal());
        dto.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());

        // Calcular plazo total en días
        int totalDias = 0;
        if (instrumento.getDiasPlazo() != null && instrumento.getDiasPlazo() > 0) {
            totalDias = instrumento.getDiasPlazo();
        } else {
            int anos = instrumento.getPlazoAnos() != null ? instrumento.getPlazoAnos() : 0;
            int meses = instrumento.getPlazoMeses() != null ? instrumento.getPlazoMeses() : 0;
            totalDias = anos * 365 + meses * 30;
        }

        if (totalDias <= 0) {
            totalDias = 1;
        }

        // Asignar fecha de finalización
        LocalDateTime fechaFinal = LocalDateTime.now().plusDays(totalDias);
        dto.setFechaFinalizacion(fechaFinal);

        // Asignar texto del plazo
        if (totalDias >= 365) {
            dto.setPlazoTexto((totalDias / 365) + " año(s)");
        } else if (totalDias >= 30) {
            dto.setPlazoTexto((totalDias / 30) + " mes(es)");
        } else {
            dto.setPlazoTexto(totalDias + " día(s)");
        }

        return dto;
    }

    @Override
    public SimulacionDto buildFromCartera(Integer idCartera) {
        Cartera cartera = carteraRepository.findById(idCartera)
                .orElseThrow(() -> new EntityNotFoundException("Cartera no encontrada"));

        SimulacionDto dto = new SimulacionDto();
        dto.setIdCartera(cartera.getIdCartera());
        dto.setNombreCartera(cartera.getNombreCartera());
        dto.setIdUsuario(cartera.getUsuario().getIdUsuario());

        // Plazo general no aplica: se colocan como "variables"
        dto.setFechaFinalizacion(null);
        dto.setPlazoTexto("Plazos variables");

        BigDecimal interesBrutoTotal = BigDecimal.ZERO;
        BigDecimal isrTotal = BigDecimal.ZERO;
        BigDecimal valorFinalTotal = BigDecimal.ZERO;
        BigDecimal remanenteTotal = BigDecimal.ZERO;
        BigDecimal montoInvertidoTotal = BigDecimal.ZERO;

        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        for (InstrumentoCartera ic : cartera.getInstrumentos()) {
            Instrumento instrumento = ic.getInstrumento();
            BigDecimal monto = ic.getMontoInvertido();

            if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) continue;

            // Obtener plazo adecuado
            long dias;
            if ("BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())) {
                dias = ic.getPlazoDiasBonddia() != null ? ic.getPlazoDiasBonddia() : 1;
            } else {
                dias = instrumento.getDiasPlazo() != null ? instrumento.getDiasPlazo() : 1;
            }

            // Usar calculador adecuado
            InstrumentoCalculador calculador = InstrumentoCalculadorFactory.obtenerCalculador(instrumento);
            SimulacionDto parcial = calculador.calcular(instrumento, monto, dias);

            // Agregar detalles individuales
            detalles.addAll(parcial.getDetalles());

            // Acumular totales
            interesBrutoTotal = interesBrutoTotal.add(parcial.getInteresBruto());
            isrTotal = isrTotal.add(parcial.getIsr());
            remanenteTotal = remanenteTotal.add(parcial.getRemanente());
            valorFinalTotal = valorFinalTotal.add(parcial.getValorFinal());
            montoInvertidoTotal = montoInvertidoTotal.add(monto);
        }

        // Asignar resultados al DTO principal
        dto.setInteresBruto(interesBrutoTotal.setScale(2, RoundingMode.HALF_UP));
        dto.setIsr(isrTotal.setScale(2, RoundingMode.HALF_UP));
        dto.setRemanente(remanenteTotal.setScale(2, RoundingMode.HALF_UP));
        dto.setValorFinal(valorFinalTotal.setScale(2, RoundingMode.HALF_UP));
        dto.setMontoInvertido(montoInvertidoTotal.setScale(2, RoundingMode.HALF_UP));
        dto.setRendimiento(valorFinalTotal.subtract(montoInvertidoTotal).setScale(2, RoundingMode.HALF_UP));
        dto.setDetalles(detalles);

        return dto;
    }

    @Override
    public SimulacionDto calcularSimulacion(SimulacionDto dto) {
        BigDecimal monto = dto.getMontoInvertido();
        long dias = 0;

        boolean esSimulacionPorInstrumento = dto.getIdInstrumento() != null && dto.getIdCartera() == null;
        boolean esSimulacionPorCartera = dto.getIdCartera() != null && dto.getIdInstrumento() == null;

        BigDecimal interesBruto = BigDecimal.ZERO;
        BigDecimal isr = BigDecimal.ZERO;
        BigDecimal valorFinal = BigDecimal.ZERO;
        BigDecimal remanenteTotal = BigDecimal.ZERO;
        BigDecimal montoInvertidoTotal = BigDecimal.ZERO;
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        if (esSimulacionPorInstrumento) {
            Instrumento instrumento;

            if ("BONDDIA".equalsIgnoreCase(dto.getTipoInstrumentoDescripcion())) {
                if (dto.getPlazoDiasManual() == null || dto.getPlazoDiasManual() <= 0) {
                    throw new IllegalArgumentException("Debe establecer un plazo en días válido para BONDDIA.");
                }
                dias = dto.getPlazoDiasManual();
                dto.setFechaFinalizacion(LocalDateTime.now().plusDays(dias));

                // Obtener BONDDIA manualmente y setear campos
                instrumento = instrumentoRepository.findByNombreIgnoreCase("BONDDIA")
                        .orElseThrow(() -> new EntityNotFoundException("Instrumento BONDDIA no encontrado"));

                dto.setIdInstrumento(instrumento.getIdInstrumento());
                dto.setNombreInstrumento(instrumento.getNombre());
                dto.setPrecioMercado(instrumento.getPrecioMercado());
                dto.setValorNominal(instrumento.getValorNominal());
            } else {
                if (dto.getFechaFinalizacion() == null) {
                    throw new IllegalArgumentException("Debe establecer una fecha de finalización para la simulación.");
                }
                dias = ChronoUnit.DAYS.between(LocalDate.now(), dto.getFechaFinalizacion().toLocalDate());
                validarFechaPorTipo(dto);

                instrumento = instrumentoRepository.findById(dto.getIdInstrumento())
                        .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));
            }

            InstrumentoCalculador calculador = InstrumentoCalculadorFactory.obtenerCalculador(instrumento);
            SimulacionDto parcial = calculador.calcular(instrumento, monto, dias);

            parcial.setIdInstrumento(instrumento.getIdInstrumento());
            parcial.setNombreInstrumento(instrumento.getNombre());
            parcial.setValorNominal(instrumento.getValorNominal());
            parcial.setPrecioMercado(instrumento.getPrecioMercado());
            parcial.setTipoInstrumentoDescripcion(instrumento.getTipoInstrumento().getDescripcion());
            parcial.setMontoInvertido(monto);
            parcial.setFechaFinalizacion(dto.getFechaFinalizacion());

            String tipo = instrumento.getTipoInstrumento().getDescripcion();
            if ("BONO".equalsIgnoreCase(tipo) || "UDIBONOS".equalsIgnoreCase(tipo) || "BONDES".equalsIgnoreCase(tipo)) {
                parcial.setTasaInteres(instrumento.getTasaCupon());
            } else {
                parcial.setTasaInteres(instrumento.getTasaInteres());
            }

            if ("BONDDIA".equalsIgnoreCase(parcial.getTipoInstrumentoDescripcion())) {
                parcial.setPlazoTexto(dto.getPlazoDiasManual() + " día(s)");
            } else {
                int totalDias = instrumento.getDiasPlazo() != null && instrumento.getDiasPlazo() > 0
                        ? instrumento.getDiasPlazo()
                        : instrumento.getPlazoAnos() * 365 + instrumento.getPlazoMeses() * 30;

                parcial.setPlazoTexto(totalDias >= 365 ? (totalDias / 365) + " año(s)"
                        : totalDias >= 30 ? (totalDias / 30) + " mes(es)" : totalDias + " día(s)");
            }

            parcial.setIdUsuario(dto.getIdUsuario());
            parcial.setCorreoUsuario(dto.getCorreoUsuario());
            parcial.setNombreUsuario(dto.getNombreUsuario());
            parcial.setPlazoDiasManual(dto.getPlazoDiasManual());
            parcial.setIdInstrumento(dto.getIdInstrumento());

            return parcial;

        } else if (esSimulacionPorCartera) {
            Cartera cartera = carteraRepository.findById(dto.getIdCartera())
                    .orElseThrow(() -> new EntityNotFoundException("Cartera no encontrada"));

            long plazoMaximo = 1;

            for (InstrumentoCartera ic : cartera.getInstrumentos()) {
                Instrumento instrumento = ic.getInstrumento();
                BigDecimal parcialMonto = ic.getMontoInvertido();

                if (parcialMonto == null || parcialMonto.compareTo(BigDecimal.ZERO) <= 0) continue;

                long diasInd;
                if ("BONDDIA".equalsIgnoreCase(instrumento.getTipoInstrumento().getDescripcion())) {
                    diasInd = ic.getPlazoDiasBonddia() != null ? ic.getPlazoDiasBonddia() : 1;
                } else {
                    diasInd = instrumento.getDiasPlazo() != null ? instrumento.getDiasPlazo() : 1;
                }

                plazoMaximo = Math.max(plazoMaximo, diasInd);

                InstrumentoCalculador calculador = InstrumentoCalculadorFactory.obtenerCalculador(instrumento);
                SimulacionDto parcial = calculador.calcular(instrumento, parcialMonto, diasInd);

                interesBruto = interesBruto.add(parcial.getInteresBruto());
                isr = isr.add(parcial.getIsr());
                valorFinal = valorFinal.add(parcial.getValorFinal());
                remanenteTotal = remanenteTotal.add(parcial.getRemanente());
                montoInvertidoTotal = montoInvertidoTotal.add(parcialMonto);
                detalles.addAll(parcial.getDetalles());
            }

            detalles.sort(Comparator.comparing(
                    DetalleSimulacionDto::getTipoInstrumento,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            ));

            dto.setInteresBruto(interesBruto.setScale(2, RoundingMode.HALF_UP));
            dto.setIsr(isr.setScale(2, RoundingMode.HALF_UP));
            dto.setValorFinal(valorFinal.setScale(2, RoundingMode.HALF_UP));
            dto.setRendimiento(valorFinal.subtract(montoInvertidoTotal).setScale(2, RoundingMode.HALF_UP));
            dto.setRemanente(remanenteTotal.setScale(2, RoundingMode.HALF_UP));
            dto.setMontoInvertido(montoInvertidoTotal.setScale(2, RoundingMode.HALF_UP));
            dto.setDetalles(detalles);

            dto.setFechaFinalizacion(LocalDateTime.now().plusDays(plazoMaximo));
            dto.setPlazoTexto("Hasta " + plazoMaximo + " días (plazo más largo)");

            return dto;

        } else {
            throw new IllegalArgumentException("Debe especificarse un instrumento o una cartera para la simulación.");
        }
    }

    private void validarFechaPorTipo(SimulacionDto dto) {
        String tipo = dto.getTipoInstrumentoDescripcion();
        if (tipo == null || tipo.isBlank()) return;

        if (!"BONDDIA".equalsIgnoreCase(tipo)) return;

        if (dto.getPlazoDiasManual() == null || dto.getPlazoDiasManual() <= 0) {
            throw new IllegalArgumentException("Debe establecer un plazo en días válido para BONDDIA.");
        }

        if (dto.getPlazoDiasManual() > 365) {
            throw new IllegalArgumentException("El plazo ingresado para BONDDIA no puede ser mayor a 365 días.");
        }
    }

    private SimulacionDto mapToDto(Simulacion simulacion) {
        SimulacionDto dto = modelMapper.map(simulacion, SimulacionDto.class);

        // Usuario (vía simulación directa o vía cartera)
        if (simulacion.getUsuario() != null) {
            dto.setIdUsuario(simulacion.getUsuario().getIdUsuario());
            dto.setNombreUsuario(simulacion.getUsuario().getNombre() + " " + simulacion.getUsuario().getApellidoPaterno());
            dto.setCorreoUsuario(simulacion.getUsuario().getCorreo());
        } else if (simulacion.getCartera() != null && simulacion.getCartera().getUsuario() != null) {
            Usuario usuario = simulacion.getCartera().getUsuario();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellidoPaterno());
            dto.setCorreoUsuario(usuario.getCorreo());
        }

        // Instrumento (simulación individual)
        if (simulacion.getInstrumento() != null) {
            Instrumento ins = simulacion.getInstrumento();
            dto.setIdInstrumento(ins.getIdInstrumento());
            dto.setNombreInstrumento(ins.getNombre());
            dto.setTipoInstrumentoDescripcion(ins.getTipoInstrumento() != null ? ins.getTipoInstrumento().getDescripcion() : null);
            dto.setPrecioMercado(ins.getPrecioMercado());
            dto.setValorNominal(ins.getValorNominal());
            dto.setTasaInteres(ins.getTasaCupon() != null ? ins.getTasaCupon() : ins.getTasaInteres());
        }

        // Cartera (simulación grupal)
        if (simulacion.getCartera() != null) {
            dto.setIdCartera(simulacion.getCartera().getIdCartera());
            dto.setNombreCartera(simulacion.getCartera().getNombreCartera());
        }

        dto.setInteresBruto(simulacion.getInteresBruto());
        dto.setIsr(simulacion.getIsr());
        dto.setValorFinal(simulacion.getValorFinal());
        dto.setPlazoTexto(simulacion.getPlazoTexto());
        dto.setFechaFinalizacion(simulacion.getFechaFinalizacion());
        dto.setMontoInvertido(simulacion.getMontoInvertido());
        dto.setCompartidaAnalista(simulacion.getCompartidaAnalista());

        return dto;
    }

    private Simulacion mapToEntity(SimulacionDto dto) {
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("El idUsuario no puede ser null al guardar la simulación.");
        }

        Simulacion simulacion = new Simulacion();
        simulacion.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado")));

        // Cargar instrumento si aplica
        if (dto.getIdInstrumento() != null) {
            simulacion.setInstrumento(instrumentoRepository.findById(dto.getIdInstrumento())
                    .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado")));
        }

        // Cargar cartera si aplica
        if (dto.getIdCartera() != null) {
            simulacion.setCartera(carteraRepository.findById(dto.getIdCartera())
                    .orElseThrow(() -> new EntityNotFoundException("Cartera no encontrada")));
        }

        simulacion.setFechaFinalizacion(dto.getFechaFinalizacion());
        simulacion.setFechaSimulacion(LocalDateTime.now());
        simulacion.setMontoInvertido(dto.getMontoInvertido());
        simulacion.setValorFinal(dto.getValorFinal());
        simulacion.setInteresBruto(dto.getInteresBruto());
        simulacion.setIsr(dto.getIsr());
        simulacion.setPlazoTexto(dto.getPlazoTexto());
        simulacion.setCompartidaAnalista(dto.getCompartidaAnalista());

        return simulacion;
    }

    @Override
    public Map<String, List<DetalleSimulacionDto>> agruparDetallesPorTipo(List<DetalleSimulacionDto> detalles) {
        return detalles.stream()
                .filter(d -> d.getTipoInstrumento() != null)
                .collect(Collectors.groupingBy(DetalleSimulacionDto::getTipoInstrumento));
    }

    @Override
    public Integer buscarIdUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(Usuario::getIdUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado por correo"));
    }

    @Override
    public Page<SimulacionDto> findCompartidasParaAnalista(Pageable pageable) {
        Page<Simulacion> simulaciones = simulacionRepository.findByCompartidaAnalistaTrue(pageable);
        List<SimulacionDto> dtoList = simulaciones.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, simulaciones.getTotalElements());
    }

}
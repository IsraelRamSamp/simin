package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.ReporteDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Cartera;
import mx.dgtic.unam.simin.entity.Reporte;
import mx.dgtic.unam.simin.entity.TipoReporte;
import mx.dgtic.unam.simin.repository.CarteraRepository;
import mx.dgtic.unam.simin.repository.ReporteRepository;
import mx.dgtic.unam.simin.repository.TipoReporteRepository;
import mx.dgtic.unam.simin.service.dto.ReporteDtoService;
import mx.dgtic.unam.simin.service.dto.SimulacionDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReporteDtoServiceImpl implements ReporteDtoService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private CarteraRepository carteraRepository;

    @Autowired
    private TipoReporteRepository tipoReporteRepository;

    @Autowired
    private SimulacionDtoService simulacionDtoService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ReporteDto save(ReporteDto dto) {
        log.info("Guardando nuevo reporte para simulación ID: {}", dto.getIdSimulacion());

        Reporte reporte = new Reporte();

        if (dto.getIdCartera() != null) {
            Cartera cartera = carteraRepository.findById(dto.getIdCartera())
                    .orElseThrow(() -> new IllegalArgumentException("Cartera no encontrada"));
            reporte.setCartera(cartera);
        }

        TipoReporte tipoReporte = tipoReporteRepository.findById(dto.getIdTipoReporte())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de reporte no encontrado"));
        reporte.setTipoReporte(tipoReporte);

        reporte.setIdSimulacion(dto.getIdSimulacion());
        reporte.setNombreUsuario(dto.getNombreUsuario());
        reporte.setCorreoUsuario(dto.getCorreoUsuario());
        reporte.setTipoOrigen(dto.getTipoOrigen());

        Reporte saved = reporteRepository.save(reporte);

        log.info("Reporte guardado exitosamente con ID: {}", saved.getIdReporte());
        return convertirADto(saved);
    }

    @Override
    public List<ReporteDto> findByCarteraId(Integer idCartera) {
        log.info("Buscando reportes por cartera ID: {}", idCartera);
        return reporteRepository.findByCartera_IdCartera(idCartera).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ReporteDto convertirADto(Reporte reporte) {
        ReporteDto dto = new ReporteDto();
        dto.setIdReporte(reporte.getIdReporte());

        if (reporte.getCartera() != null) {
            dto.setIdCartera(reporte.getCartera().getIdCartera());
            dto.setNombreCartera(reporte.getCartera().getNombreCartera());

            if (reporte.getCartera().getUsuario() != null) {
                dto.setCorreoUsuario(reporte.getCartera().getUsuario().getCorreo());
                dto.setNombreUsuario(reporte.getCartera().getUsuario().getNombre());
            }
        } else {
            dto.setCorreoUsuario(reporte.getCorreoUsuario());
            dto.setNombreUsuario(reporte.getNombreUsuario());
        }

        dto.setIdSimulacion(reporte.getIdSimulacion());
        dto.setTipoOrigen(reporte.getTipoOrigen());
        dto.setFechaReporte(reporte.getFechaReporte());

        if (reporte.getTipoReporte() != null) {
            dto.setIdTipoReporte(reporte.getTipoReporte().getIdTipoReporte());
            dto.setTipoReporteDescripcion(reporte.getTipoReporte().getDescripcion());
        }

        return dto;
    }

    @Override
    public List<ReporteDto> findAll() {
        log.info("Listando todos los reportes ordenados por fecha");
        List<Reporte> entidades = reporteRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaReporte"));
        return entidades.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReporteDto> findByFiltrosAdmin(LocalDateTime fechaInicio, LocalDateTime fechaFin, String correo, String nombre, Pageable pageable) {
        log.info("Filtrando reportes para administrador con parámetros: fechaInicio={}, fechaFin={}, correo={}, nombre={}",
                fechaInicio, fechaFin, correo, nombre);

        Specification<Reporte> spec = Specification.where(null);

        if (fechaInicio != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaReporte"), fechaInicio));
        }
        if (fechaFin != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaReporte"), fechaFin));
        }
        if (correo != null && !correo.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("cartera").get("usuario").get("correo")), "%" + correo.toLowerCase() + "%"));
        }
        if (nombre != null && !nombre.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("cartera").get("usuario").get("nombre")), "%" + nombre.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("cartera").get("usuario").get("apellidoPaterno")), "%" + nombre.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("cartera").get("usuario").get("apellidoMaterno")), "%" + nombre.toLowerCase() + "%")
            ));
        }

        Page<Reporte> reportes = reporteRepository.findAll(spec, pageable);
        return reportes.map(this::convertirADto);
    }

    @Override
    public Page<ReporteDto> findByCorreoUsuario(String correo, Pageable pageable) {
        log.info("Buscando reportes por correo de usuario: {}", correo);
        return reporteRepository.findByCorreoUsuario(correo, pageable)
                .map(this::convertirADto);
    }

    @Override
    public Page<ReporteDto> findReportesParaAnalista(String correo, Pageable pageable) {
        log.info("Obteniendo reportes visibles para analista con correo: {}", correo);

        Page<Reporte> todos = reporteRepository.findAll(pageable);

        List<ReporteDto> visibles = todos.stream()
                .filter(r -> {
                    boolean esPropio = correo.equalsIgnoreCase(r.getCorreoUsuario());
                    boolean esCompartido = false;
                    try {
                        SimulacionDto simulacion = simulacionDtoService.findById(r.getIdSimulacion());
                        esCompartido = Boolean.TRUE.equals(simulacion.getCompartidaAnalista());
                    } catch (Exception e) {
                        log.warn("No se pudo verificar compartición de simulación ID: {}", r.getIdSimulacion());
                    }
                    return esPropio || esCompartido;
                })
                .map(this::convertirADto)
                .toList();

        return new PageImpl<>(visibles, pageable, visibles.size());
    }

    @Override
    @Transactional
    public void deleteByIdSimulacion(Integer idSimulacion) {
        log.info("Eliminando reportes por simulación ID: {}", idSimulacion);
        reporteRepository.deleteAllByIdSimulacion(idSimulacion);
    }

    @Override
    @Transactional
    public void deleteByIdCartera(Integer idCartera) {
        log.info("Eliminando reportes por cartera ID: {}", idCartera);
        reporteRepository.deleteAllByCartera_IdCartera(idCartera);
    }

    @Override
    public boolean existePorSimulacionId(Integer idSimulacion) {
        boolean existe = reporteRepository.existsByIdSimulacion(idSimulacion);
        log.info("¿Existe reporte para simulación ID {}? {}", idSimulacion, existe);
        return existe;
    }

    @Override
    public void deleteById(Integer idReporte) {
        log.info("Eliminando reporte ID: {}", idReporte);
        reporteRepository.deleteById(idReporte);
    }

    @Override
    public ReporteDto findById(Integer id) {
        log.info("Buscando reporte por ID: {}", id);
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado"));
        return convertirADto(reporte);
    }

    @Override
    public Optional<ReporteDto> findBySimulacionId(Integer idSimulacion) {
        log.info("Buscando reporte por simulación ID: {}", idSimulacion);
        return reporteRepository.findByIdSimulacion(idSimulacion)
                .map(reporte -> modelMapper.map(reporte, ReporteDto.class));
    }

}
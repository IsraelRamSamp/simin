package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.CarteraDto;
import mx.dgtic.unam.simin.entity.Cartera;
import mx.dgtic.unam.simin.entity.PerfilRiesgo;
import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.repository.CarteraRepository;
import mx.dgtic.unam.simin.repository.PerfilRiesgoRepository;
import mx.dgtic.unam.simin.repository.UsuarioRepository;
import mx.dgtic.unam.simin.service.dto.CarteraDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarteraDtoServiceImpl implements CarteraDtoService {

    @Autowired
    private CarteraRepository carteraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRiesgoRepository perfilRiesgoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<CarteraDto> findByFilter(String keyword, Integer perfilId, Pageable pageable) {
        log.info("Buscando carteras con filtro - keyword: {}, perfilId: {}", keyword, perfilId);
        Page<Cartera> carteras;

        if (keyword != null && !keyword.isBlank() && perfilId != null) {
            carteras = carteraRepository.findByNombreCarteraContainingIgnoreCaseAndPerfilRiesgo_IdPerfilRiesgo(keyword, perfilId, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            carteras = carteraRepository.findByNombreCarteraContainingIgnoreCase(keyword, pageable);
        } else if (perfilId != null) {
            carteras = carteraRepository.findByPerfilRiesgo_IdPerfilRiesgo(perfilId, pageable);
        } else {
            carteras = carteraRepository.findAll(pageable);
        }

        List<CarteraDto> dtoList = carteras.stream()
                .map(cartera -> modelMapper.map(cartera, CarteraDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, carteras.getTotalElements());
    }

    @Override
    public CarteraDto findById(Integer id) {
        log.info("Buscando cartera por ID: {}", id);
        Cartera cartera = carteraRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cartera no encontrada con ID: {}", id);
                    return new EntityNotFoundException("Cartera no encontrada");
                });

        CarteraDto dto = modelMapper.map(cartera, CarteraDto.class);
        dto.setNombreUsuario(cartera.getUsuario().getNombre());
        dto.setCorreoUsuario(cartera.getUsuario().getCorreo());
        return dto;
    }

    @Override
    public CarteraDto save(CarteraDto carteraDto) {
        log.info("Guardando nueva cartera para usuario ID: {}", carteraDto.getIdUsuario());
        Usuario usuario = usuarioRepository.findById(carteraDto.getIdUsuario())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", carteraDto.getIdUsuario());
                    return new EntityNotFoundException("Usuario no encontrado");
                });

        PerfilRiesgo perfil = null;
        if (carteraDto.getIdPerfilRiesgo() != null) {
            perfil = perfilRiesgoRepository.findById(carteraDto.getIdPerfilRiesgo())
                    .orElseThrow(() -> {
                        log.error("Perfil de riesgo no encontrado con ID: {}", carteraDto.getIdPerfilRiesgo());
                        return new EntityNotFoundException("Perfil de riesgo no encontrado");
                    });
        }

        Cartera cartera = new Cartera();
        cartera.setNombreCartera(carteraDto.getNombre());
        cartera.setUsuario(usuario);
        cartera.setPerfilRiesgo(perfil);
        cartera.setValorTotal(BigDecimal.ZERO);

        Cartera saved = carteraRepository.save(cartera);
        log.info("Cartera guardada correctamente con ID: {}", saved.getIdCartera());

        CarteraDto dto = modelMapper.map(saved, CarteraDto.class);
        dto.setNombreUsuario(saved.getUsuario().getNombre());
        dto.setCorreoUsuario(saved.getUsuario().getCorreo());
        return dto;
    }

    @Override
    public CarteraDto update(Integer id, CarteraDto carteraDto) {
        log.info("Actualizando cartera con ID: {}", id);
        Cartera cartera = carteraRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cartera no encontrada con ID: {}", id);
                    return new EntityNotFoundException("Cartera no encontrada");
                });

        Usuario usuario = usuarioRepository.findById(carteraDto.getIdUsuario())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", carteraDto.getIdUsuario());
                    return new EntityNotFoundException("Usuario no encontrado");
                });

        PerfilRiesgo perfil = null;
        if (carteraDto.getIdPerfilRiesgo() != null) {
            perfil = perfilRiesgoRepository.findById(carteraDto.getIdPerfilRiesgo())
                    .orElseThrow(() -> {
                        log.error("Perfil de riesgo no encontrado con ID: {}", carteraDto.getIdPerfilRiesgo());
                        return new EntityNotFoundException("Perfil de riesgo no encontrado");
                    });
        }

        cartera.setNombreCartera(carteraDto.getNombre());
        cartera.setUsuario(usuario);
        cartera.setPerfilRiesgo(perfil);

        Cartera updated = carteraRepository.save(cartera);
        log.info("Cartera actualizada correctamente con ID: {}", updated.getIdCartera());

        CarteraDto dto = modelMapper.map(updated, CarteraDto.class);
        dto.setNombreUsuario(updated.getUsuario().getNombre());
        dto.setCorreoUsuario(updated.getUsuario().getCorreo());
        return dto;
    }

    @Override
    public void delete(Integer id) {
        log.info("Eliminando cartera con ID: {}", id);
        carteraRepository.deleteById(id);
    }

    @Override
    public Page<CarteraDto> findByFilterAndCorreo(String keyword, Integer perfilId, String correo, Pageable pageable) {
        log.info("Buscando carteras por correo: {}, filtro: {}, perfilId: {}", correo, keyword, perfilId);
        return carteraRepository.findByFilterAndCorreo(keyword, perfilId, correo, pageable)
                .map(cartera -> modelMapper.map(cartera, CarteraDto.class));
    }

    @Override
    public List<CarteraDto> findByUsuarioId(Integer idUsuario) {
        log.info("Obteniendo carteras del usuario ID: {}", idUsuario);
        return carteraRepository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(cartera -> {
                    CarteraDto dto = modelMapper.map(cartera, CarteraDto.class);
                    dto.setNombreUsuario(cartera.getUsuario().getNombre());
                    dto.setCorreoUsuario(cartera.getUsuario().getCorreo());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
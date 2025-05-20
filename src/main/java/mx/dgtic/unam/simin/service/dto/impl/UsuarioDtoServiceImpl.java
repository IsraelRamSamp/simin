package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.UsuarioDto;
import mx.dgtic.unam.simin.dto.UsuarioRegistroDto;
import mx.dgtic.unam.simin.entity.*;
import mx.dgtic.unam.simin.exception.UsuarioNotFoundException;
import mx.dgtic.unam.simin.repository.*;
import mx.dgtic.unam.simin.service.dto.UsuarioDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioDtoServiceImpl implements UsuarioDtoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private CarteraRepository carteraRepository;

    @Autowired
    private SimulacionRepository simulacionRepository;

    @Autowired
    private DetalleSimulacionRepository detalleSimulacionRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private InstrumentoCarteraRepository instrumentoCarteraRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toEntity(UsuarioDto dto) {
        return modelMapper.map(dto, Usuario.class);
    }

    public UsuarioDto toDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDto.class);
    }

    @Override
    public List<UsuarioDto> findAll() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDto findById(Integer id) {
        log.info("Buscando usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new UsuarioNotFoundException("Usuario no encontrado con ID: " + id);
                });
        return modelMapper.map(usuario, UsuarioDto.class);
    }

    @Override
    public UsuarioDto save(UsuarioDto usuarioDto) {
        log.info("Guardando nuevo usuario con correo: {}", usuarioDto.getCorreo());
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
        TipoUsuario tipo = tipoUsuarioRepository.findById(usuarioDto.getIdTipoUsuario())
                .orElseThrow(() -> {
                    log.error("Tipo de usuario no válido: {}", usuarioDto.getIdTipoUsuario());
                    return new UsuarioNotFoundException("Tipo de usuario no válido");
                });
        usuario.setTipoUsuario(tipo);
        Usuario saved = usuarioRepository.save(usuario);
        log.info("Usuario guardado exitosamente con ID: {}", saved.getIdUsuario());
        return modelMapper.map(saved, UsuarioDto.class);
    }

    @Override
    public UsuarioDto update(Integer id, UsuarioDto usuarioDto) {
        log.info("Actualizando usuario con ID: {}", id);
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new UsuarioNotFoundException("Usuario no encontrado con ID: " + id);
                });

        modelMapper.map(usuarioDto, usuarioExistente);
        TipoUsuario tipo = tipoUsuarioRepository.findById(usuarioDto.getIdTipoUsuario())
                .orElseThrow(() -> {
                    log.error("Tipo de usuario no válido: {}", usuarioDto.getIdTipoUsuario());
                    return new UsuarioNotFoundException("Tipo de usuario no válido");
                });
        usuarioExistente.setTipoUsuario(tipo);

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario actualizado correctamente: {}", actualizado.getIdUsuario());
        return modelMapper.map(actualizado, UsuarioDto.class);
    }

    @Override
    @Transactional
    public UsuarioDto delete(Integer id) {
        log.info("Eliminando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new UsuarioNotFoundException("Usuario no encontrado con ID: " + id);
                });

        List<Simulacion> simulaciones = simulacionRepository.findByUsuario_IdUsuario(id);
        for (Simulacion simulacion : simulaciones) {
            detalleSimulacionRepository.deleteAllBySimulacion_IdSimulacion(simulacion.getIdSimulacion());
            reporteRepository.deleteAllByIdSimulacion(simulacion.getIdSimulacion());
        }
        simulacionRepository.deleteAll(simulaciones);

        List<Cartera> carteras = carteraRepository.findByUsuario_IdUsuario(id);
        for (Cartera cartera : carteras) {
            instrumentoCarteraRepository.deleteAllByCartera_IdCartera(cartera.getIdCartera());
            reporteRepository.deleteAllByCartera_IdCartera(cartera.getIdCartera());
        }
        carteraRepository.deleteAll(carteras);

        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado exitosamente con ID: {}", id);

        return modelMapper.map(usuario, UsuarioDto.class);
    }

    @Override
    public Integer findIdByCorreo(String correo) {
        log.info("Buscando ID de usuario por correo: {}", correo);
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con correo: {}", correo);
                    return new EntityNotFoundException("Usuario no encontrado: " + correo);
                })
                .getIdUsuario();
    }

    @Override
    public boolean existeCorreo(String correo) {
        log.info("Verificando existencia del correo: {}", correo);
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    @Transactional
    public void registrarNuevoUsuario(UsuarioRegistroDto dto) {
        log.info("Registrando nuevo usuario: {}", dto.getCorreo());
        Usuario nuevo = new Usuario();

        nuevo.setNombre(dto.getNombre());
        nuevo.setApellidoPaterno(dto.getApellidoPaterno());
        nuevo.setApellidoMaterno(dto.getApellidoMaterno());
        nuevo.setCorreo(dto.getCorreo());
        nuevo.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        nuevo.setFechaRegistro(LocalDateTime.now());

        Rol rolInversionista = rolRepository.findByNombre("INVERSIONISTA")
                .orElseThrow(() -> {
                    log.error("Rol INVERSIONISTA no encontrado");
                    return new RuntimeException("Rol INVERSIONISTA no encontrado");
                });
        nuevo.setRol(rolInversionista);

        TipoUsuario tipo = tipoUsuarioRepository.findById(2)
                .orElseThrow(() -> {
                    log.error("Tipo de usuario por defecto no encontrado");
                    return new RuntimeException("Tipo de usuario no encontrado");
                });
        nuevo.setTipoUsuario(tipo);

        usuarioRepository.save(nuevo);
        log.info("Usuario registrado exitosamente: {}", nuevo.getCorreo());
    }
}
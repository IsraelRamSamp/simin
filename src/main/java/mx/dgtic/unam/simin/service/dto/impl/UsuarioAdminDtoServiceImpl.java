package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.persistence.EntityNotFoundException;
import mx.dgtic.unam.simin.dto.UsuarioAdminDto;
import mx.dgtic.unam.simin.entity.Rol;
import mx.dgtic.unam.simin.entity.TipoUsuario;
import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.repository.RolRepository;
import mx.dgtic.unam.simin.repository.TipoUsuarioRepository;
import mx.dgtic.unam.simin.repository.UsuarioRepository;
import mx.dgtic.unam.simin.service.dto.UsuarioAdminDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioAdminDtoServiceImpl implements UsuarioAdminDtoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UsuarioAdminDto> findByFilter(String keyword, Integer tipoId, Pageable pageable) {
        Page<Usuario> usuarios;

        if (keyword != null && !keyword.isEmpty() && tipoId != null) {
            usuarios = usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCaseAndTipoUsuarioIdTipoUsuario(
                    keyword, keyword, tipoId, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            usuarios = usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    keyword, keyword, pageable);
        } else if (tipoId != null) {
            usuarios = usuarioRepository.findByTipoUsuarioIdTipoUsuario(tipoId, pageable);
        } else {
            usuarios = usuarioRepository.findAll(pageable);
        }

        List<UsuarioAdminDto> dtoList = usuarios.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, usuarios.getTotalElements());
    }

    @Override
    public UsuarioAdminDto findById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return mapToDto(usuario);
    }

    @Override
    public UsuarioAdminDto save(UsuarioAdminDto usuarioAdminDto) {
        Usuario usuario = new Usuario();

        usuario.setNombre(usuarioAdminDto.getNombre());
        usuario.setApellidoPaterno(usuarioAdminDto.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioAdminDto.getApellidoMaterno());
        usuario.setCorreo(usuarioAdminDto.getCorreo());

        if (usuarioAdminDto.getContrasena() != null && !usuarioAdminDto.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioAdminDto.getContrasena()));
        }

        TipoUsuario tipo = tipoUsuarioRepository.findById(usuarioAdminDto.getIdTipoUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado"));
        usuario.setTipoUsuario(tipo);

        Rol rol = rolRepository.findById(usuarioAdminDto.getIdRol())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
        usuario.setRol(rol);

        Usuario saved = usuarioRepository.save(usuario);
        return mapToDto(saved);
    }

    @Override
    public UsuarioAdminDto update(Integer id, UsuarioAdminDto usuarioAdminDto) {
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        existing.setNombre(usuarioAdminDto.getNombre());
        existing.setApellidoPaterno(usuarioAdminDto.getApellidoPaterno());
        existing.setApellidoMaterno(usuarioAdminDto.getApellidoMaterno());
        existing.setCorreo(usuarioAdminDto.getCorreo());

        if (usuarioAdminDto.getContrasena() != null && !usuarioAdminDto.getContrasena().isBlank()) {
            existing.setContrasena(passwordEncoder.encode(usuarioAdminDto.getContrasena()));
        }

        if (usuarioAdminDto.getIdTipoUsuario() != null) {
            TipoUsuario tipo = tipoUsuarioRepository.findById(usuarioAdminDto.getIdTipoUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de usuario no encontrado"));
            existing.setTipoUsuario(tipo);
        }

        if (usuarioAdminDto.getIdRol() != null) {
            Rol rol = rolRepository.findById(usuarioAdminDto.getIdRol())
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
            existing.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(existing);
        return mapToDto(actualizado);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioAdminDto mapToDto(Usuario usuario) {
        UsuarioAdminDto dto = modelMapper.map(usuario, UsuarioAdminDto.class);
        dto.setIdTipoUsuario(usuario.getTipoUsuario().getIdTipoUsuario());
        dto.setTipoUsuarioDescripcion(usuario.getTipoUsuario().getDescripcion());
        dto.setIdRol(usuario.getRol().getIdRol());
        dto.setRolDescripcion(usuario.getRol().getNombre());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        return dto;
    }
}
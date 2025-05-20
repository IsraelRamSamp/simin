package mx.dgtic.unam.simin.service.dto.impl;

import jakarta.transaction.Transactional;
import mx.dgtic.unam.simin.dto.UsuarioPerfilDto;
import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.repository.UsuarioRepository;
import mx.dgtic.unam.simin.service.dto.UsuarioPerfilDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPerfilDtoServiceImpl implements UsuarioPerfilDtoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioPerfilDto obtenerPerfilPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));

        UsuarioPerfilDto dto = modelMapper.map(usuario, UsuarioPerfilDto.class);
        dto.setTipoUsuarioDescripcion(usuario.getTipoUsuario().getDescripcion());

        if (dto.getAvatarUrl() == null || dto.getAvatarUrl().isBlank()) {
            dto.setAvatarUrl("/image/default-avatar.png");
        }

        return dto;
    }

    @Override
    @Transactional
    public void actualizarPerfil(String correo, UsuarioPerfilDto dto) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));

        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());

        if (dto.getNuevaContrasena() != null && !dto.getNuevaContrasena().isBlank()) {
            if (dto.getNuevaContrasena().length() < 6) {
                throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres.");
            }
            usuario.setContrasena(passwordEncoder.encode(dto.getNuevaContrasena()));
        }

        usuarioRepository.save(usuario);
    }
}
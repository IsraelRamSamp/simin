package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.UsuarioPerfilDto;

public interface UsuarioPerfilDtoService {
    UsuarioPerfilDto obtenerPerfilPorCorreo(String correo);
    void actualizarPerfil(String correo, UsuarioPerfilDto usuarioPerfilDto);
}

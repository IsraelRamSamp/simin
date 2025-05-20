package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.UsuarioDto;
import mx.dgtic.unam.simin.dto.UsuarioRegistroDto;

import java.util.List;

public interface UsuarioDtoService {
    List<UsuarioDto> findAll();
    UsuarioDto findById(Integer id);
    UsuarioDto save(UsuarioDto usuarioDto);
    UsuarioDto update(Integer id, UsuarioDto usuarioDto);
    UsuarioDto delete(Integer id);
    Integer findIdByCorreo(String correo);
    void registrarNuevoUsuario(UsuarioRegistroDto dto);
    boolean existeCorreo(String correo);
}


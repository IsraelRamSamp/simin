package mx.dgtic.unam.simin.service.dto;

import mx.dgtic.unam.simin.dto.UsuarioAdminDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioAdminDtoService {

    Page<UsuarioAdminDto> findByFilter(String keyword, Integer tipoId, Pageable pageable);

    UsuarioAdminDto findById(Integer id);

    UsuarioAdminDto save(UsuarioAdminDto usuarioAdminDto);

    UsuarioAdminDto update(Integer id, UsuarioAdminDto usuarioAdminDto);

    void delete(Integer id);
}

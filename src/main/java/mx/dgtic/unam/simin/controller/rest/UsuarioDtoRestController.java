package mx.dgtic.unam.simin.controller.rest;

import jakarta.validation.Valid;
import mx.dgtic.unam.simin.dto.UsuarioDto;
import mx.dgtic.unam.simin.service.dto.UsuarioDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioDtoRestController {

    @Autowired
    private UsuarioDtoService usuarioDtoService;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioDtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioDtoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioDtoService.save(usuarioDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(@PathVariable Integer id,
                                                    @Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioDtoService.update(id, usuarioDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDto> deleteUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioDtoService.delete(id));
    }
}

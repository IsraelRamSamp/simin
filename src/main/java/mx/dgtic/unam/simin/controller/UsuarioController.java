package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.CarteraDto;
import mx.dgtic.unam.simin.dto.UsuarioAdminDto;
import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.repository.RolRepository;
import mx.dgtic.unam.simin.repository.TipoUsuarioRepository;
import mx.dgtic.unam.simin.repository.UsuarioRepository;
import mx.dgtic.unam.simin.service.TipoUsuarioService;
import mx.dgtic.unam.simin.service.dto.CarteraDtoService;
import mx.dgtic.unam.simin.service.dto.UsuarioAdminDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioAdminDtoService usuarioAdminDtoService;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteraDtoService carteraDtoService;

    @GetMapping("/list")
    public String listUsuarios(Model model,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "tipo", required = false) Integer tipoId,
                               @RequestParam(name = "page", defaultValue = "0") int page) {

        log.info("Listando usuarios - keyword: {}, tipoId: {}, página: {}", keyword, tipoId, page);

        Pageable pageable = PageRequest.of(page, 10);
        Page<UsuarioAdminDto> usuarios = usuarioAdminDtoService.findByFilter(keyword, tipoId, pageable);

        model.addAttribute("usuarios", usuarios.getContent());
        model.addAttribute("totalPages", usuarios.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tipoSeleccionado", tipoId);
        model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());

        return "usuarios/list";
    }

    @GetMapping("/create")
    public String createUsuarioForm(Model model) {
        model.addAttribute("usuarioAdminDto", new UsuarioAdminDto());
        model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
        model.addAttribute("roles", rolRepository.findAll());
        log.info("Mostrando formulario para crear nuevo usuario.");
        return "usuarios/create";
    }

    @PostMapping("/create")
    public String saveUsuario(@Valid @ModelAttribute("usuarioAdminDto") UsuarioAdminDto usuarioAdminDto,
                              BindingResult result, Model model, RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            log.warn("Errores de validación al crear usuario: {}", result.getAllErrors());
            model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "usuarios/create";
        }

        usuarioAdminDtoService.save(usuarioAdminDto);
        redirectAttrs.addFlashAttribute("mensaje", "Usuario creado exitosamente.");
        log.info("Usuario creado exitosamente: {}", usuarioAdminDto.getCorreo());
        return "redirect:/usuarios/list";
    }

    @GetMapping("/edit/{id}")
    public String editUsuarioForm(@PathVariable Integer id, Model model) {
        UsuarioAdminDto usuario = usuarioAdminDtoService.findById(id);
        model.addAttribute("usuarioAdminDto", usuario);
        model.addAttribute("tiposUsuario", tipoUsuarioService.findAll());
        model.addAttribute("roles", rolRepository.findAll());
        log.info("Mostrando formulario para editar usuario con ID: {}", id);
        return "usuarios/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUsuario(@PathVariable Integer id,
                                @Valid @ModelAttribute("usuarioAdminDto") UsuarioAdminDto usuarioAdminDto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            log.warn("Errores al editar usuario con ID {}: {}", id, result.getAllErrors());
            model.addAttribute("tiposUsuario", tipoUsuarioRepository.findAll());
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "usuarios/edit";
        }

        try {
            usuarioAdminDtoService.update(id, usuarioAdminDto);
            redirectAttrs.addFlashAttribute("mensaje", "Usuario actualizado exitosamente.");
            log.info("Usuario actualizado exitosamente con ID: {}", id);
        } catch (Exception e) {
            log.error("Error al actualizar usuario con ID {}: {}", id, e.getMessage());
            model.addAttribute("tiposUsuario", tipoUsuarioRepository.findAll());
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            return "usuarios/edit";
        }

        return "redirect:/usuarios/list";
    }

    @GetMapping("/view/{id}")
    public String viewUsuario(@PathVariable Integer id, Model model) {
        UsuarioAdminDto usuarioAdminDto = usuarioAdminDtoService.findById(id);
        if (usuarioAdminDto == null) {
            log.warn("Intento de ver usuario no existente con ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        model.addAttribute("usuarioAdminDto", usuarioAdminDto);

        List<CarteraDto> carteras = carteraDtoService.findByUsuarioId(id);
        model.addAttribute("carterasUsuario", carteras);

        log.info("Mostrando detalles del usuario con ID: {}", id);
        return "usuarios/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteUsuario(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        usuarioAdminDtoService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        log.info("Usuario eliminado con ID: {}", id);
        return "redirect:/usuarios/list";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public List<Map<String, Object>> buscarPorCorreo(@RequestParam("correo") String correo) {
        List<Usuario> usuarios = usuarioRepository.findByCorreoContainingIgnoreCase(correo);
        log.info("Búsqueda de usuarios por correo: '{}', encontrados: {}", correo, usuarios.size());

        return usuarios.stream()
                .map(usuario -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", usuario.getIdUsuario());
                    map.put("correo", usuario.getCorreo());
                    map.put("nombre", usuario.getNombre() + " " + usuario.getApellidoPaterno());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
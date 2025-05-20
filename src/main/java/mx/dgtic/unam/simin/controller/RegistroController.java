package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.UsuarioRegistroDto;
import mx.dgtic.unam.simin.repository.RolRepository;
import mx.dgtic.unam.simin.repository.TipoUsuarioRepository;
import mx.dgtic.unam.simin.service.dto.UsuarioDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/public")
public class RegistroController {

    @Autowired
    private UsuarioDtoService usuarioDtoService;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        log.info("Mostrando formulario de registro público.");
        model.addAttribute("usuarioRegistroDto", new UsuarioRegistroDto());
        return "public/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioRegistroDto") UsuarioRegistroDto dto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttrs,
                                   Model model) {
        log.info("Intentando registrar nuevo usuario con correo: {}", dto.getCorreo());

        if (result.hasErrors()) {
            log.warn("Errores de validación encontrados en el formulario de registro.");
            return "public/registro";
        }

        if (!dto.getContrasena().equals(dto.getConfirmarContrasena())) {
            log.warn("Las contraseñas no coinciden para el correo: {}", dto.getCorreo());
            result.rejectValue("confirmarContrasena", "error.usuarioRegistroDto", "Las contraseñas no coinciden.");
            return "public/registro";
        }

        if (usuarioDtoService.existeCorreo(dto.getCorreo())) {
            log.warn("Correo ya registrado: {}", dto.getCorreo());
            result.rejectValue("correo", "error.usuarioRegistroDto", "Este correo ya está registrado.");
            return "public/registro";
        }

        try {
            usuarioDtoService.registrarNuevoUsuario(dto);
            log.info("Usuario registrado exitosamente con correo: {}", dto.getCorreo());
            redirectAttrs.addFlashAttribute("mensaje", "Registro exitoso.");
            return "redirect:/home";
        } catch (Exception ex) {
            log.error("Error durante el registro del usuario: {}", ex.getMessage(), ex);
            model.addAttribute("error", "Ocurrió un error durante el registro: " + ex.getMessage());
            return "public/registro";
        }
    }
}
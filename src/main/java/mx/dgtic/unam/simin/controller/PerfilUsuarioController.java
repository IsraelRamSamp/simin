package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import mx.dgtic.unam.simin.dto.UsuarioPerfilDto;
import mx.dgtic.unam.simin.service.dto.UsuarioPerfilDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/usuario/perfil")
public class PerfilUsuarioController {

    @Autowired
    private UsuarioPerfilDtoService usuarioPerfilDtoService;

    @GetMapping
    public String verPerfil(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioPerfilDto usuario = usuarioPerfilDtoService.obtenerPerfilPorCorreo(correo);

        LocalDateTime fecha = LocalDateTime.parse(usuario.getFechaRegistro());
        String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        model.addAttribute("fechaFormateada", fechaFormateada);
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    @GetMapping("/editar")
    public String editarPerfil(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioPerfilDto usuario = usuarioPerfilDtoService.obtenerPerfilPorCorreo(correo);
        model.addAttribute("usuario", usuario);
        return "usuario/editar-perfil";
    }

    @PostMapping("/editar")
    public String actualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioPerfilDto usuario,
                                   BindingResult result,
                                   Authentication authentication,
                                   Model model) {
        if (result.hasErrors()) {
            return "usuario/editar-perfil";
        }

        String correo = authentication.getName();
        usuarioPerfilDtoService.actualizarPerfil(correo, usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("successMessage", "Perfil actualizado correctamente");
        return "redirect:/usuario/perfil";
    }
}
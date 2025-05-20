package mx.dgtic.unam.simin.controller;

import jakarta.persistence.EntityNotFoundException;
import mx.dgtic.unam.simin.entity.Usuario;
import mx.dgtic.unam.simin.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Inicio - SIMIN");

        if (authentication != null && authentication.isAuthenticated()) {
            String correo = authentication.getName();
            Usuario usuario = usuarioService.findByCorreo(correo)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidoPaterno();
            String tipoUsuario = usuario.getTipoUsuario().getDescripcion();

            model.addAttribute("nombreUsuario", nombreCompleto);
            model.addAttribute("tipoUsuario", tipoUsuario);
        }

        return "public/home";
    }

    @GetMapping("/que-es-simin")
    public String queEsSimin(Model model) {
        model.addAttribute("pageTitle", "¿Qué es SIMIN?");
        return "public/que-es-simin";
    }

    @GetMapping("/contenido-educativo")
    public String contenidoEducativo(Model model) {
        model.addAttribute("pageTitle", "Contenido Educativo");
        return "public/contenido-educativo";
    }

    @GetMapping("/acerca-de")
    public String acercaDe(Model model) {
        model.addAttribute("pageTitle", "Acerca de");
        return "public/acerca-de";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("pageTitle", "Contacto");
        return "public/contacto";
    }

}
package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.LoginDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getAuthorities().isEmpty()
                && !auth.getName().equals("anonymousUser")) {
            log.info("Usuario '{}' ya autenticado. Redirigiendo a /home.", auth.getName());
            return "redirect:/home";
        }

        log.info("Mostrando formulario de login.");
        model.addAttribute("loginDto", new LoginDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginDto") @Valid LoginDto loginDto,
                               BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Errores en los campos del formulario de login.");
            return "auth/login";
        }

        log.info("Login procesado correctamente (autenticación manejada por Spring Security).");
        return "redirect:/home";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        log.warn("Fallo en el intento de login. Mostrando mensaje de error.");
        model.addAttribute("loginDto", new LoginDto());
        model.addAttribute("loginError", true);
        return "auth/login";
    }
}
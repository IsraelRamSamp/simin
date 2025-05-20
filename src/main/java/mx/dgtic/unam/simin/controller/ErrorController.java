package mx.dgtic.unam.simin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String error403(Model model) {
        model.addAttribute("pageTitle", "Acceso Denegado");
        return "error/403";
    }
}

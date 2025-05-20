package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import mx.dgtic.unam.simin.dto.InstrumentoCarteraDto;
import mx.dgtic.unam.simin.repository.InstrumentoRepository;
import mx.dgtic.unam.simin.service.dto.InstrumentoCarteraDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instrumentos-cartera")
public class InstrumentoCarteraController {

    @Autowired
    private InstrumentoCarteraDtoService instrumentoCarteraDtoService;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        InstrumentoCarteraDto dto = instrumentoCarteraDtoService.findById(id);
        model.addAttribute("instrumentoCarteraDto", dto);
        model.addAttribute("instrumentos", instrumentoRepository.findAll());

        instrumentoRepository.findById(dto.getIdInstrumento()).ifPresent(instrumento ->
                model.addAttribute("instrumentoSeleccionado", instrumento)
        );

        return "instrumentos-cartera/edit";
    }

    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute("instrumentoCarteraDto") InstrumentoCarteraDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttrs,
                         Model model) {
        // Validación normal por anotaciones
        if (result.hasErrors()) {
            model.addAttribute("instrumentos", instrumentoRepository.findAll());
            return "instrumentos-cartera/edit";
        }

        // Validación especial para BONDDIA
        String tipo = dto.getTipoInstrumentoDescripcion();
        if ("BONDDIA".equalsIgnoreCase(tipo)) {
            if (dto.getPlazoDiasBonddia() == null || dto.getPlazoDiasBonddia() <= 0) {
                result.rejectValue("plazoDiasBonddia", "error.plazoDiasBonddia",
                        "Debe especificar un plazo en días válido para BONDDIA.");
                model.addAttribute("instrumentos", instrumentoRepository.findAll());
                return "instrumentos-cartera/edit";
            }
        }

        instrumentoCarteraDtoService.update(dto.getIdInstrumentosCartera(), dto);
        redirectAttrs.addFlashAttribute("mensaje", "Instrumento actualizado correctamente.");
        return "redirect:/carteras/view/" + dto.getIdCartera();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        InstrumentoCarteraDto instrumento = instrumentoCarteraDtoService.findById(id);
        Integer idCartera = instrumento.getIdCartera();

        instrumentoCarteraDtoService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Instrumento eliminado correctamente.");
        return "redirect:/carteras/view/" + idCartera;
    }
}
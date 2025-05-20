package mx.dgtic.unam.simin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.InstrumentoDto;
import mx.dgtic.unam.simin.repository.TipoInstrumentoRepository;
import mx.dgtic.unam.simin.service.dto.InstrumentoDtoService;
import mx.dgtic.unam.simin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/instrumentos")
public class InstrumentoController {

    @Autowired
    private InstrumentoDtoService instrumentoDtoService;

    @Autowired
    private TipoInstrumentoRepository tipoInstrumentoRepository;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Integer tipo,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<InstrumentoDto> pageInstrumentos = instrumentoDtoService.findByFilter(keyword, tipo, pageable);

        log.info("Listando instrumentos (keyword: '{}', tipo: {}, página: {})", keyword, tipo, page);

        model.addAttribute("instrumentos", pageInstrumentos);
        model.addAttribute("page", pageInstrumentos);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tipo", tipo);
        model.addAttribute("tiposInstrumento", tipoInstrumentoRepository.findAll());

        return "instrumentos/list";
    }

    @GetMapping("/create")
    public String create(Model model, RedirectAttributes redirectAttrs) {
        if (!SecurityUtils.isAdmin()) {
            log.warn("Acceso denegado a crear instrumento. Usuario no autorizado.");
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para acceder a esta sección.");
            return "redirect:/instrumentos/list";
        }

        log.info("Mostrando formulario para crear nuevo instrumento.");
        model.addAttribute("instrumentoDto", new InstrumentoDto());
        model.addAttribute("tiposInstrumento", tipoInstrumentoRepository.findAll());
        return "instrumentos/create";
    }

    @PostMapping("/create")
    public String save(@Valid @ModelAttribute("instrumentoDto") InstrumentoDto instrumentoDto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttrs) {

        if (!SecurityUtils.isAdmin()) {
            log.warn("Acceso denegado a guardar instrumento. Usuario no autorizado.");
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para realizar esta acción.");
            return "redirect:/instrumentos/list";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al intentar crear instrumento.");
            model.addAttribute("tiposInstrumento", tipoInstrumentoRepository.findAll());
            return "instrumentos/create";
        }

        instrumentoDtoService.save(instrumentoDto);
        log.info("Instrumento '{}' creado exitosamente.", instrumentoDto.getNombre());
        redirectAttrs.addFlashAttribute("mensaje", "Instrumento creado correctamente.");
        return "redirect:/instrumentos/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
        if (!SecurityUtils.isAdmin()) {
            log.warn("Acceso denegado a editar instrumento. Usuario no autorizado.");
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para editar instrumentos.");
            return "redirect:/instrumentos/list";
        }

        try {
            InstrumentoDto instrumentoDto = instrumentoDtoService.findById(id);
            log.info("Mostrando formulario para editar instrumento con ID: {}", id);
            model.addAttribute("instrumentoDto", instrumentoDto);
            model.addAttribute("tiposInstrumento", tipoInstrumentoRepository.findAll());
            return "instrumentos/edit";
        } catch (Exception e) {
            log.error("Error al cargar instrumento con ID: {}. {}", id, e.getMessage());
            redirectAttrs.addFlashAttribute("error", "Instrumento no encontrado.");
            return "redirect:/instrumentos/list";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("instrumentoDto") InstrumentoDto instrumentoDto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {

        if (!SecurityUtils.isAdmin()) {
            log.warn("Acceso denegado a actualizar instrumento. Usuario no autorizado.");
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para editar instrumentos.");
            return "redirect:/instrumentos/list";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al actualizar instrumento ID: {}", id);
            model.addAttribute("tiposInstrumento", tipoInstrumentoRepository.findAll());
            return "instrumentos/edit";
        }

        instrumentoDtoService.update(id, instrumentoDto);
        log.info("Instrumento con ID {} actualizado correctamente.", id);
        redirectAttrs.addFlashAttribute("mensaje", "Instrumento actualizado correctamente.");
        return "redirect:/instrumentos/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        if (!SecurityUtils.isAdmin()) {
            log.warn("Acceso denegado a eliminar instrumento. Usuario no autorizado.");
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para eliminar instrumentos.");
            return "redirect:/instrumentos/list";
        }

        try {
            instrumentoDtoService.delete(id);
            log.info("Instrumento con ID {} eliminado correctamente.", id);
            redirectAttrs.addFlashAttribute("mensaje", "Instrumento eliminado correctamente.");
        } catch (Exception e) {
            log.error("Error al eliminar instrumento con ID {}: {}", id, e.getMessage());
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar el instrumento.");
        }

        return "redirect:/instrumentos/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            InstrumentoDto instrumento = instrumentoDtoService.findById(id);
            log.info("Mostrando vista detallada para instrumento con ID: {}", id);
            model.addAttribute("instrumento", instrumento);
            return "instrumentos/view";
        } catch (EntityNotFoundException ex) {
            log.warn("Instrumento no encontrado con ID: {}", id);
            redirectAttributes.addFlashAttribute("error", "Instrumento no encontrado.");
            return "redirect:/instrumentos/list";
        }
    }
}
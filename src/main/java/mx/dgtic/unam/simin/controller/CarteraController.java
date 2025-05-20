package mx.dgtic.unam.simin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.CarteraDto;
import mx.dgtic.unam.simin.dto.InstrumentoCarteraDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.repository.InstrumentoRepository;
import mx.dgtic.unam.simin.repository.PerfilRiesgoRepository;
import mx.dgtic.unam.simin.service.dto.CarteraDtoService;
import mx.dgtic.unam.simin.service.dto.InstrumentoCarteraDtoService;
import mx.dgtic.unam.simin.service.dto.UsuarioDtoService;
import mx.dgtic.unam.simin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/carteras")
public class CarteraController {

    @Autowired
    private CarteraDtoService carteraDtoService;

    @Autowired
    private UsuarioDtoService usuarioDtoService;

    @Autowired
    private PerfilRiesgoRepository perfilRiesgoRepository;

    @Autowired
    private InstrumentoCarteraDtoService instrumentoCarteraDtoService;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @GetMapping("/list")
    public String listCarteras(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "perfilId", required = false) Integer perfilId) {

        log.info("Accediendo a lista de carteras (keyword: '{}', perfilId: {})", keyword, perfilId);

        Pageable pageable = PageRequest.of(page, 10);
        Page<CarteraDto> carteras;
        boolean isAdmin = SecurityUtils.isAdmin();
        String correo = SecurityUtils.getCorreoUsuarioAutenticado();

        if (isAdmin) {
            carteras = carteraDtoService.findByFilter(keyword, perfilId, pageable);
        } else {
            carteras = carteraDtoService.findByFilterAndCorreo(keyword, perfilId, correo, pageable);
        }

        carteras.forEach(dto -> {
            boolean esPropia = dto.getCorreoUsuario() != null && dto.getCorreoUsuario().equals(correo);
            dto.setPuedeEditar(isAdmin || esPropia);
            dto.setPuedeEliminar(isAdmin || esPropia);
        });

        model.addAttribute("carteras", carteras.getContent());
        model.addAttribute("totalPages", carteras.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("usuarios", usuarioDtoService.findAll());
        model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
        model.addAttribute("perfilId", perfilId);
        model.addAttribute("esAdmin", isAdmin);
        return "carteras/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        if (!SecurityUtils.puedeCrearCarteras()) {
            log.warn("Intento no autorizado de crear cartera.");
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("carteraDto", new CarteraDto());

        if (!SecurityUtils.isAdmin()) {
            String correo = SecurityUtils.getCorreoUsuarioAutenticado();
            Integer idUsuario = usuarioDtoService.findIdByCorreo(correo);
            model.addAttribute("idUsuarioActual", idUsuario);
        }

        model.addAttribute("usuarios", usuarioDtoService.findAll());
        model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
        return "carteras/create";
    }

    @PostMapping("/create")
    public String saveCartera(@Valid @ModelAttribute("carteraDto") CarteraDto carteraDto,
                              BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.puedeCrearCarteras()) {
            log.warn("Intento no autorizado de guardar cartera.");
            return "redirect:/acceso-denegado";
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al crear cartera.");
            model.addAttribute("usuarios", usuarioDtoService.findAll());
            model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "carteras/create";
        }

        try {
            carteraDtoService.save(carteraDto);
            log.info("Cartera '{}' guardada correctamente.", carteraDto.getNombre());
            redirectAttributes.addFlashAttribute("mensaje", "Cartera creada exitosamente.");
            return "redirect:/carteras/list";
        } catch (Exception e) {
            log.error("Error al guardar cartera: {}", e.getMessage());
            model.addAttribute("usuarios", usuarioDtoService.findAll());
            model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
            model.addAttribute("error", "Hubo un error al guardar la cartera: " + e.getMessage());
            return "carteras/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        CarteraDto cartera = carteraDtoService.findById(id);
        if (!SecurityUtils.puedeEditarCartera(cartera)) {
            log.warn("Intento no autorizado de editar cartera con ID {}", id);
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("carteraDto", cartera);
        model.addAttribute("usuarios", usuarioDtoService.findAll());
        model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
        model.addAttribute("esAdmin", SecurityUtils.isAdmin());
        return "carteras/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCartera(@PathVariable Integer id,
                                @Valid @ModelAttribute("carteraDto") CarteraDto carteraDto,
                                BindingResult result,
                                Model model, RedirectAttributes redirectAttributes) {
        CarteraDto carteraExistente = carteraDtoService.findById(id);
        if (!SecurityUtils.puedeEditarCartera(carteraExistente)) {
            log.warn("Intento no autorizado de actualizar cartera con ID {}", id);
            return "redirect:/acceso-denegado";
        }

        if (result.hasErrors()) {
            log.warn("Errores al validar actualización de cartera con ID {}", id);
            model.addAttribute("usuarios", usuarioDtoService.findAll());
            model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "carteras/edit";
        }

        try {
            carteraDtoService.update(id, carteraDto);
            log.info("Cartera con ID {} actualizada correctamente.", id);
            redirectAttributes.addFlashAttribute("mensaje", "Cartera actualizada exitosamente.");
            return "redirect:/carteras/list";
        } catch (Exception e) {
            log.error("Error al actualizar cartera ID {}: {}", id, e.getMessage());
            model.addAttribute("usuarios", usuarioDtoService.findAll());
            model.addAttribute("perfilesRiesgo", perfilRiesgoRepository.findAll());
            model.addAttribute("error", "Error al actualizar la cartera.");
            return "carteras/edit";
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        CarteraDto cartera = carteraDtoService.findById(id);
        if (!SecurityUtils.puedeVerCartera(cartera)) {
            log.warn("Intento no autorizado de ver cartera con ID {}", id);
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("carteraDto", cartera);
        List<InstrumentoCarteraDto> instrumentos = instrumentoCarteraDtoService.findByCarteraId(id);
        model.addAttribute("instrumentosCartera", instrumentos);
        return "carteras/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        CarteraDto cartera = carteraDtoService.findById(id);
        if (!SecurityUtils.puedeEliminarCartera(cartera)) {
            log.warn("Intento no autorizado de eliminar cartera con ID {}", id);
            return "redirect:/acceso-denegado";
        }

        carteraDtoService.delete(id);
        log.info("Cartera con ID {} eliminada correctamente.", id);
        redirectAttrs.addFlashAttribute("mensaje", "Cartera eliminada correctamente.");
        return "redirect:/carteras/list";
    }

    @GetMapping("/add-instrumento/{idCartera}")
    public String showAddInstrumentoForm(@PathVariable Integer idCartera, Model model) {
        CarteraDto cartera = carteraDtoService.findById(idCartera);
        if (!SecurityUtils.puedeEditarCartera(cartera)) {
            log.warn("Intento no autorizado de agregar instrumento a cartera ID {}", idCartera);
            return "redirect:/acceso-denegado";
        }

        InstrumentoCarteraDto dto = new InstrumentoCarteraDto();
        dto.setIdCartera(idCartera);

        model.addAttribute("instrumentoCarteraDto", dto);
        model.addAttribute("instrumentos", instrumentoRepository.findAll());
        model.addAttribute("instrumentoSeleccionado", new Instrumento());
        return "carteras/add-instrumento";
    }

    @PostMapping("/add-instrumento")
    public String saveInstrumentoToCartera(@Valid @ModelAttribute("instrumentoCarteraDto") InstrumentoCarteraDto dto,
                                           BindingResult result,
                                           Model model,
                                           RedirectAttributes redirectAttrs) {

        CarteraDto cartera = carteraDtoService.findById(dto.getIdCartera());
        if (!SecurityUtils.puedeEditarCartera(cartera)) {
            log.warn("Intento no autorizado de guardar instrumento en cartera ID {}", dto.getIdCartera());
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("instrumentos", instrumentoRepository.findAll());

        if (dto.getIdInstrumento() != null) {
            instrumentoRepository.findById(dto.getIdInstrumento()).ifPresent(i ->
                    model.addAttribute("instrumentoSeleccionado", i));
        }

        if (result.hasErrors()) {
            log.warn("Errores de validación al agregar instrumento a cartera ID {}", dto.getIdCartera());
            return "carteras/add-instrumento";
        }

        String tipo = dto.getTipoInstrumentoDescripcion();
        if ("BONDDIA".equalsIgnoreCase(tipo)) {
            if (dto.getPlazoDiasBonddia() == null || dto.getPlazoDiasBonddia() <= 0) {
                result.rejectValue("plazoDiasBonddia", "error.plazoDiasBonddia",
                        "Debe especificar un plazo en días válido para BONDDIA.");
                return "carteras/add-instrumento";
            }
        }

        try {
            instrumentoCarteraDtoService.save(dto);
            log.info("Instrumento agregado exitosamente a cartera ID {}", dto.getIdCartera());
            redirectAttrs.addFlashAttribute("mensaje", "Instrumento agregado correctamente a la cartera.");
        } catch (Exception e) {
            log.error("Error al agregar instrumento a cartera ID {}: {}", dto.getIdCartera(), e.getMessage());
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carteras/view/" + dto.getIdCartera();
    }
}
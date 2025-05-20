package mx.dgtic.unam.simin.controller;

import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.service.dto.SimulacionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/simulaciones")
public class SimulacionController {

    @Autowired
    private SimulacionDtoService simulacionDtoService;

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private String getCorreoUsuarioAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listSimulaciones(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "keyword", required = false) String keyword,
                                   @RequestParam(name = "origen", required = false) String origen,
                                   Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<SimulacionDto> simulaciones = simulacionDtoService.findByFilter(keyword, origen, pageable);
        log.info("Listado de simulaciones para admin - Página: {}, Filtro: {}, Origen: {}", page, keyword, origen);

        model.addAttribute("simulaciones", simulaciones.getContent());
        model.addAttribute("totalPages", simulaciones.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("origen", origen);

        return "simulaciones/list";
    }

    @GetMapping("/view/{id}")
    public String viewSimulacion(@PathVariable Integer id,
                                 @RequestParam(name = "from", required = false) String from,
                                 Model model,
                                 RedirectAttributes redirectAttrs) {
        SimulacionDto dto = simulacionDtoService.findById(id);

        String correo = getCorreoUsuarioAutenticado();
        boolean esAdmin = hasRole("ROLE_ADMIN");
        boolean esAnalista = hasRole("ROLE_ANALISTA");
        boolean esPropietario = correo.equals(dto.getCorreoUsuario());
        dto.setEsPropietario(esPropietario);

        if (esAnalista && !dto.getCompartidaAnalista() && !esPropietario) {
            log.warn("Acceso denegado: analista intentó ver simulación no compartida ID: {}", id);
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para acceder a esta simulación.");
            return "redirect:/simulaciones/analista?tipo=" + (from != null ? from : "compartidas");
        }

        if (!esAdmin && !esPropietario && !dto.getCompartidaAnalista()) {
            log.warn("Acceso denegado: usuario sin permisos intentó ver simulación ID: {}", id);
            redirectAttrs.addFlashAttribute("error", "No tienes permiso para acceder a esta simulación.");
            return hasRole("ROLE_ANALISTA") ? "redirect:/simulaciones/analista?tipo=compartidas" :
                    hasRole("ROLE_INVERSIONISTA") ? "redirect:/simulaciones/inversionista" :
                            "redirect:/home";
        }

        model.addAttribute("simulacionDto", dto);

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            model.addAttribute("advertencia", "Esta simulación no contiene detalles por instrumento.");
        }

        Map<String, Boolean> tipoUsaTasaCupon = dto.getDetalles().stream()
                .map(DetalleSimulacionDto::getTipoInstrumento)
                .distinct()
                .collect(Collectors.toMap(
                        tipo -> tipo,
                        tipo -> List.of("BONO", "UDIBONOS", "BONDES").contains(tipo.toUpperCase())
                ));
        model.addAttribute("tipoUsaTasaCupon", tipoUsaTasaCupon);

        if (dto.getIdCartera() != null) {
            Map<String, List<DetalleSimulacionDto>> detallesPorTipo = simulacionDtoService.agruparDetallesPorTipo(dto.getDetalles());
            model.addAttribute("detallesPorTipo", detallesPorTipo);
        }

        log.info("Vista de simulación cargada correctamente ID: {}", id);
        return "simulaciones/view";
    }

    @GetMapping("/simular-instrumento/{idInstrumento}")
    public String simularDesdeInstrumento(@PathVariable Integer idInstrumento, Model model) {
        SimulacionDto dto = simulacionDtoService.buildFromInstrumento(idInstrumento);
        boolean esTasaCupon = List.of("BONO", "UDIBONOS", "BONDES")
                .contains(dto.getTipoInstrumentoDescripcion().toUpperCase());

        model.addAttribute("simulacionDto", dto);
        model.addAttribute("esTasaCupon", esTasaCupon);

        log.info("Preparando simulación desde instrumento ID: {}", idInstrumento);
        return "simulaciones/simular-instrumento";
    }

    @GetMapping("/simular-cartera/{idCartera}")
    public String simularDesdeCartera(@PathVariable Integer idCartera, Model model) {
        SimulacionDto dto = simulacionDtoService.buildFromCartera(idCartera);
        dto.setIdInstrumento(null);

        SimulacionDto resultado = simulacionDtoService.calcularSimulacion(dto);

        Map<String, List<DetalleSimulacionDto>> detallesPorTipo = simulacionDtoService.agruparDetallesPorTipo(resultado.getDetalles());

        Map<String, Boolean> tipoUsaTasaCupon = detallesPorTipo.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> List.of("BONO", "UDIBONOS", "BONDES").contains(e.getKey().toUpperCase())
                ));

        model.addAttribute("simulacionDto", resultado);
        model.addAttribute("detallesPorTipo", detallesPorTipo);
        model.addAttribute("tipoUsaTasaCupon", tipoUsaTasaCupon);

        log.info("Preparando simulación desde cartera ID: {}", idCartera);
        return "simulaciones/simular-cartera";
    }

    @PostMapping("/save")
    public String saveSimulacion(@ModelAttribute("simulacionDto") SimulacionDto dto,
                                 @RequestParam(name = "compartidaAnalista", defaultValue = "false") boolean compartidaAnalista,
                                 RedirectAttributes redirectAttrs) {
        try {
            if (dto.getIdUsuario() == null) {
                String correo = getCorreoUsuarioAutenticado();
                Integer idUsuario = simulacionDtoService.buscarIdUsuarioPorCorreo(correo);
                dto.setIdUsuario(idUsuario);
            }

            SimulacionDto resultado = simulacionDtoService.calcularSimulacion(dto);
            resultado.setIdUsuario(dto.getIdUsuario());
            resultado.setCompartidaAnalista(compartidaAnalista);

            simulacionDtoService.save(resultado);
            redirectAttrs.addFlashAttribute("mensaje", "Simulación guardada exitosamente.");
            log.info("Simulación guardada correctamente para usuario ID: {}", resultado.getIdUsuario());
        } catch (Exception ex) {
            log.error("Error al guardar simulación: {}", ex.getMessage(), ex);
            redirectAttrs.addFlashAttribute("error", "Ocurrió un error al guardar la simulación: " + ex.getMessage());
        }

        return hasRole("ROLE_INVERSIONISTA") ? "redirect:/simulaciones/inversionista" :
                hasRole("ROLE_ANALISTA") ? "redirect:/simulaciones/analista" :
                        "redirect:/simulaciones/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSimulacion(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            simulacionDtoService.delete(id);
            redirectAttrs.addFlashAttribute("mensaje", "Simulación eliminada exitosamente.");
            log.info("Simulación eliminada ID: {}", id);
        } catch (Exception ex) {
            log.error("Error al eliminar simulación ID {}: {}", id, ex.getMessage(), ex);
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar la simulación: " + ex.getMessage());
        }

        return "redirect:/simulaciones/list";
    }

    @PostMapping("/calcular")
    public String calcularSimulacion(@ModelAttribute("simulacionDto") SimulacionDto dto, Model model) {
        try {
            SimulacionDto resultado = simulacionDtoService.calcularSimulacion(dto);

            if ("BONDDIA".equalsIgnoreCase(dto.getTipoInstrumentoDescripcion()) && dto.getPlazoDiasManual() != null) {
                resultado.setPlazoTexto(dto.getPlazoDiasManual() + " día(s)");
            }

            boolean esTasaCupon = List.of("BONO", "UDIBONOS", "BONDES")
                    .contains(resultado.getTipoInstrumentoDescripcion().toUpperCase());

            model.addAttribute("simulacionDto", resultado);
            model.addAttribute("esTasaCupon", esTasaCupon);

            if (dto.getIdCartera() != null) {
                Map<String, List<DetalleSimulacionDto>> detallesPorTipo = simulacionDtoService.agruparDetallesPorTipo(resultado.getDetalles());
                model.addAttribute("detallesPorTipo", detallesPorTipo);
                return "simulaciones/simular-cartera";
            }

            return "simulaciones/simular-instrumento";

        } catch (IllegalArgumentException ex) {
            log.warn("Error al calcular simulación: {}", ex.getMessage());
            model.addAttribute("simulacionDto", dto);
            model.addAttribute("error", ex.getMessage());

            boolean esTasaCupon = List.of("BONO", "UDIBONOS", "BONDES")
                    .contains(dto.getTipoInstrumentoDescripcion().toUpperCase());
            model.addAttribute("esTasaCupon", esTasaCupon);

            if (dto.getIdCartera() != null) {
                return "simulaciones/simular-cartera";
            }

            return "simulaciones/simular-instrumento";
        }
    }

    @GetMapping("/analista")
    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    public String simulacionesAnalista(@RequestParam(name = "tipo", defaultValue = "compartidas") String tipo,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<SimulacionDto> simulaciones;

        if ("personales".equalsIgnoreCase(tipo)) {
            String correo = getCorreoUsuarioAutenticado();
            Integer idUsuario = simulacionDtoService.buscarIdUsuarioPorCorreo(correo);
            simulaciones = simulacionDtoService.findByUsuarioId(idUsuario, pageable);
        } else {
            simulaciones = simulacionDtoService.findCompartidasParaAnalista(pageable);
        }

        model.addAttribute("simulaciones", simulaciones.getContent());
        model.addAttribute("totalPages", simulaciones.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("tipo", tipo);

        log.info("Simulaciones para analista cargadas: tipo '{}', página {}", tipo, page);
        return "simulaciones/analista-list";
    }

    @GetMapping("/inversionista")
    @PreAuthorize("hasRole('ROLE_INVERSIONISTA')")
    public String simulacionesInversionista(@RequestParam(name = "page", defaultValue = "0") int page,
                                            Model model) {
        String correo = getCorreoUsuarioAutenticado();
        Integer idUsuario = simulacionDtoService.buscarIdUsuarioPorCorreo(correo);

        Pageable pageable = PageRequest.of(page, 10);
        Page<SimulacionDto> simulaciones = simulacionDtoService.findByUsuarioId(idUsuario, pageable);

        model.addAttribute("simulaciones", simulaciones.getContent());
        model.addAttribute("totalPages", simulaciones.getTotalPages());
        model.addAttribute("currentPage", page);

        log.info("Simulaciones del inversionista cargadas: correo {}, página {}", correo, page);
        return "simulaciones/inversionista-list";
    }

    @GetMapping("/inversionista/delete/{id}")
    @PreAuthorize("hasRole('ROLE_INVERSIONISTA')")
    public String deleteSimulacionInversionista(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            SimulacionDto simulacion = simulacionDtoService.findById(id);
            String correo = getCorreoUsuarioAutenticado();

            if (!correo.equals(simulacion.getCorreoUsuario())) {
                log.warn("Inversionista intentó borrar simulación que no le pertenece ID: {}", id);
                redirectAttrs.addFlashAttribute("error", "No puedes eliminar simulaciones de otro usuario.");
                return "redirect:/simulaciones/inversionista";
            }

            simulacionDtoService.delete(id);
            redirectAttrs.addFlashAttribute("mensaje", "Simulación eliminada exitosamente.");
            log.info("Simulación eliminada por inversionista ID: {}", id);
        } catch (Exception ex) {
            log.error("Error al eliminar simulación ID {}: {}", id, ex.getMessage(), ex);
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar la simulación: " + ex.getMessage());
        }

        return "redirect:/simulaciones/inversionista";
    }

    @GetMapping("/informacion")
    public String informacionSimulador() {
        return "simulaciones/informacion";
    }
}
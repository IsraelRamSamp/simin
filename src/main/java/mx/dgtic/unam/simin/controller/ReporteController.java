package mx.dgtic.unam.simin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.ReporteDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.service.dto.ReporteDtoService;
import mx.dgtic.unam.simin.service.dto.SimulacionDtoService;
import mx.dgtic.unam.simin.util.PdfGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private SimulacionDtoService simulacionDtoService;

    @Autowired
    private ReporteDtoService reporteDtoService;

    @Autowired
    private PdfGeneratorUtil pdfGeneratorUtil;

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private String getCorreoUsuarioAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listarReportes(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
                                 @RequestParam(required = false) String correo,
                                 @RequestParam(required = false) String nombre,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<ReporteDto> reportes = reporteDtoService.findByFiltrosAdmin(
                fechaInicio != null ? fechaInicio.atStartOfDay() : null,
                fechaFin != null ? fechaFin.atTime(23, 59, 59) : null,
                correo,
                nombre,
                pageable
        );

        log.info("Administrador consultó reportes. Página: {}, Filtros - correo: {}, nombre: {}", page, correo, nombre);

        model.addAttribute("reportes", reportes.getContent());
        model.addAttribute("totalPages", reportes.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("correo", correo);
        model.addAttribute("nombre", nombre);

        return "reportes/list";
    }

    @GetMapping("/simulacion/{id}/ver")
    public void verPdfSimulacion(@PathVariable("id") Integer idSimulacion, HttpServletResponse response) {
        log.info("Petición para ver PDF de simulación ID: {}", idSimulacion);
        generarPdfSimulacion(idSimulacion, response, false);
    }

    @GetMapping("/simulacion/{id}/descargar")
    public void descargarPdfSimulacion(@PathVariable("id") Integer idSimulacion, HttpServletResponse response) {
        log.info("Petición para descargar PDF de simulación ID: {}", idSimulacion);
        generarPdfSimulacion(idSimulacion, response, true);
    }

    private void generarPdfSimulacion(Integer idSimulacion, HttpServletResponse response, boolean forzarDescarga) {
        try {
            SimulacionDto simulacionDto;
            try {
                simulacionDto = simulacionDtoService.findById(idSimulacion);
            } catch (EntityNotFoundException e) {
                log.warn("Simulación no encontrada ID: {}", idSimulacion);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La simulación no fue encontrada.");
                return;
            }

            String correo = getCorreoUsuarioAutenticado();
            boolean esAdmin = hasRole("ROLE_ADMIN");
            boolean esAnalista = hasRole("ROLE_ANALISTA");
            boolean esPropietario = correo.equals(simulacionDto.getCorreoUsuario());

            if (!esAdmin && !esPropietario && !(esAnalista && simulacionDto.getCompartidaAnalista())) {
                log.warn("Acceso denegado al PDF. Usuario: {}, Simulación: {}", correo, idSimulacion);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para acceder a este PDF.");
                return;
            }

            ByteArrayInputStream bis = pdfGeneratorUtil.generarReporteSimulacion(simulacionDto);

            if (!reporteDtoService.existePorSimulacionId(simulacionDto.getIdSimulacion())) {
                ReporteDto historial = new ReporteDto();
                historial.setIdSimulacion(simulacionDto.getIdSimulacion());
                historial.setIdTipoReporte(1);
                historial.setNombreUsuario(simulacionDto.getNombreUsuario());
                historial.setCorreoUsuario(simulacionDto.getCorreoUsuario());

                if (simulacionDto.getIdCartera() != null) {
                    historial.setIdCartera(simulacionDto.getIdCartera());
                    historial.setTipoOrigen("CARTERA");
                } else if (simulacionDto.getIdInstrumento() != null) {
                    historial.setTipoOrigen("INSTRUMENTO");
                }

                reporteDtoService.save(historial);
                log.info("Historial de reporte registrado para simulación ID: {}", simulacionDto.getIdSimulacion());
            }

            response.setContentType("application/pdf");
            String disposition = forzarDescarga ? "attachment" : "inline";
            response.setHeader("Content-Disposition", disposition + "; filename=simulacion_" + idSimulacion + ".pdf");

            byte[] buffer = new byte[1024];
            int bytesRead;
            OutputStream out = response.getOutputStream();
            while ((bytesRead = bis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();

            log.info("PDF generado exitosamente para simulación ID: {}", idSimulacion);

        } catch (IOException e) {
            log.error("Error al generar el PDF de la simulación ID {}: {}", idSimulacion, e.getMessage(), e);
            throw new RuntimeException("Error al generar el PDF de la simulación", e);
        }
    }

    @GetMapping("/mis-reportes")
    @PreAuthorize("hasRole('ROLE_INVERSIONISTA')")
    public String reportesInversionista(@RequestParam(defaultValue = "0") int page,
                                        Model model) {
        String correo = getCorreoUsuarioAutenticado();
        Pageable pageable = PageRequest.of(page, 10);

        Page<ReporteDto> reportes = reporteDtoService.findByCorreoUsuario(correo, pageable);

        log.info("Inversionista accedió a sus reportes. Correo: {}, Página: {}", correo, page);

        model.addAttribute("reportes", reportes.getContent());
        model.addAttribute("totalPages", reportes.getTotalPages());
        model.addAttribute("currentPage", page);
        return "reportes/inversionista-list";
    }

    @GetMapping("/analista")
    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    public String reportesAnalista(@RequestParam(defaultValue = "0") int page,
                                   Model model) {
        String correo = getCorreoUsuarioAutenticado();
        Pageable pageable = PageRequest.of(page, 10);

        Page<ReporteDto> reportes = reporteDtoService.findReportesParaAnalista(correo, pageable);

        log.info("Analista accedió a reportes. Correo: {}, Página: {}", correo, page);

        model.addAttribute("reportes", reportes.getContent());
        model.addAttribute("totalPages", reportes.getTotalPages());
        model.addAttribute("currentPage", page);
        return "reportes/analista-list";
    }

    @GetMapping("/simulacion/{id}/eliminar")
    public String eliminarReportePorSimulacion(@PathVariable("id") Integer idSimulacion,
                                               Principal principal,
                                               RedirectAttributes redirectAttrs) {
        try {
            SimulacionDto simulacion = simulacionDtoService.findById(idSimulacion);
            String correoUsuario = principal.getName();
            boolean esAdmin = hasRole("ROLE_ADMIN");
            boolean esAnalista = hasRole("ROLE_ANALISTA");
            boolean esInversionista = hasRole("ROLE_INVERSIONISTA");

            boolean esPropietario = correoUsuario.equals(simulacion.getCorreoUsuario());

            if (esAdmin || (esAnalista && esPropietario) || (esInversionista && esPropietario)) {
                reporteDtoService.deleteByIdSimulacion(idSimulacion);
                redirectAttrs.addFlashAttribute("mensaje", "Reporte eliminado correctamente.");
                log.info("Reporte eliminado para simulación ID: {} por usuario: {}", idSimulacion, correoUsuario);
            } else {
                redirectAttrs.addFlashAttribute("error", "No tienes permiso para eliminar este reporte.");
                log.warn("Intento de eliminación sin permisos. Usuario: {}, Simulación: {}", correoUsuario, idSimulacion);
            }

        } catch (Exception e) {
            log.error("Error al eliminar el reporte de simulación ID {}: {}", idSimulacion, e.getMessage(), e);
            redirectAttrs.addFlashAttribute("error", "Ocurrió un error al intentar eliminar el reporte.");
        }

        if (hasRole("ROLE_ADMIN")) return "redirect:/reportes/list";
        if (hasRole("ROLE_ANALISTA")) return "redirect:/reportes/analista";
        return "redirect:/reportes/mis-reportes";
    }

}